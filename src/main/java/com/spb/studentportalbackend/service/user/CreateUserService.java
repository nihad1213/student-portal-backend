package com.spb.studentportalbackend.service.user;

import com.spb.studentportalbackend.common.RoleEnum;
import com.spb.studentportalbackend.dto.user.request.CreateUserRequest;
import com.spb.studentportalbackend.dto.user.response.CreateUserResponse;
import com.spb.studentportalbackend.entity.User;
import com.spb.studentportalbackend.repository.UserRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CreateUserService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public CreateUserResponse create(CreateUserRequest request) {

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

        RoleEnum role;
        try {
            role = RoleEnum.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role: " + request.getRole());
        }
        user.setRole(role);

        User saved = userRepository.save(user);

        return new CreateUserResponse(saved.getId(), saved.getUsername(), saved.getMail(), saved.getRole());

    }
}
