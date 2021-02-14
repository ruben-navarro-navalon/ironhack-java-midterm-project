package com.ironhack.midtermProject.controller.impl;

import com.ironhack.midtermProject.controller.interfaces.IStudentCheckingController;
import com.ironhack.midtermProject.model.StudentChecking;
import com.ironhack.midtermProject.service.interfaces.IStudentCheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class StudentCheckingController implements IStudentCheckingController {
    @Autowired
    IStudentCheckingService studentCheckingService;

    // Get all data from all Student Checking accounts. Only admins can do this:
    @GetMapping("/admin/student-checking")
    @ResponseStatus(HttpStatus.OK)
    public List<StudentChecking> showAll() {
        return studentCheckingService.showAll();
    }

    // Get all data from specific Student Checking account, by id. Only admins can do this:
    @GetMapping("/admin/student-checking/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<StudentChecking> find(@PathVariable Long id) {
        return studentCheckingService.find(id);
    }
}
