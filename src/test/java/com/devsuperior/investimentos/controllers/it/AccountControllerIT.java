package com.devsuperior.investimentos.controllers.it;

import com.devsuperior.investimentos.dto.account.AccountDTO;
import com.devsuperior.investimentos.dto.account.DepositDTO;
import com.devsuperior.investimentos.dto.account.WithdrawDTO;
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
class AccountControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private String usernameClientWithAccount, password, usernameClientWithoutAccount;
    private String bearerTokenClientWithAccount, bearerTokenClientWithoutAccount;
    private AccountDTO accountDTO;
    private DepositDTO depositDTO;
    private WithdrawDTO withdrawDTO;

    @BeforeEach
    void setUp() throws Exception {

        usernameClientWithAccount = "lucas@gmail.com";
        usernameClientWithoutAccount = "maria@gmail.com";
        password = "123456";
        accountDTO = Factory.createAccountDTOForIT();
        depositDTO = Factory.createDepositDTO();
        withdrawDTO = Factory.createWithdrawDTO();

        bearerTokenClientWithAccount = tokenUtil.obtainAccessToken(mockMvc, usernameClientWithAccount, password);
        bearerTokenClientWithoutAccount = tokenUtil.obtainAccessToken(mockMvc, usernameClientWithoutAccount, password);

    }

    @Test
    public void insertShouldReturnAccountDTOWhenUserLoggedWithoutAccount() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(accountDTO);

        ResultActions result =mockMvc.perform(post("/accounts")
                .header("Authorization", "Bearer " + bearerTokenClientWithoutAccount)
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(2L));
        result.andExpect(jsonPath("$.name").value(accountDTO.getName()));
        result.andExpect(jsonPath("$.description").value(accountDTO.getDescription()));

    }

    @Test
    public void insertShouldReturn400WhenUserLoggedWithAccount() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(accountDTO);

        ResultActions result =mockMvc.perform(post("/accounts")
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
    public void insertShouldReturn422WhenUserLoggedAndFieldDescriptionIsInvalid() throws Exception {

        accountDTO.setDescription("");
        String jsonBody = objectMapper.writeValueAsString(accountDTO);

        ResultActions result =mockMvc.perform(post("/accounts")
                .header("Authorization", "Bearer " + bearerTokenClientWithAccount)
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnprocessableEntity());
        result.andExpect(jsonPath("$.status").value(422));
        result.andExpect(jsonPath("$.errors[0].fieldName").value("description"));
        result.andExpect(jsonPath("$.path").exists());
        result.andExpect(jsonPath("$.error").exists());
        result.andExpect(jsonPath("$.timestamp").exists());

    }

    @Test
    public void insertShouldReturn401WhenUserNonLogged() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(accountDTO);

        ResultActions result =mockMvc.perform(post("/accounts")
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnauthorized());

    }

    @Test
    public void depositShouldReturn200AndBalanceDTOWhenUserLoggedAndAccountExists() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(depositDTO);

        ResultActions result =mockMvc.perform(post("/accounts/deposit")
                .header("Authorization", "Bearer " + bearerTokenClientWithAccount)
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.balance").value(2000));

    }

    @Test
    public void depositShouldReturn404WhenUserLoggedAndAccountNonExists() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(depositDTO);

        ResultActions result =mockMvc.perform(post("/accounts/deposit")
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
    public void depositShouldReturn400WhenUserLoggedAndAccountExistsAndPasswordIsIncorrect() throws Exception {

        depositDTO.setPassword("");
        String jsonBody = objectMapper.writeValueAsString(depositDTO);

        ResultActions result =mockMvc.perform(post("/accounts/deposit")
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
    public void depositShouldReturn401WhenUserNonLogged() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(depositDTO);

        ResultActions result =mockMvc.perform(post("/accounts/deposit")
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnauthorized());
    }

    @Test
    public void depositShouldReturn400WhenUserLoggedAndAccountExistsAndAmountIsNull() throws Exception {

        depositDTO.setAmount(null);
        String jsonBody = objectMapper.writeValueAsString(depositDTO);

        ResultActions result =mockMvc.perform(post("/accounts/deposit")
                .header("Authorization", "Bearer " + bearerTokenClientWithAccount)
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnprocessableEntity());
        result.andExpect(jsonPath("$.status").value(422));
        result.andExpect(jsonPath("$.errors[0].fieldName").value("amount"));
        result.andExpect(jsonPath("$.path").exists());
        result.andExpect(jsonPath("$.error").exists());
        result.andExpect(jsonPath("$.timestamp").exists());

    }

    @Test
    public void depositShouldReturn400WhenUserLoggedAndAccountExistsAndAmountIsNegative() throws Exception {

        depositDTO.setAmount(-100.0);
        String jsonBody = objectMapper.writeValueAsString(depositDTO);

        ResultActions result =mockMvc.perform(post("/accounts/deposit")
                .header("Authorization", "Bearer " + bearerTokenClientWithAccount)
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnprocessableEntity());
        result.andExpect(jsonPath("$.status").value(422));
        result.andExpect(jsonPath("$.errors[0].fieldName").value("amount"));
        result.andExpect(jsonPath("$.path").exists());
        result.andExpect(jsonPath("$.error").exists());
        result.andExpect(jsonPath("$.timestamp").exists());

    }

    @Test
    public void withdrawShouldReturn200AndBalanceDTOWhenUserLoggedAndAccountExists() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(withdrawDTO);

        ResultActions result =mockMvc.perform(post("/accounts/withdraw")
                .header("Authorization", "Bearer " + bearerTokenClientWithAccount)
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.balance").value(0));

    }

    @Test
    public void withdrawShouldReturn404WhenUserLoggedAndAccountNonExists() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(withdrawDTO);

        ResultActions result =mockMvc.perform(post("/accounts/withdraw")
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
    public void withdrawShouldReturn400WhenUserLoggedAndAccountExistsAndPasswordIsIncorrect() throws Exception {

        withdrawDTO.setPassword("");
        String jsonBody = objectMapper.writeValueAsString(withdrawDTO);

        ResultActions result =mockMvc.perform(post("/accounts/withdraw")
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
    public void withdrawShouldReturn401WhenUserNonLogged() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(withdrawDTO);

        ResultActions result =mockMvc.perform(post("/accounts/withdraw")
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnauthorized());
    }

    @Test
    public void withdrawShouldReturn400WhenUserLoggedAndAccountExistsAndAmountIsNull() throws Exception {

        withdrawDTO.setAmount(null);
        String jsonBody = objectMapper.writeValueAsString(withdrawDTO);

        ResultActions result =mockMvc.perform(post("/accounts/withdraw")
                .header("Authorization", "Bearer " + bearerTokenClientWithAccount)
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnprocessableEntity());
        result.andExpect(jsonPath("$.status").value(422));
        result.andExpect(jsonPath("$.errors[0].fieldName").value("amount"));
        result.andExpect(jsonPath("$.path").exists());
        result.andExpect(jsonPath("$.error").exists());
        result.andExpect(jsonPath("$.timestamp").exists());

    }

    @Test
    public void withdrawShouldReturn40OWhenUserLoggedAndAccountExistsAndAmountIsNegative() throws Exception {

        withdrawDTO.setAmount(-100.0);
        String jsonBody = objectMapper.writeValueAsString(withdrawDTO);

        ResultActions result =mockMvc.perform(post("/accounts/withdraw")
                .header("Authorization", "Bearer " + bearerTokenClientWithAccount)
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnprocessableEntity());
        result.andExpect(jsonPath("$.status").value(422));
        result.andExpect(jsonPath("$.errors[0].fieldName").value("amount"));
        result.andExpect(jsonPath("$.path").exists());
        result.andExpect(jsonPath("$.error").exists());
        result.andExpect(jsonPath("$.timestamp").exists());

    }

    @Test
    public void withdrawShouldReturn40OWhenUserLoggedAndAccountExistsAndAmountBiggerThenBalance() throws Exception {

        withdrawDTO.setAmount(2000.0);
        String jsonBody = objectMapper.writeValueAsString(withdrawDTO);

        ResultActions result =mockMvc.perform(post("/accounts/withdraw")
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
    public void BalanceShouldReturn200AndBalanceDTOWhenUserLoggedAndAccountExists() throws Exception {

        ResultActions result =mockMvc.perform(get("/accounts/balance")
                .header("Authorization", "Bearer " + bearerTokenClientWithAccount)
                .accept(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.balance").value(1000));

    }

    @Test
    public void balanceShouldReturn404WhenUserLoggedAndAccountNonExists() throws Exception {

        ResultActions result =mockMvc.perform(get("/accounts/balance")
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
    public void balanceShouldReturn401WhenUserNonLogged() throws Exception {

        ResultActions result =mockMvc.perform(get("/accounts/balance")
                .accept(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnauthorized());
    }
}