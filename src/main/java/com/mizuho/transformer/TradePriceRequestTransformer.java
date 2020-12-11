package com.mizuho.transformer;

import com.mizuho.model.TradePriceRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.integration.transformer.AbstractPayloadTransformer;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public class TradePriceRequestTransformer extends AbstractPayloadTransformer<Map<String, String>, TradePriceRequest> {
    private final Class<?> targetClass;

    public TradePriceRequestTransformer(Class<?> targetClass) {
        Assert.notNull(targetClass, "targetClass can not be null");
        this.targetClass = targetClass;
    }

    @Override
    protected TradePriceRequest transformPayload(Map<String, String> payload) {
        TradePriceRequest request = null;
        Object target = BeanUtils.instantiateClass(this.targetClass);
        if (target instanceof TradePriceRequest) {
            request = new TradePriceRequest(Long.valueOf(payload.get("vendorId")), Long.valueOf(payload.get("instrumentId")),
                    payload.get("name"), payload.get("instrumentName"), new BigDecimal(payload.get("bid")),
                    new BigDecimal(payload.get("ask")), LocalDateTime.now());
        }
        return request;
    }
}
