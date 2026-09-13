package com.spb.studentportalbackend.service.user;

import com.spb.studentportalbackend.dto.common.request.DeleteRecordRequest;
import com.spb.studentportalbackend.dto.common.response.DeleteRecordResponse;
import com.spb.studentportalbackend.entity.User;
import com.spb.studentportalbackend.repository.UserRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DeleteUserService {

    UserRepository userRepository;

    public DeleteRecordResponse delete(DeleteRecordRequest request) {

        User user = userRepository.findById(request.getId())
                .orElseThrow(() -> {
                    log.warn("Delete user rejected: not found id={}", request.getId());
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + request.getId());
                });

        if (user.getDeletedAt() != null) {
            log.warn("Delete user rejected: already deleted id={}", request.getId());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already deleted");
        }

        user.setDeletedAt(LocalDateTime.now());
        user.setEnabled(false);
        userRepository.save(user);
        log.info("User deleted id={}", request.getId());

        return new DeleteRecordResponse(request.getId(), "Record deleted successfully!");
    }
}
