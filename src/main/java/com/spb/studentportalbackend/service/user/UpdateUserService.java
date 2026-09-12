package com.spb.studentportalbackend.service.user;

import com.spb.studentportalbackend.common.RoleEnum;
import com.spb.studentportalbackend.dto.user.request.UpdateUserRequest;
import com.spb.studentportalbackend.dto.user.response.UpdateUserResponse;
import com.spb.studentportalbackend.entity.User;
import com.spb.studentportalbackend.repository.UserRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UpdateUserService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public UpdateUserResponse update(UpdateUserRequest  request) {
        User user = userRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getId()));;

        if (userRepository.existsByUsernameAndIdNot(request.getUsername(), request.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already taken");
        }

        if (userRepository.existsByMailAndIdNot(request.getMail(), request.getId())) {
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role: " + request.getRole());
        }
        user.setRole(role);

        User saved = userRepository.save(user);

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
