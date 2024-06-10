package com.devsuperior.investimentos.controllers.it;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String stockIdExisting;
    private String stockIdNonExisting;

    @BeforeEach
    void setUp() {
        stockIdExisting = "AMZO34";
        stockIdNonExisting = "NÃO EXISTE";
    }


    @Test
    public void findByIdShouldReturnStockBrapiFindByIdDTOWhenBrapiFoundStock() throws Exception {

        ResultActions result =mockMvc.perform(get("/stocks/{stockId}", stockIdExisting)
                .accept(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.symbol").value("AMZO34"));
        result.andExpect(jsonPath("$.longName").exists());
        result.andExpect(jsonPath("$.regularMarketPrice").exists());
    }

    @Test
    public void findByIdShouldReturn404WhenBrapiNotFoundStock() throws Exception {

        ResultActions result =mockMvc.perform(get("/stocks/{stockId}", stockIdNonExisting)
                .accept(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isNotFound());
        result.andExpect(jsonPath("$.status").value(404));
        result.andExpect(jsonPath("$.timestamp").exists());
        result.andExpect(jsonPath("$.error").exists());
        result.andExpect(jsonPath("$.path").exists());
    }

    @Test
    public void getAllShouldReturnBrapiResponseListDTOWhenBrapiSearchFoundStocks() throws Exception {

        ResultActions result =mockMvc.perform(get("/stocks")
                .queryParam("search", stockIdExisting)
                .accept(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.stocks").exists());
        result.andExpect(jsonPath("$.stocks[0].stock").value("AMZO34"));
    }

    @Test
    public void getAllShouldReturnBrapiResponseListDTOWithStocksEmptyWhenBrapiSearchFoundStocks() throws Exception {

        ResultActions result =mockMvc.perform(get("/stocks")
                .queryParam("search", stockIdExisting)
                .accept(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.stocks").exists());
    }

}