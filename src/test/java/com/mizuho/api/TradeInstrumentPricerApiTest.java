package com.mizuho.api;

import com.mizuho.service.TradeInstrumentPriceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.mizuho.testutils.TradePriceStub.createTradePrice;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TradeInstrumentPricerApi.class)
public class TradeInstrumentPricerApiTest {

    @MockBean
    private TradeInstrumentPriceService tradeInstrumentPriceService;

    //TODO: this is karana
    @Autowired
    private MockMvc mockMvc;

    @Test
    void addTradePriceWorks() throws Exception {
        mockMvc.perform(post("/api/price")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"vendorId\":3,\"instrumentId\":3,\"name\":\"TEST_Vendor\",\"instrumentName\":\"MS\"," +
                        "\"bid\":102.34,\"ask\":103.55, \"timestamp\":\"2020-12-06T19:09:26\"}"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(status().isAccepted());
    }

    @Test
    void addTradePriceFailsDueToInvalidPayload() throws Exception {
        mockMvc.perform(post("/api/price")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[" + getJsonAsString() + "]"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getAllPrices() throws Exception {
        when(tradeInstrumentPriceService.loadAll())
                .thenReturn(List.of(createTradePrice("requestId"), createTradePrice("requestId")));

        mockMvc.perform(get("/api/prices"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[" + getJsonAsString() + "," + getJsonAsString() + "]"));

    }

    @Test
    void getByVendor() throws Exception {
        when(tradeInstrumentPriceService.findByVendor(Long.valueOf("3")))
                .thenReturn(List.of(createTradePrice("requestId")));

        mockMvc.perform(get("/api/vendor/3/prices"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[" + getJsonAsString() + "]"));
    }

    @Test
    void getByInstrument() throws Exception {
        when(tradeInstrumentPriceService.findByInstrument("MS"))
                .thenReturn(List.of(createTradePrice("requestId")));

        mockMvc.perform(get("/api/instrument/MS/prices"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[" + getJsonAsString() + "]"));
    }

    private String getJsonAsString() {
        return "{\"id\":\"requestId\",\"vendor\":{\"id\":3,\"name\":\"TEST_Vendor\"}," +
                "\"instrument\":{\"id\":3,\"instrumentName\":\"MS\"},\"bid\":102.34,\"ask\":103.55,\"timestamp\":null}";
    }

}