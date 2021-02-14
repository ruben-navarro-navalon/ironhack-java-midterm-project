package com.ironhack.midtermProject.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midtermProject.controller.dto.AdminDTO;
import com.ironhack.midtermProject.model.Admin;
import com.ironhack.midtermProject.repository.AdminRepository;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AdminControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private RoleRepository roleRepository;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @AfterEach
    void tearDown() {
        roleRepository.deleteAll();
        adminRepository.deleteAll();
    }

    @Test
    void create_validDTO_newAdmin() throws Exception {
        AdminDTO adminDTO = new AdminDTO("Juan González", "gonzo_epic23","asdfqwerzxcv");
        String body = objectMapper.writeValueAsString(adminDTO);
        System.out.println(body);
        MvcResult result = mockMvc.perform(
                post("/admin/new")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("admin")
                                .password("password")
                                .roles("ADMIN"))
        ).andExpect(status().isCreated()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("gonzo"));

        List<Admin> adminList = adminRepository.findAll();
        assertEquals(1,adminList.size());
    }
    @Test
    void create_validDTOButNoAdminLogged_exception() throws Exception {
        AdminDTO adminDTO = new AdminDTO("Juan González", "gonzo_epic23","asdfqwerzxcv");
        String body = objectMapper.writeValueAsString(adminDTO);
        System.out.println(body);
        MvcResult result = mockMvc.perform(
                post("/admin/new")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("account_holder")
                                .password("password")
                                .roles("ACCOUNT_HOLDER"))
        ).andExpect(status().isForbidden()).andReturn();
    }
    @Test
    void create_validDTOButNoUserLogged_exception() throws Exception {
        AdminDTO adminDTO = new AdminDTO("Juan González", "gonzo_epic23","asdfqwerzxcv");
        String body = objectMapper.writeValueAsString(adminDTO);
        System.out.println(body);
        MvcResult result = mockMvc.perform(
                post("/admin/new")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized()).andReturn();
    }
}