package com.ironhack.midtermProject.service.interfaces;

import com.ironhack.midtermProject.model.StudentChecking;

import java.util.List;
import java.util.Optional;

public interface IStudentCheckingService {

    // The methods are described in the service implementation.

    public List<StudentChecking> showAll();
    public Optional<StudentChecking> find(Long id);
}
