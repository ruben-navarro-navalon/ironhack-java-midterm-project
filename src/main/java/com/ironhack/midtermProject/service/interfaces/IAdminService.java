package com.ironhack.midtermProject.service.interfaces;

import com.ironhack.midtermProject.controller.dto.AdminDTO;
import com.ironhack.midtermProject.model.Admin;

public interface IAdminService {

    // The methods are described in the service implementation.

    public Admin create(AdminDTO adminDTO);
}
