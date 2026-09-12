package com.spb.studentportalbackend.service.user;

import com.spb.studentportalbackend.dto.common.request.DeleteRecordRequest;
import com.spb.studentportalbackend.dto.common.response.DeleteRecordResponse;
import com.spb.studentportalbackend.entity.User;
import com.spb.studentportalbackend.repository.UserRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DeleteUserService {

    UserRepository userRepository;

    public DeleteRecordResponse delete(DeleteRecordRequest request) {

        User user = userRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getId()));

        if (user.getDeletedAt() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already deleted");
        }

        user.setDeletedAt(LocalDateTime.now());
        user.setEnabled(false);
        userRepository.save(user);

        return new DeleteRecordResponse(request.getId(), "Record deleted successfully!");
    }
}
