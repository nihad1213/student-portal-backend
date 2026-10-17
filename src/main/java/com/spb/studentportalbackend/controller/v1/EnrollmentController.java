package com.spb.studentportalbackend.controller.v1;

import com.spb.studentportalbackend.dto.enrollment.response.EnrollResponse;
import com.spb.studentportalbackend.entity.User;
import com.spb.studentportalbackend.service.enrollment.EnrollStudentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/enrollment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EnrollmentController {

    EnrollStudentService enrollStudentService;

    @PostMapping("/{classId}")
    public ResponseEntity<EnrollResponse> enroll(@PathVariable Long classId,
                                                 @AuthenticationPrincipal User student) {
        log.info("POST /enrollment/{} studentId={}", classId, student.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(enrollStudentService.enroll(classId, student.getId()));
    }
}