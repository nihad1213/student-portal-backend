package com.spb.studentportalbackend.service.user;

import com.spb.studentportalbackend.dto.user.request.ReadUserRequest;
import com.spb.studentportalbackend.dto.user.response.ReadUserResponse;
import com.spb.studentportalbackend.entity.User;
import com.spb.studentportalbackend.repository.UserRepository;
import com.spb.studentportalbackend.repository.UserSpecification;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ReadUserService {

    UserRepository userRepository;

    public List<ReadUserResponse> readAll() {
        List<ReadUserResponse> users = userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
        log.debug("Read all users count={}", users.size());
        return users;
    }

    public ReadUserResponse readById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Read user rejected: not found id={}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id);
                });
        return toResponse(user);
    }

    public List<ReadUserResponse> search(ReadUserRequest request) {
        List<ReadUserResponse> users = userRepository.findAll(UserSpecification.fromRequest(request))
                .stream()
                .map(this::toResponse)
                .toList();
        log.debug("Search users criteria={} count={}", request, users.size());
        return users;
    }

    private ReadUserResponse toResponse(User user) {
        return new ReadUserResponse(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getMail(),
                user.getRole(),
                user.getDeletedAt() == null
        );
    }
}
