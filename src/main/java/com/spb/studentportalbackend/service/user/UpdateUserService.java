package com.spb.studentportalbackend.service.user;

import com.spb.studentportalbackend.common.RoleEnum;
import com.spb.studentportalbackend.dto.user.request.UpdateUserRequest;
import com.spb.studentportalbackend.dto.user.response.UpdateUserResponse;
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
public class UpdateUserService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public UpdateUserResponse update(UpdateUserRequest  request) {
        User user = userRepository.findById(request.getId())
                .orElseThrow(() -> {
                    log.warn("Update user rejected: not found id={}", request.getId());
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + request.getId());
                });

        if (userRepository.existsByUsernameAndIdNot(request.getUsername(), request.getId())) {
            log.warn("Update user rejected: username already taken username={}", request.getUsername());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already taken");
        }

        if (userRepository.existsByMailAndIdNot(request.getMail(), request.getId())) {
            log.warn("Update user rejected: mail already taken mail={}", request.getMail());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Mail already taken");
        }

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
            log.warn("Update user rejected: invalid role={}", request.getRole());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role: " + request.getRole());
        }
        user.setRole(role);

        User saved = userRepository.save(user);
        log.info("User updated id={}", saved.getId());

        return new UpdateUserResponse(
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
