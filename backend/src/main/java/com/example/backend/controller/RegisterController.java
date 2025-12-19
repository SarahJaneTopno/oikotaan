package com.example.backend.controller;

import com.example.backend.model.ApiResponse;
import com.example.backend.model.User;
import com.example.backend.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/register")
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @PostMapping
    public ResponseEntity<ApiResponse> registerUser(@RequestBody User user) {
        System.out.println(user.getName());
        try {
            String result = registerService.register(user);
            return new ResponseEntity<>(new ApiResponse(result, HttpStatus.OK), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<ApiResponse> testEndpoint() {
        return new ResponseEntity<>(new ApiResponse("Backend working fine", HttpStatus.OK), HttpStatus.OK);
    }
}
