package com.devsuperior.investimentos.controllers.it;

import com.devsuperior.investimentos.dto.UserDTO;
import com.devsuperior.investimentos.dto.UserDeleteDTO;
import com.devsuperior.investimentos.dto.UserInsertDTO;
import com.devsuperior.investimentos.testes.Factory;
import com.devsuperior.investimentos.testes.TokenUtil;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private String existingUsername, usernameInvalidFormat, password, bearerTokenClient, usernameNonDependent, bearerTokenClientNonDependent;
    private UserInsertDTO userInsertDTO;
    private UserDTO userDTO;
    private UserDeleteDTO userDeleteDTO;

    @BeforeEach
    void setUp() throws Exception {

        usernameInvalidFormat = "lucas.lucas";
        existingUsername = "lucas@gmail.com";
        usernameNonDependent = "maria@gmail.com";
        password = "123456";
        userInsertDTO = Factory.createUserInsertDTO();
        userDTO = Factory.createUserDTO();
        userDeleteDTO = Factory.createUserDeleteDTO();

        bearerTokenClient = tokenUtil.obtainAccessToken(mockMvc, existingUsername, password);
        bearerTokenClientNonDependent = tokenUtil.obtainAccessToken(mockMvc, usernameNonDependent, password);

    }

    @Test
    public void insertShouldReturn204WhenDataIsValid() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(userInsertDTO);

        ResultActions result =mockMvc.perform(post("/users")
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isNoContent());

    }

    @Test
    public void insertShouldReturn422WhenFirstNameIsBlank() throws Exception {

        userInsertDTO.setFirstName("  ");
        String jsonBody = objectMapper.writeValueAsString(userInsertDTO);

        ResultActions result =mockMvc.perform(post("/users")
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnprocessableEntity());
        result.andExpect(jsonPath("$.status").value(422));
        result.andExpect(jsonPath("$.path").exists());
        result.andExpect(jsonPath("$.error").exists());
        result.andExpect(jsonPath("$.errors[0].fieldName").value("firstName"));
        result.andExpect(jsonPath("$.timestamp").exists());

    }

    @Test
    public void insertShouldReturn422WhenLastNameIsBlank() throws Exception {

        userInsertDTO.setLastName("  ");
        String jsonBody = objectMapper.writeValueAsString(userInsertDTO);

        ResultActions result =mockMvc.perform(post("/users")
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnprocessableEntity());
        result.andExpect(jsonPath("$.status").value(422));
        result.andExpect(jsonPath("$.path").exists());
        result.andExpect(jsonPath("$.error").exists());
        result.andExpect(jsonPath("$.errors[0].fieldName").value("lastName"));
        result.andExpect(jsonPath("$.timestamp").exists());

    }

    @Test
    public void insertShouldReturn422WhenEmailInInvalidFormat() throws Exception {

        userInsertDTO.setEmail(usernameInvalidFormat);
        String jsonBody = objectMapper.writeValueAsString(userInsertDTO);

        ResultActions result =mockMvc.perform(post("/users")
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnprocessableEntity());
        result.andExpect(jsonPath("$.status").value(422));
        result.andExpect(jsonPath("$.path").exists());
        result.andExpect(jsonPath("$.error").exists());
        result.andExpect(jsonPath("$.errors[0].fieldName").value("email"));
        result.andExpect(jsonPath("$.timestamp").exists());

    }

    @Test
    public void insertShouldReturn422WhenEmailExistsInDatabase() throws Exception {

        userInsertDTO.setEmail(existingUsername);
        String jsonBody = objectMapper.writeValueAsString(userInsertDTO);

        ResultActions result =mockMvc.perform(post("/users")
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnprocessableEntity());
        result.andExpect(jsonPath("$.status").value(422));
        result.andExpect(jsonPath("$.path").exists());
        result.andExpect(jsonPath("$.error").exists());
        result.andExpect(jsonPath("$.errors[0].fieldName").value("email"));
        result.andExpect(jsonPath("$.timestamp").exists());

    }

    @Test
    public void insertShouldReturn400WhenBirthDateInvalidFormat() throws Exception {

        userInsertDTO.setBirthDate("22-10-2022");
        String jsonBody = objectMapper.writeValueAsString(userInsertDTO);

        ResultActions result =mockMvc.perform(post("/users")
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
    public void insertShouldReturn422WhenBirthDateIsLessThan18() throws Exception {

        userInsertDTO.setBirthDate("2010-03-05");
        String jsonBody = objectMapper.writeValueAsString(userInsertDTO);

        ResultActions result =mockMvc.perform(post("/users")
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnprocessableEntity());
        result.andExpect(jsonPath("$.status").value(422));
        result.andExpect(jsonPath("$.path").exists());
        result.andExpect(jsonPath("$.error").exists());
        result.andExpect(jsonPath("$.errors[0].fieldName").value("birthDate"));
        result.andExpect(jsonPath("$.timestamp").exists());

    }

    @Test
    public void updateShouldReturn200AndUserDTOWhenUserLoggedAndDataValid() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(userDTO);

        ResultActions result =mockMvc.perform(put("/users")
                .header("Authorization", "Bearer " + bearerTokenClient)
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(1L));
        result.andExpect(jsonPath("$.email").value(userDTO.getEmail()));

    }

    @Test
    public void updateShouldReturn401WhenUserNonLogged() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(userDTO);

        ResultActions result =mockMvc.perform(put("/users")
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnauthorized());

    }

    @Test
    public void updateShouldReturn422WhenFirstNameIsBlank() throws Exception {

        userDTO.setFirstName("  ");
        String jsonBody = objectMapper.writeValueAsString(userDTO);

        ResultActions result =mockMvc.perform(put("/users")
                .header("Authorization", "Bearer " + bearerTokenClient)
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnprocessableEntity());
        result.andExpect(jsonPath("$.status").value(422));
        result.andExpect(jsonPath("$.path").exists());
        result.andExpect(jsonPath("$.error").exists());
        result.andExpect(jsonPath("$.errors[0].fieldName").value("firstName"));
        result.andExpect(jsonPath("$.timestamp").exists());

    }

    @Test
    public void updateShouldReturn422WhenLastNameIsBlank() throws Exception {

        userDTO.setLastName("  ");
        String jsonBody = objectMapper.writeValueAsString(userDTO);

        ResultActions result =mockMvc.perform(put("/users")
                .header("Authorization", "Bearer " + bearerTokenClient)
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnprocessableEntity());
        result.andExpect(jsonPath("$.status").value(422));
        result.andExpect(jsonPath("$.path").exists());
        result.andExpect(jsonPath("$.error").exists());
        result.andExpect(jsonPath("$.errors[0].fieldName").value("lastName"));
        result.andExpect(jsonPath("$.timestamp").exists());

    }

    @Test
    public void updateShouldReturn422WhenEmailInInvalidFormat() throws Exception {

        userDTO.setEmail(usernameInvalidFormat);
        String jsonBody = objectMapper.writeValueAsString(userDTO);

        ResultActions result =mockMvc.perform(put("/users")
                .header("Authorization", "Bearer " + bearerTokenClient)
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnprocessableEntity());
        result.andExpect(jsonPath("$.status").value(422));
        result.andExpect(jsonPath("$.path").exists());
        result.andExpect(jsonPath("$.error").exists());
        result.andExpect(jsonPath("$.errors[0].fieldName").value("email"));
        result.andExpect(jsonPath("$.timestamp").exists());

    }

    @Test
    public void deleteShouldReturn204WhenUserLoggedAndPasswordCorrectAndUserIsNonDependentInDatabase() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(userDeleteDTO);

        ResultActions result =mockMvc.perform(delete("/users")
                .header("Authorization", "Bearer " + bearerTokenClientNonDependent)
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isNoContent());

    }

    @Test
    public void deleteShouldReturn401WhenUserNonLogged() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(userDeleteDTO);

        ResultActions result =mockMvc.perform(delete("/users")
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnauthorized());

    }

    @Test
    public void deleteShouldReturn400WhenUserLoggedAndPasswordIncorrect() throws Exception {

        userDeleteDTO.setPassword("errado");
        String jsonBody = objectMapper.writeValueAsString(userDeleteDTO);

        ResultActions result =mockMvc.perform(delete("/users")
                .header("Authorization", "Bearer " + bearerTokenClient)
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
    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteShouldReturn400WhenUserLoggedAndPasswordCorrectAndUserIsDependentInDatabase() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(userDeleteDTO);

        ResultActions result =mockMvc.perform(delete("/users")
                .header("Authorization", "Bearer " + bearerTokenClient)
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isBadRequest());
        result.andExpect(jsonPath("$.status").value(400));
        result.andExpect(jsonPath("$.path").exists());
        result.andExpect(jsonPath("$.error").exists());
        result.andExpect(jsonPath("$.error").value("Falha de integridade referencial"));
        result.andExpect(jsonPath("$.timestamp").exists());

    }


    @Test
    public void getMeShouldReturn200AndUserDTOWhenUserLogged() throws Exception {

        ResultActions result =mockMvc.perform(get("/users")
                .header("Authorization", "Bearer " + bearerTokenClient)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(1L));
        result.andExpect(jsonPath("$.email").value("lucas@gmail.com"));


    }

    @Test
    public void getMeShouldReturn401WhenUserNonLogged() throws Exception {

        ResultActions result =mockMvc.perform(get("/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print());

        result.andExpect(status().isUnauthorized());

    }

}