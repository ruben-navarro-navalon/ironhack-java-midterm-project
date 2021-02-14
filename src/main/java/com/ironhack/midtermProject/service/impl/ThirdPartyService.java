package com.ironhack.midtermProject.service.impl;

import com.ironhack.midtermProject.controller.dto.ThirdPartyDTO;
import com.ironhack.midtermProject.model.ThirdParty;
import com.ironhack.midtermProject.repository.ThirdPartyRepository;
import com.ironhack.midtermProject.service.interfaces.IThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ThirdPartyService implements IThirdPartyService {
    @Autowired
    ThirdPartyRepository thirdPartyRepository;

    // Create new Third Party:
    public ThirdParty create(ThirdPartyDTO thirdPartyDTO) {
        ThirdParty thirdParty = new ThirdParty(thirdPartyDTO.getName(), thirdPartyDTO.getHashKey());
        return thirdPartyRepository.save(thirdParty);
    }
}
