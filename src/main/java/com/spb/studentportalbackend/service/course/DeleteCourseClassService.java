package com.spb.studentportalbackend.service.course;

import com.spb.studentportalbackend.dto.common.request.DeleteRecordRequest;
import com.spb.studentportalbackend.dto.common.response.DeleteRecordResponse;
import com.spb.studentportalbackend.entity.CourseClass;
import com.spb.studentportalbackend.repository.CourseClassRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DeleteCourseClassService {

    CourseClassRepository courseClassRepository;

    @Transactional
    public DeleteRecordResponse delete(DeleteRecordRequest request) {

        CourseClass courseClass = courseClassRepository.findById(request.getId())
                .orElseThrow(() -> {
                    log.warn("Delete class rejected: not found id={}", request.getId());
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Class not found with id: " + request.getId());
                });

        if (courseClass.getDeletedAt() != null) {
            log.warn("Delete class rejected: already deleted id={}", request.getId());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Class already deleted");
        }

        courseClass.setDeletedAt(LocalDateTime.now());
        courseClassRepository.save(courseClass);
        log.info("Class deleted id={}", request.getId());

        return new DeleteRecordResponse(request.getId(), "Record deleted successfully!");
    }
}