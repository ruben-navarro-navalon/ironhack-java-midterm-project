package com.ironhack.midtermProject.controller.interfaces;

import com.ironhack.midtermProject.controller.dto.AdminDTO;
import com.ironhack.midtermProject.model.Admin;

public interface IAdminController {

    // The methods are described in the controller implementation.

    public Admin create(AdminDTO adminDTO);
}
