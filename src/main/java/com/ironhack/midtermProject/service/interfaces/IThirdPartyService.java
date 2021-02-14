package com.ironhack.midtermProject.service.interfaces;

import com.ironhack.midtermProject.controller.dto.ThirdPartyDTO;
import com.ironhack.midtermProject.model.ThirdParty;

public interface IThirdPartyService {

    // The methods are described in the service implementation.

    public ThirdParty create(ThirdPartyDTO thirdPartyDTO);
}
