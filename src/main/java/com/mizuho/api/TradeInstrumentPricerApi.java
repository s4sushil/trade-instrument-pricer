package com.mizuho.api;

import com.mizuho.model.Instrument;
import com.mizuho.model.TradePrice;
import com.mizuho.model.TradePriceRequest;
import com.mizuho.model.Vendor;
import com.mizuho.service.TradeInstrumentPriceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api")
public class TradeInstrumentPricerApi {

    private final TradeInstrumentPriceService tradeInstrumentPriceService;

    public TradeInstrumentPricerApi(TradeInstrumentPriceService tradeInstrumentPriceService) {
        this.tradeInstrumentPriceService = tradeInstrumentPriceService;
    }

    @PostMapping(value = "/price", produces = "application/json", consumes = "application/json")
    public ResponseEntity<String> addTradePrice(@RequestBody TradePriceRequest priceRequest) {

        String requestId = String.join("_", String.valueOf(priceRequest.getVendorId()), String.valueOf(priceRequest.getInstrumentId()));
        TradePrice tradePrice = new TradePrice(requestId, new Vendor(priceRequest.getVendorId(), priceRequest.getName()),
                new Instrument(priceRequest.getInstrumentId(), priceRequest.getInstrumentName()),
                priceRequest.getBid(), priceRequest.getAsk(), priceRequest.getTimestamp());

        tradeInstrumentPriceService.addOrUpdate(tradePrice);
        return ResponseEntity.accepted().build();
    }

    @GetMapping(value = "/prices", produces = "application/json")
    public Collection<TradePrice> getAllPrices() {
        return tradeInstrumentPriceService.loadAll();
    }

    @GetMapping(value = "/vendor/{id}/prices", produces = "application/json")
    public Collection<TradePrice> getByVendor(@PathVariable Long id) {
        return tradeInstrumentPriceService.findByVendor(id);
    }

    @GetMapping(value = "/instrument/{instrumentName}/prices", produces = "application/json")
    public Collection<TradePrice> getByInstrument(@PathVariable String instrumentName) {
        return tradeInstrumentPriceService.findByInstrument(instrumentName);
    }
}
