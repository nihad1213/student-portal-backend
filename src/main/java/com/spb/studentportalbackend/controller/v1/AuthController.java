package com.spb.studentportalbackend.controller.v1;

import com.spb.studentportalbackend.dto.auth.request.LoginRequest;
import com.spb.studentportalbackend.dto.auth.request.RegisterRequest;
import com.spb.studentportalbackend.dto.auth.response.LoginResponse;
import com.spb.studentportalbackend.dto.auth.response.RegisterResponse;
import com.spb.studentportalbackend.service.auth.LoginService;
import com.spb.studentportalbackend.service.auth.RegisterService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    LoginService loginService;
    RegisterService registerService;

    // This is only used to add admin for the first time
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registerService.registerFirstAdmin(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(loginService.login(request));
    }

    @PostMapping("/logout")
    public String logout() {
        return "logout";
    }

}
