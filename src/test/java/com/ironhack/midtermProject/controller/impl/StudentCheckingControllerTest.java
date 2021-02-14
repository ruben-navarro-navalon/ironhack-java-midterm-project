package com.ironhack.midtermProject.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midtermProject.classes.Address;
import com.ironhack.midtermProject.classes.Money;
import com.ironhack.midtermProject.enums.State;
import com.ironhack.midtermProject.enums.UserRole;
import com.ironhack.midtermProject.model.AccountHolder;
import com.ironhack.midtermProject.model.Role;
import com.ironhack.midtermProject.model.StudentChecking;
import com.ironhack.midtermProject.repository.AccountHolderRepository;
import com.ironhack.midtermProject.repository.RoleRepository;
import com.ironhack.midtermProject.repository.StudentCheckingRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class StudentCheckingControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
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
        AccountHolder owner1 = new AccountHolder("Juan Antonio Fernández", "juan_an_fe69", "patata", LocalDate.of(2000,7,21), address1, address2);
        AccountHolder owner2 = new AccountHolder("Arturo Fernández", "artu_fe06", "carambola", LocalDate.of(2006,12,3), address1);
        accountHolderRepository.saveAll(List.of(owner1, owner2));
        roleRepository.saveAll(List.of(new Role(UserRole.ACCOUNT_HOLDER, owner1), new Role(UserRole.ACCOUNT_HOLDER, owner2)));
        StudentChecking studentChecking1 = new StudentChecking(new Money(new BigDecimal("1200")), owner1, "potatimix");
        StudentChecking studentChecking2 = new StudentChecking(new Money(new BigDecimal("1320.23")), owner2, "carambolo");
        studentCheckingRepository.saveAll(List.of(studentChecking1, studentChecking2));
    }

    @AfterEach
    void tearDown() {
        studentCheckingRepository.deleteAll();
        accountHolderRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void showAll() throws Exception {
        MvcResult result = mockMvc.perform(get("/admin/student-checking")
                .with(user("cafla")
                        .password("risoto")
                        .roles("ADMIN")))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("Juan"));
        assertTrue(result.getResponse().getContentAsString().contains("Arturo"));
    }

    @Test
    void find_validId_rightStudentCheckingAccount() throws Exception {
        List<StudentChecking> studentCheckingList = studentCheckingRepository.findAll(); // To avoid problems with auto_increment.
        MvcResult result = mockMvc.perform(get("/admin/student-checking/"+studentCheckingList.get(0).getId())
                .with(user("cafla")
                        .password("risoto")
                        .roles("ADMIN")))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("Juan"));
        assertFalse(result.getResponse().getContentAsString().contains("Arturo"));
    }
    @Test
    void find_validIdButNoAdminLogged_exception() throws Exception {
        List<StudentChecking> studentCheckingList = studentCheckingRepository.findAll(); // To avoid problems with auto_increment.
        MvcResult result = mockMvc.perform(get("/admin/student-checking/"+studentCheckingList.get(0).getId())
                .with(user("cafla")
                        .password("risoto")
                        .roles("ACCOUNT_HOLDER")))
                .andExpect(status().isForbidden())
                .andReturn();
    }
    @Test
    void find_validIdButNoUserLogged_exception() throws Exception {
        List<StudentChecking> studentCheckingList = studentCheckingRepository.findAll(); // To avoid problems with auto_increment.
        MvcResult result = mockMvc.perform(get("/admin/student-checking/"+studentCheckingList.get(0).getId()))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }
    @Test
    void find_wrongId_exception() throws Exception {
        MvcResult result = mockMvc.perform(get("/admin/student-checking/0")
                .with(user("cafla")
                        .password("risoto")
                        .roles("ADMIN")))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}