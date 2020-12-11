package com.mizuho.publish;

import com.mizuho.model.TradePrice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class TradePricePublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(TradePricePublisher.class);

    @Autowired
    private JmsTemplate jmsTopicTemplate;

    @Value("${topic.consumer.name:trade-instrument-price-topic}")
    private String topicName;

    public void publish(TradePrice tradePrice) {
        LOGGER.info("publishing trade price {}", tradePrice);
        jmsTopicTemplate.convertAndSend(topicName, tradePrice);
    }

}
