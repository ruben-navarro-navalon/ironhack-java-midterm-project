package com.ironhack.midtermProject.controller.interfaces;

import com.ironhack.midtermProject.model.StudentChecking;

import java.util.List;
import java.util.Optional;

public interface IStudentCheckingController {

    // The methods are described in the controller implementation.

    public List<StudentChecking> showAll();
    public Optional<StudentChecking> find(Long id);
}
