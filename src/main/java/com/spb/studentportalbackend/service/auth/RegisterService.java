package com.spb.studentportalbackend.service.auth;

import com.spb.studentportalbackend.common.RoleEnum;
import com.spb.studentportalbackend.dto.auth.request.RegisterRequest;
import com.spb.studentportalbackend.dto.auth.response.RegisterResponse;
import com.spb.studentportalbackend.entity.User;
import com.spb.studentportalbackend.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegisterService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public RegisterResponse registerFirstAdmin(RegisterRequest request) {
        if (userRepository.existsByRole(RoleEnum.ADMIN)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "An admin already exists");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already taken");
        }
        if (userRepository.existsByMail(request.getMail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Mail already taken");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setMail(request.getMail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(RoleEnum.ADMIN);

        User saved = userRepository.save(user);

        return new RegisterResponse(saved.getId(), saved.getUsername(), saved.getMail(), saved.getRole());
    }
}
