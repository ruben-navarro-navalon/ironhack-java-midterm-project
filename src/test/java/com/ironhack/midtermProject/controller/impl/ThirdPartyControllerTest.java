package com.ironhack.midtermProject.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midtermProject.controller.dto.ThirdPartyDTO;
import com.ironhack.midtermProject.repository.ThirdPartyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ThirdPartyControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @AfterEach
    void tearDown() {
        thirdPartyRepository.deleteAll();
    }

    @Test
    void create() throws Exception {
        ThirdPartyDTO thirdPartyDTO = new ThirdPartyDTO("Antonio López", "qwertyuiop");
        String body = objectMapper.writeValueAsString(thirdPartyDTO);
        System.out.println(body);
        MvcResult result = mockMvc.perform(
                post("/admin/third-party")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("admin")
                                .password("theMostEpicPasswordInTheWorldHistoryFromTheBeginningOfTimesEvenMoreEpicThanHydrangeas")
                                .roles("ADMIN"))
        ).andExpect(status().isCreated()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("Antonio"));
    }
    @Test
    void create_NoUserLogged_exception() throws Exception {
        ThirdPartyDTO thirdPartyDTO = new ThirdPartyDTO("Antonio López", "qwertyuiop");
        String body = objectMapper.writeValueAsString(thirdPartyDTO);
        System.out.println(body);
        MvcResult result = mockMvc.perform(
                post("/admin/third-party")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized()).andReturn();
    }
    @Test
    void create_NoAdminLogged_exception() throws Exception {
        ThirdPartyDTO thirdPartyDTO = new ThirdPartyDTO("Antonio López", "qwertyuiop");
        String body = objectMapper.writeValueAsString(thirdPartyDTO);
        System.out.println(body);
        MvcResult result = mockMvc.perform(
                post("/admin/third-party")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("account_holder")
                                .password("Hydrangeas")
                                .roles("ACCOUNT_HOLDER"))
        ).andExpect(status().isForbidden()).andReturn();
    }
}