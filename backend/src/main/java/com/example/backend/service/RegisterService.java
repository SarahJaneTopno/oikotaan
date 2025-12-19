package com.example.backend.service;

import com.example.backend.model.User;
import com.example.backend.repository.RegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RegisterService {

    @Autowired
    private RegisterRepository registerRepository;

    @Autowired
    private EmailService emailService;

    public String register(User user) throws Exception {
        System.out.println(user.getName());
        validateUser(user);

        String existingRegId = registerRepository.findRegIdByEmail(user.getEmail());
        if (existingRegId != null) {
            registerRepository.updateUser(user, existingRegId);
            return "Updated successfully with existing Reg ID: " + existingRegId;
        }
        else {
            String regId = generateRegId();
            LocalDateTime timestamp = LocalDateTime.now();
            registerRepository.saveUser(user, regId, timestamp);

            emailService.sendRegistrationEmail(user.getEmail(), regId, user.getName());

            return "Registered successfully with new Reg ID: " + regId;
        }
    }

    private void validateUser(User user) throws Exception {
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new Exception("Email invalid/not entered");
        }
        if (user.getType() == null || user.getType().isEmpty()) {
            throw new Exception("You need to specify whether you are a band or an individual");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            throw new Exception("Band/Individual Name not entered");
        }
        if (user.getPhoneNumber() == null || !user.getPhoneNumber().matches("\\d{10}")) {
            throw new Exception("Phone number invalid/not entered");
        }
        if (user.getCity() == null || user.getCity().isEmpty()) {
            throw new Exception("City not entered");
        }
        if (user.getDetails() == null || user.getDetails().isEmpty()) {
            throw new Exception("Instrument details/Band member details not entered");
        }
        if (user.getAccommodationRequired() == null || (!user.getAccommodationRequired().equalsIgnoreCase("yes")
                && !user.getAccommodationRequired().equalsIgnoreCase("no"))) {
            throw new Exception("You need to specify whether you need accommodation");
        }
        if (user.getDriveLink() == null || user.getDriveLink().isEmpty()) {
            throw new Exception("Drive link not provided");
        }
    }


    private String generateRegId() {
        int lastId = registerRepository.getRegId();
        return String.format("2026OKTN8%03d", lastId + 1);
    }
}
