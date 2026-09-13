package com.spb.studentportalbackend.service.user;

import com.spb.studentportalbackend.common.RoleEnum;
import com.spb.studentportalbackend.dto.user.request.CreateUserRequest;
import com.spb.studentportalbackend.dto.user.response.CreateUserResponse;
import com.spb.studentportalbackend.entity.User;
import com.spb.studentportalbackend.repository.UserRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CreateUserService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public CreateUserResponse create(CreateUserRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            log.warn("Create user rejected: username already taken username={}", request.getUsername());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already taken");
        }
        if (userRepository.existsByMail(request.getMail())) {
            log.warn("Create user rejected: mail already taken mail={}", request.getMail());
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
        log.info("User created id={} username={}", saved.getId(), saved.getUsername());

        return new CreateUserResponse(
                saved.getId(),
                saved.getUsername(),
                saved.getFirstName(),
                saved.getLastName(),
                saved.getPhoneNumber(),
                saved.getMail(),
                saved.getRole()
        );
    }
}
