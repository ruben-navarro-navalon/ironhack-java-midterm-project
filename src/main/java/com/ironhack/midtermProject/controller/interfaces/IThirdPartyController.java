package com.ironhack.midtermProject.controller.interfaces;

import com.ironhack.midtermProject.controller.dto.ThirdPartyDTO;
import com.ironhack.midtermProject.model.ThirdParty;

public interface IThirdPartyController {

    // The methods are described in the controller implementation.

    public ThirdParty create(ThirdPartyDTO thirdPartyDTO);
}
