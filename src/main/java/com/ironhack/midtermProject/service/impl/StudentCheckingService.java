package com.ironhack.midtermProject.service.impl;

import com.ironhack.midtermProject.model.StudentChecking;
import com.ironhack.midtermProject.repository.StudentCheckingRepository;
import com.ironhack.midtermProject.service.interfaces.IStudentCheckingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class StudentCheckingService implements IStudentCheckingService {
    @Autowired
    StudentCheckingRepository studentCheckingRepository;

    // Show all Student Checking accounts in repository:
    public List<StudentChecking> showAll() {
        List<StudentChecking> studentCheckingList = studentCheckingRepository.findAll();
        // If list size is 0, there is no account to show.
        if (studentCheckingList.size() == 0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no Student Checking Account to show.");
        } else {
            return studentCheckingList;
        }
    }

    // Show specific Student Checking account, by id:
    public Optional<StudentChecking> find(Long id) {
        Optional<StudentChecking> studentChecking = studentCheckingRepository.findById(id);
        if (studentChecking.isPresent()){
            return studentChecking;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no Student Checking Account with ID: " + id);
        }
    }
}
