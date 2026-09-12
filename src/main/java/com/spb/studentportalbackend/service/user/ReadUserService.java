package com.spb.studentportalbackend.service.user;

import com.spb.studentportalbackend.dto.user.request.ReadUserRequest;
import com.spb.studentportalbackend.dto.user.response.ReadUserResponse;
import com.spb.studentportalbackend.entity.User;
import com.spb.studentportalbackend.repository.UserRepository;
import com.spb.studentportalbackend.repository.UserSpecification;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ReadUserService {

    UserRepository userRepository;

    public List<ReadUserResponse> read(ReadUserRequest request) {
        return userRepository.findAll(UserSpecification.fromRequest(request))
                .stream()
                .map(this::toResponse)
                .toList();
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
