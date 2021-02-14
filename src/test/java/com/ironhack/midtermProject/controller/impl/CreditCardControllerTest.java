package com.ironhack.midtermProject.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midtermProject.classes.Address;
import com.ironhack.midtermProject.controller.dto.CreditCardDTO;
import com.ironhack.midtermProject.enums.State;
import com.ironhack.midtermProject.enums.UserRole;
import com.ironhack.midtermProject.model.AccountHolder;
import com.ironhack.midtermProject.model.CreditCard;
import com.ironhack.midtermProject.model.Role;
import com.ironhack.midtermProject.repository.AccountHolderRepository;
import com.ironhack.midtermProject.repository.CreditCardRepository;
import com.ironhack.midtermProject.repository.RoleRepository;
import com.ironhack.midtermProject.repository.SavingsRepository;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class CreditCardControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private AccountHolderRepository accountHolderRepository;
    @Autowired
    private RoleRepository roleRepository;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
        Address address1 = new Address("Av. Estrella de la Muerte", 27890, "Madrid", State.MADRID);
        Address address2 = new Address("Av. Naboo", 99991, "Santiago de Compostela", State.LA_CORUNA);
        AccountHolder owner1 = new AccountHolder("Juan Antonio Fernández", "juan_an_fe69", "patata", LocalDate.of(1969,7,21), address1, address2);
        AccountHolder owner2 = new AccountHolder("Arturo Fernández", "artu_fe06", "carambola", LocalDate.of(2006,12,3), address1);
        accountHolderRepository.saveAll(List.of(owner1, owner2));
        roleRepository.saveAll(List.of(new Role(UserRole.ACCOUNT_HOLDER, owner1), new Role(UserRole.ACCOUNT_HOLDER, owner2)));
    }

    @AfterEach
    void tearDown() {
        creditCardRepository.deleteAll();
        accountHolderRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void create_validDTO_newCreditCard() throws Exception {
        Long primaryOwnerID = accountHolderRepository.findAll().get(0).getId();
        Long secondaryOwnerID = accountHolderRepository.findAll().get(1).getId();
        CreditCardDTO creditCardDTO = new CreditCardDTO();
        creditCardDTO.setBalance(new BigDecimal("35000"));
        creditCardDTO.setPrimaryOwnerId(primaryOwnerID);
        creditCardDTO.setSecondaryOwnerId(secondaryOwnerID);
        String body = objectMapper.writeValueAsString(creditCardDTO);
        System.out.println("test: " + body);
        MvcResult result = mockMvc.perform(
                post("/admin/credit-card")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("admin")
                                .password("password")
                                .roles("ADMIN"))
        ).andExpect(status().isCreated()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("Juan"));

        List<CreditCard> creditCardList = creditCardRepository.findAll();
        assertEquals(1, creditCardList.size());
    }
    @Test
    void create_validDTOButNoAdminLogged_exception() throws Exception {
        Long primaryOwnerID = accountHolderRepository.findAll().get(0).getId();
        Long secondaryOwnerID = accountHolderRepository.findAll().get(1).getId();
        CreditCardDTO creditCardDTO = new CreditCardDTO();
        creditCardDTO.setBalance(new BigDecimal("35000"));
        creditCardDTO.setPrimaryOwnerId(primaryOwnerID);
        creditCardDTO.setSecondaryOwnerId(secondaryOwnerID);
        String body = objectMapper.writeValueAsString(creditCardDTO);
        System.out.println("test: " + body);
        MvcResult result = mockMvc.perform(
                post("/admin/credit-card")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("account_holder")
                                .password("password")
                                .roles("ACCOUNT_HOLDER"))
        ).andExpect(status().isForbidden()).andReturn();
    }
    @Test
    void create_validDTOButNoUserLogged_exception() throws Exception {
        Long primaryOwnerID = accountHolderRepository.findAll().get(0).getId();
        Long secondaryOwnerID = accountHolderRepository.findAll().get(1).getId();
        CreditCardDTO creditCardDTO = new CreditCardDTO();
        creditCardDTO.setBalance(new BigDecimal("35000"));
        creditCardDTO.setPrimaryOwnerId(primaryOwnerID);
        creditCardDTO.setSecondaryOwnerId(secondaryOwnerID);
        String body = objectMapper.writeValueAsString(creditCardDTO);
        System.out.println("test: " + body);
        MvcResult result = mockMvc.perform(
                post("/admin/credit-card")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized()).andReturn();
    }
}