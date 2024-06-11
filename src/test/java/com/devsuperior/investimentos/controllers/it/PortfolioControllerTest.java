package com.devsuperior.investimentos.controllers.it;

import com.devsuperior.investimentos.dto.portfolio.PortfolioDTO;
import com.devsuperior.investimentos.dto.stockPortfolio.StockPortfolioSaleDTO;
import com.devsuperior.investimentos.testes.Factory;
import com.devsuperior.investimentos.testes.TokenUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
class PortfolioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private String usernameClientWithAccount, password, usernameClientWithoutAccount;
    private String bearerTokenClientWithAccount, bearerTokenClientWithoutAccount;
    private String existingStock, nonExistingStock;
    private Long existingId, nonExistingId;
    private PortfolioDTO portfolioDTO;
    private StockPortfolioSaleDTO stockPortfolioSaleDTO;

    @BeforeEach
    void setUp() throws Exception {

        usernameClientWithAccount = "lucas@gmail.com";
        usernameClientWithoutAccount = "maria@gmail.com";
        existingStock = "AMZO34";
        nonExistingStock = "NÃO EXISTE";
        existingId = 1L;
        nonExistingId = 100L;
        password = "123456";
        portfolioDTO = Factory.createPortfolioDTOforIT();
        stockPortfolioSaleDTO = Factory.createStockPortfolioSaleDTO();

        bearerTokenClientWithAccount = tokenUtil.obtainAccessToken(mockMvc, usernameClientWithAccount, password);
        bearerTokenClientWithoutAccount = tokenUtil.obtainAccessToken(mockMvc, usernameClientWithoutAccount, password);

    }

    @Test
    public void insertShouldReturnPortfolioDTOWhenUserLoggedWithAccount() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(portfolioDTO);

        ResultActions result =mockMvc.perform(post("/portfolios")
                .header("Authorization", "Bearer " + bearerTokenClientWithAccount)
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(3L));
        result.andExpect(jsonPath("$.totalValue").value(0));
        result.andExpect(jsonPath("$.accountId").value(1));

    }

    @Test
    public void insertShould404WhenUserLoggedWithoutAccount() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(portfolioDTO);

        ResultActions result =mockMvc.perform(post("/portfolios")
                .header("Authorization", "Bearer " + bearerTokenClientWithoutAccount)
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isNotFound());
        result.andExpect(jsonPath("$.status").value(404));
        result.andExpect(jsonPath("$.path").exists());
        result.andExpect(jsonPath("$.error").exists());
        result.andExpect(jsonPath("$.timestamp").exists());

    }

    @Test
    public void insertShouldReturn401WhenUserNonLogged() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(portfolioDTO);

        ResultActions result =mockMvc.perform(post("/portfolios")
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnauthorized());
    }

    @Test
    public void insertShould422WhenWhenUserLoggedWithAccountAndDescriptionIsInvalid() throws Exception {

        portfolioDTO.setDescription(" ");
        String jsonBody = objectMapper.writeValueAsString(portfolioDTO);

        ResultActions result =mockMvc.perform(post("/portfolios")
                .header("Authorization", "Bearer " + bearerTokenClientWithAccount)
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnprocessableEntity());
        result.andExpect(jsonPath("$.status").value(422));
        result.andExpect(jsonPath("$.path").exists());
        result.andExpect(jsonPath("$.error").exists());
        result.andExpect(jsonPath("$.timestamp").exists());

    }

    @Test
    public void findByIdShouldReturn200AndPortfolioDTOWhenIdExistsAndIsSelf() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(portfolioDTO);

        ResultActions result =mockMvc.perform(get("/portfolios/{id}", existingId)
                .header("Authorization", "Bearer " + bearerTokenClientWithAccount)
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(1L));
        result.andExpect(jsonPath("$.totalValue").value(0.0));
        result.andExpect(jsonPath("$.description").value("Ativos"));

    }

    @Test
    public void findByIdShouldReturn404WhenIdNonExists() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(portfolioDTO);

        ResultActions result =mockMvc.perform(get("/portfolios/{id}", nonExistingId)
                .header("Authorization", "Bearer " + bearerTokenClientWithoutAccount)
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isNotFound());
        result.andExpect(jsonPath("$.status").value(404));
        result.andExpect(jsonPath("$.path").exists());
        result.andExpect(jsonPath("$.error").exists());
        result.andExpect(jsonPath("$.timestamp").exists());

    }

    @Test
    public void findByIdShouldReturn403WhenIdExistsAndIsNonSelf() throws Exception {

        Long idIsNonSelf = 2L;

        String jsonBody = objectMapper.writeValueAsString(portfolioDTO);

        ResultActions result =mockMvc.perform(get("/portfolios/{id}", idIsNonSelf)
                .header("Authorization", "Bearer " + bearerTokenClientWithAccount)
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isForbidden());
        result.andExpect(jsonPath("$.status").value(403));
        result.andExpect(jsonPath("$.path").exists());
        result.andExpect(jsonPath("$.error").exists());
        result.andExpect(jsonPath("$.timestamp").exists());

    }

    @Test
    public void findByIdShouldReturn401WhenUserNonLogged() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(portfolioDTO);

        ResultActions result =mockMvc.perform(get("/portfolios/{id}", existingId)
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnauthorized());
    }

    @Test
    public void findAllShouldReturn200AndListPortfolioDTOWhenUserLoggedWithAccount() throws Exception{

        ResultActions result =mockMvc.perform(get("/portfolios")
                .header("Authorization", "Bearer " + bearerTokenClientWithAccount)
                .accept(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$[0].id").value(1L));
        result.andExpect(jsonPath("$[0].totalValue").value(0));
        result.andExpect(jsonPath("$[0].accountId").value(1));
        result.andExpect(jsonPath("$[0].stockPortfolios[0].symbol").exists());

    }

    @Test
    public void findAllShouldReturn404WhenUserLoggedWithoutAccount() throws Exception{

        ResultActions result =mockMvc.perform(get("/portfolios")
                .header("Authorization", "Bearer " + bearerTokenClientWithoutAccount)
                .accept(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isNotFound());
        result.andExpect(jsonPath("$.status").value(404));
        result.andExpect(jsonPath("$.path").exists());
        result.andExpect(jsonPath("$.error").exists());
        result.andExpect(jsonPath("$.timestamp").exists());

    }

    @Test
    public void findAllShouldReturn401WhenUserNonLogged() throws Exception {

        ResultActions result =mockMvc.perform(get("/portfolios")
                .accept(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnauthorized());
    }

    @Test
    public void comparisonShouldReturn200AndListStockComparisonDTOWhenExistingId() throws Exception{

        ResultActions result =mockMvc.perform(get("/portfolios/{id}/comparison", existingId)
                .header("Authorization", "Bearer " + bearerTokenClientWithAccount)
                .accept(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$[0].symbol").exists());
        result.andExpect(jsonPath("$[0].priceActual").exists());
        result.andExpect(jsonPath("$[0].quantity").exists());

    }

    @Test
    public void comparisonShouldReturn404WhenIdNonExisting() throws Exception{

        ResultActions result =mockMvc.perform(get("/portfolios/{id}/comparison", nonExistingId)
                .header("Authorization", "Bearer " + bearerTokenClientWithoutAccount)
                .accept(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isNotFound());
        result.andExpect(jsonPath("$.status").value(404));
        result.andExpect(jsonPath("$.path").exists());
        result.andExpect(jsonPath("$.error").exists());
        result.andExpect(jsonPath("$.timestamp").exists());

    }

    @Test
    public void comparisonShouldReturn401WhenUserNonLogged() throws Exception {

        ResultActions result =mockMvc.perform(get("/portfolios/{id}/comparison", existingId)
                .accept(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnauthorized());
    }

    @Test
    public void findByStockIdAndPortfolioIdShouldReturn200AndStockPortfolioDTOWhenExistingIds() throws Exception{

        ResultActions result =mockMvc.perform(get("/portfolios/{id}/{stockId}", existingId, existingStock)
                .header("Authorization", "Bearer " + bearerTokenClientWithAccount)
                .accept(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.symbol").value("AMZO34"));
        result.andExpect(jsonPath("$.price").value(40));
        result.andExpect(jsonPath("$.quantity").value(5));

    }

    @Test
    public void findByStockIdAndPortfolioIdShouldReturn404WhenIdsNonExisting() throws Exception{

        ResultActions result =mockMvc.perform(get("/portfolios/{id}/{stockId}", nonExistingId, nonExistingStock)
                .header("Authorization", "Bearer " + bearerTokenClientWithAccount)
                .accept(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isNotFound());
        result.andExpect(jsonPath("$.status").value(404));
        result.andExpect(jsonPath("$.path").exists());
        result.andExpect(jsonPath("$.error").exists());
        result.andExpect(jsonPath("$.timestamp").exists());

    }

    @Test
    public void findByStockIdAndPortfolioIdShouldReturn401WhenUserNonLogged() throws Exception {

        ResultActions result =mockMvc.perform(get("/portfolios/{id}/{stockId}", existingId, existingStock)
                .accept(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnauthorized());
    }

    @Test
    public void saleShouldReturn200AndStockPortfolioSaleResponseDTOWhenUserLoggedAndExistingIdsAndPasswordIsCorrectAndQuantityIsValid() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(stockPortfolioSaleDTO);

        ResultActions result =mockMvc.perform(put("/portfolios/{id}/{stockId}/sale", existingId, existingStock)
                .header("Authorization", "Bearer " + bearerTokenClientWithAccount)
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.priceSale").exists());
        result.andExpect(jsonPath("$.pricePurchased").value(40));
        result.andExpect(jsonPath("$.saleValue").exists());
        result.andExpect(jsonPath("$.quantitySale").value(5));

    }

    @Test
    public void saleShouldReturn404WhenUserLoggedAndNonExistingIds() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(stockPortfolioSaleDTO);

        ResultActions result =mockMvc.perform(put("/portfolios/{id}/{stockId}/sale", nonExistingId, nonExistingStock)
                .header("Authorization", "Bearer " + bearerTokenClientWithAccount)
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isNotFound());
        result.andExpect(jsonPath("$.status").value(404));
        result.andExpect(jsonPath("$.path").exists());
        result.andExpect(jsonPath("$.error").exists());
        result.andExpect(jsonPath("$.timestamp").exists());

    }

    @Test
    public void saleShouldReturn401WhenUserNonLogged() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(stockPortfolioSaleDTO);

        ResultActions result =mockMvc.perform(put("/portfolios/{id}/{stockId}/sale", existingId, existingStock)
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnauthorized());

    }

    @Test
    public void saleShouldReturn400WhenUserLoggedAndExistingIdsAndPasswordIsIncorrect() throws Exception {

        stockPortfolioSaleDTO.setPassword("errado");
        String jsonBody = objectMapper.writeValueAsString(stockPortfolioSaleDTO);

        ResultActions result =mockMvc.perform(put("/portfolios/{id}/{stockId}/sale", existingId, existingStock)
                .header("Authorization", "Bearer " + bearerTokenClientWithAccount)
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isBadRequest());
        result.andExpect(jsonPath("$.status").value(400));
        result.andExpect(jsonPath("$.path").exists());
        result.andExpect(jsonPath("$.error").exists());
        result.andExpect(jsonPath("$.timestamp").exists());

    }

    @Test
    public void saleShouldReturn400WhenUserLoggedAndExistingIdsAndPasswordIsCorrectAndQuantityIsBiggerThanQuantityOfStockPortfolio() throws Exception {

        stockPortfolioSaleDTO.setQuantity(10);
        String jsonBody = objectMapper.writeValueAsString(stockPortfolioSaleDTO);

        ResultActions result =mockMvc.perform(put("/portfolios/{id}/{stockId}/sale", existingId, existingStock)
                .header("Authorization", "Bearer " + bearerTokenClientWithAccount)
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isBadRequest());
        result.andExpect(jsonPath("$.status").value(400));
        result.andExpect(jsonPath("$.path").exists());
        result.andExpect(jsonPath("$.error").exists());
        result.andExpect(jsonPath("$.error").value("Quantidade inválida"));
        result.andExpect(jsonPath("$.timestamp").exists());

    }

    @Test
    public void saleShouldReturn422WhenUserLoggedAndExistingIdsAndPasswordIsCorrectAndQuantityNegative() throws Exception {

        stockPortfolioSaleDTO.setQuantity(-10);
        String jsonBody = objectMapper.writeValueAsString(stockPortfolioSaleDTO);

        ResultActions result =mockMvc.perform(put("/portfolios/{id}/{stockId}/sale", existingId, existingStock)
                .header("Authorization", "Bearer " + bearerTokenClientWithAccount)
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnprocessableEntity());
        result.andExpect(jsonPath("$.status").value(422));
        result.andExpect(jsonPath("$.path").exists());
        result.andExpect(jsonPath("$.error").exists());
        result.andExpect(jsonPath("$.errors[0].fieldName").value("quantity"));
        result.andExpect(jsonPath("$.timestamp").exists());

    }

}