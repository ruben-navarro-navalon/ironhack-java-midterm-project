package com.ironhack.midtermProject.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midtermProject.controller.dto.AccountHolderDTO;
import com.ironhack.midtermProject.controller.dto.AddressDTO;
import com.ironhack.midtermProject.model.AccountHolder;
import com.ironhack.midtermProject.repository.AccountHolderRepository;
import com.ironhack.midtermProject.repository.RoleRepository;
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

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AccountHolderControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    AccountHolderRepository accountHolderRepository;
    @Autowired
    RoleRepository roleRepository;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @AfterEach
    void tearDown() {
        roleRepository.deleteAll();
        accountHolderRepository.deleteAll();
    }

    @Test
    void create_validDTOWithOneAddress_newAccountHolder() throws Exception {
        AddressDTO address1DTO = new AddressDTO("C/ blabla", 28345, "Coslada", "Madrid");
        AccountHolderDTO accountHolderDTO = new AccountHolderDTO("José Navarro", "pepe56", "12345", LocalDate.of(1956,4,2), address1DTO);
        String body = objectMapper.writeValueAsString(accountHolderDTO);
        MvcResult result = mockMvc.perform(
                post("/admin/account-holder")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("admin")
                                .password("password")
                                .roles("ADMIN"))
        ).andExpect(status().isCreated()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("pepe"));

        List<AccountHolder> accountHolderList = accountHolderRepository.findAll();
        assertEquals(1, accountHolderList.size());
    }
    @Test
    void create_validDTOWithTwoAddresses_newAccountHolder() throws Exception {
        AddressDTO address1DTO = new AddressDTO("C/ blabla", 28345, "Coslada", "Madrid");
        AddressDTO address2DTO = new AddressDTO("c/ chorlito", 45354, "Chinchilla de Monte-Aragón", "Albacete");
        AccountHolderDTO accountHolderDTO = new AccountHolderDTO("José Navarro", "pepe56", "12345", LocalDate.of(1956,4,2), address1DTO, address2DTO);
        String body = objectMapper.writeValueAsString(accountHolderDTO);
        MvcResult result = mockMvc.perform(
                post("/admin/account-holder")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("admin")
                                .password("password")
                                .roles("ADMIN"))
        ).andExpect(status().isCreated()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("pepe"));

        List<AccountHolder> accountHolderList = accountHolderRepository.findAll();
        assertEquals(1, accountHolderList.size());
    }
    @Test
    void create_validDTOBUTNoAdminLogged_Exception() throws Exception {
        AddressDTO address1DTO = new AddressDTO("C/ blabla", 28345, "Coslada", "Madrid");
        AccountHolderDTO accountHolderDTO = new AccountHolderDTO("José Navarro", "pepe56", "12345", LocalDate.of(1956,4,2), address1DTO);
        String body = objectMapper.writeValueAsString(accountHolderDTO);
        MvcResult result = mockMvc.perform(
                post("/admin/account-holder")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("account_holder")
                                .password("password")
                                .roles("ACCOUNT_HOLDER"))
        ).andExpect(status().isForbidden()).andReturn();
    }
    @Test
    void create_validDTOBUTNoUserLogged_Exception() throws Exception {
        AddressDTO address1DTO = new AddressDTO("C/ blabla", 28345, "Coslada", "Madrid");
        AccountHolderDTO accountHolderDTO = new AccountHolderDTO("José Navarro", "pepe56", "12345", LocalDate.of(1956,4,2), address1DTO);
        String body = objectMapper.writeValueAsString(accountHolderDTO);
        MvcResult result = mockMvc.perform(
                post("/admin/account-holder")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized()).andReturn();
    }
}