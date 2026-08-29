package com.spb.studentportalbackend.service.auth;

import com.spb.studentportalbackend.dto.auth.request.LoginRequest;
import com.spb.studentportalbackend.dto.auth.response.LoginResponse;
import com.spb.studentportalbackend.entity.User;
import com.spb.studentportalbackend.security.JwtService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LoginService {

    AuthenticationManager authenticationManager;
    JwtService jwtService;

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        String token = jwtService.generateToken(user);

        return new LoginResponse(token);
    }
}
