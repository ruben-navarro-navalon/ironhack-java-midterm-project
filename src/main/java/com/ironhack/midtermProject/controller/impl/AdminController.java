package com.ironhack.midtermProject.controller.impl;

import com.ironhack.midtermProject.controller.dto.AdminDTO;
import com.ironhack.midtermProject.controller.interfaces.IAdminController;
import com.ironhack.midtermProject.model.Admin;
import com.ironhack.midtermProject.service.interfaces.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AdminController implements IAdminController {
    @Autowired
    IAdminService adminService;

    // Create new admins. Only admins can do this:
    @PostMapping("/admin/new")
    @ResponseStatus(HttpStatus.CREATED)
    public Admin create(@RequestBody @Valid AdminDTO adminDTO) {
        return adminService.create(adminDTO);
    }
}
