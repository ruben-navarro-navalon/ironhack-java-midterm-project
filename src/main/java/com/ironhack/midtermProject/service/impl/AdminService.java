package com.ironhack.midtermProject.service.impl;

import com.ironhack.midtermProject.controller.dto.AdminDTO;
import com.ironhack.midtermProject.enums.UserRole;
import com.ironhack.midtermProject.model.Admin;
import com.ironhack.midtermProject.model.Role;
import com.ironhack.midtermProject.repository.AdminRepository;
import com.ironhack.midtermProject.repository.RoleRepository;
import com.ironhack.midtermProject.service.interfaces.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService implements IAdminService {
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    RoleRepository roleRepository;

    // Create new admin:
    public Admin create(AdminDTO adminDTO) {
        Admin admin = adminRepository.save(new Admin(adminDTO.getName(), adminDTO.getUsername(), adminDTO.getPassword()));
        Role role = roleRepository.save(new Role(UserRole.ADMIN, admin));
        return admin;
    }
}
