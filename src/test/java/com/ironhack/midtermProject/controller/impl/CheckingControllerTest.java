package com.ironhack.midtermProject.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midtermProject.classes.Address;
import com.ironhack.midtermProject.controller.dto.CheckingDTO;
import com.ironhack.midtermProject.enums.State;
import com.ironhack.midtermProject.enums.UserRole;
import com.ironhack.midtermProject.model.AccountHolder;
import com.ironhack.midtermProject.model.Checking;
import com.ironhack.midtermProject.model.Role;
import com.ironhack.midtermProject.model.StudentChecking;
import com.ironhack.midtermProject.repository.AccountHolderRepository;
import com.ironhack.midtermProject.repository.CheckingRepository;
import com.ironhack.midtermProject.repository.RoleRepository;
import com.ironhack.midtermProject.repository.StudentCheckingRepository;
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
class CheckingControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private CheckingRepository checkingRepository;
    @Autowired
    private StudentCheckingRepository studentCheckingRepository;
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
        checkingRepository.deleteAll();
        studentCheckingRepository.deleteAll();
        accountHolderRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void create_validDTOWithSeniorPrimaryOwner_newChecking() throws Exception {
        Long primaryOwnerID = accountHolderRepository.findAll().get(0).getId();
        CheckingDTO checkingDTO = new CheckingDTO(new BigDecimal("20000"), primaryOwnerID, "pepinillos");
        String body = objectMapper.writeValueAsString(checkingDTO);
        System.out.println(body);
        MvcResult result = mockMvc.perform(
                post("/admin/checking")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("admin")
                                .password("password")
                                .roles("ADMIN"))
        ).andExpect(status().isCreated()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("Juan"));

        List<Checking> checkingList = checkingRepository.findAll();
        assertEquals(1, checkingList.size());
    }
    @Test
    void create_validDTOWithYoungPrimaryOwner_newStudentChecking() throws Exception {
        Long primaryOwnerID = accountHolderRepository.findAll().get(1).getId();
        CheckingDTO checkingDTO = new CheckingDTO(new BigDecimal("20000"), primaryOwnerID, "pepinillos");
        String body = objectMapper.writeValueAsString(checkingDTO);
        System.out.println(body);
        MvcResult result = mockMvc.perform(
                post("/admin/checking")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("admin")
                                .password("password")
                                .roles("ADMIN"))
        ).andExpect(status().isCreated()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("Arturo"));

        List<StudentChecking> studentCheckingList = studentCheckingRepository.findAll();
        assertEquals(1, studentCheckingList.size());
    }
    @Test
    void create_validDTOWithMoreThanOneOwner_newChecking() throws Exception {
        Long primaryOwnerID = accountHolderRepository.findAll().get(0).getId();
        Long secondaryOwnerID = accountHolderRepository.findAll().get(1).getId();
        CheckingDTO checkingDTO = new CheckingDTO(new BigDecimal("20000"), primaryOwnerID, secondaryOwnerID, "pepinillos");
        String body = objectMapper.writeValueAsString(checkingDTO);
        System.out.println(body);
        MvcResult result = mockMvc.perform(
                post("/admin/checking")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("admin")
                                .password("password")
                                .roles("ADMIN"))
        ).andExpect(status().isCreated()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("Arturo"));

        List<Checking> checkingList = checkingRepository.findAll();
        assertEquals(1, checkingList.size());
    }
    @Test
    void create_validDTOButNoAdminLogged_exception() throws Exception {
        Long primaryOwnerID = accountHolderRepository.findAll().get(0).getId();
        CheckingDTO checkingDTO = new CheckingDTO(new BigDecimal("20000"), primaryOwnerID, "pepinillos");
        String body = objectMapper.writeValueAsString(checkingDTO);
        System.out.println(body);
        MvcResult result = mockMvc.perform(
                post("/admin/checking")
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
        CheckingDTO checkingDTO = new CheckingDTO(new BigDecimal("20000"), primaryOwnerID, "pepinillos");
        String body = objectMapper.writeValueAsString(checkingDTO);
        System.out.println(body);
        MvcResult result = mockMvc.perform(
                post("/admin/checking")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized()).andReturn();
    }
}