package com.mizuho.config;

import com.mizuho.datastore.MapInMemoryDataStore;
import com.mizuho.model.Instrument;
import com.mizuho.model.TradePrice;
import com.mizuho.model.TradePriceRequest;
import com.mizuho.model.Vendor;
import com.mizuho.service.TradeInstrumentPriceService;
import com.mizuho.transformer.TradePriceRequestTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.integration.MessageRejectedException;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.file.filters.AcceptOnceFileListFilter;
import org.springframework.integration.file.filters.ChainFileListFilter;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.jms.dsl.Jms;
import org.springframework.integration.transformer.AbstractTransformer;
import org.springframework.integration.transformer.MessageTransformationException;
import org.springframework.integration.transformer.Transformer;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;

import javax.jms.ConnectionFactory;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableJms
public class IntegrationFlowConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(IntegrationFlowConfig.class);

    private File filePoller() throws IOException {
        File file = new ClassPathResource("poller").getFile();
        return file;
    }

    @Bean
    public IntegrationFlow csvInputFlow() throws IOException {
        return IntegrationFlows
                .from(Files.inboundAdapter(filePoller())
                    .filter(new ChainFileListFilter<File>()
                        .addFilter(new AcceptOnceFileListFilter<>())
                        .addFilter(new SimplePatternFileListFilter("*.csv"))
                    ), e -> e.poller(Pollers.fixedDelay(1000)))
                .split(Files.splitter()
                        .charset(StandardCharsets.UTF_8)
                        .firstLineAsHeader("fileHeader")
                        .applySequence(true))
                .transform(csvToMapTransformer())
                .transform(new TradePriceRequestTransformer(TradePriceRequest.class))
                .log(LoggingHandler.Level.DEBUG)
                .channel(c -> c.direct("newPriceRequestChannel"))
                .get();
    }

    @Bean
    public IntegrationFlow jmsInputFlow(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        return IntegrationFlows.from(
                Jms.messageDrivenChannelAdapter(connectionFactory)
                        .jmsMessageConverter(messageConverter)
                        .destination("trade-price-input"))
                        .log()
                        .channel("newPriceRequestChannel")
                        .get();
    }

    /**
     * Service Activator to connect the incoming message flows to our TradePriceService.
     * Invalid new prices are rejected by the validator.
     */
    @Bean
    @ServiceActivator(inputChannel = "newPriceRequestChannel")
    public MessageHandler tradePriceServiceActivator(TradeInstrumentPriceService tradeInstrumentPriceService) throws InterruptedException {
        Thread.sleep(1000);
        return (msg) -> {
            Object payload = msg.getPayload();
            if (payload instanceof TradePriceRequest) {
                TradePriceRequest priceRequest = (TradePriceRequest) payload;
                if(priceRequest == null || priceRequest.getVendorId() == null) {
                    throw new MessageRejectedException(msg, "payload is empty");
                }

                String requestId = String.join("_", String.valueOf(priceRequest.getVendorId()), String.valueOf(priceRequest.getInstrumentId()));
                TradePrice tradePrice = new TradePrice(requestId, new Vendor(priceRequest.getVendorId(), priceRequest.getName()),
                        new Instrument(priceRequest.getInstrumentId(), priceRequest.getInstrumentName()),
                        priceRequest.getBid(), priceRequest.getAsk(), priceRequest.getTimestamp());

                tradeInstrumentPriceService.addOrUpdate(tradePrice);
            }
        };
    }

    @Bean
    public PublishSubscribeChannel errorChannel() {
        return new PublishSubscribeChannel();
    }

    /**
     * Log the errors without the stacktrace
     */
    @Bean
    @ServiceActivator(inputChannel = "errorChannel")
    public MessageHandler errHandler() {
        return msg -> LOGGER.error("{}", msg);
    }

    /**
     * turn a row of the incoming CSV file into a map
     */
    private Transformer csvToMapTransformer() {

        return new AbstractTransformer() {
            @Override
            public Object doTransform(Message msg) {
                Object header = msg.getHeaders().get("fileHeader");
                if (header == null) {
                    throw new MessageTransformationException(msg, "no 'fileHeader' header found");
                }

                String[] headers = header.toString().split(",");
                Map<String, String> result = new HashMap<>();
                String[] elements = msg.getPayload().toString().split(",");
                for (int i = 0; i < elements.length; i++) {
                    result.put(headers[i], elements[i]);
                }
                return result;
            }
        };

    }

    @Bean
    public MapInMemoryDataStore<String, TradePrice> inMemoryDataStore() {
        return new MapInMemoryDataStore<>();
    }

}
