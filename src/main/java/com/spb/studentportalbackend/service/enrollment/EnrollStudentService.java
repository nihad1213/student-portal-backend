package com.spb.studentportalbackend.service.enrollment;

import com.spb.studentportalbackend.common.RoleEnum;
import com.spb.studentportalbackend.dto.enrollment.response.EnrollResponse;
import com.spb.studentportalbackend.entity.User;
import com.spb.studentportalbackend.repository.UserRepository;
import com.spb.studentportalbackend.service.enrollment.ClassSeatRedisService.ReserveResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnrollStudentService {

    private final UserRepository userRepository;
    private final ClassSeatRedisService seatService;
    private final EnrollmentPersistenceService persistenceService;

    public EnrollResponse enroll(Long classId, Long studentId) {

        User student = userRepository.findById(studentId)
                .filter(u -> u.getDeletedAt() == null)
                .orElseThrow(() -> {
                    log.warn("Enroll rejected: student not found studentId={}", studentId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");
                });

        if (student.getRole() != RoleEnum.STUDENT) {
            log.warn("Enroll rejected: user is not a student userId={} role={}", studentId, student.getRole());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only students can enroll");
        }

        if (student.isFrozen() || student.isGraduated() || !student.isEnabled()) {
            log.warn("Enroll rejected: student not eligible studentId={}", studentId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Student is not eligible to enroll");
        }

        ReserveResult result = seatService.reserve(classId, studentId);
        if (result == ReserveResult.NOT_INITIALIZED) {
            seatService.warmUp(classId);
            result = seatService.reserve(classId, studentId);
        }

        switch (result) {
            case ALREADY_ENROLLED -> {
                log.warn("Enroll rejected: already enrolled classId={} studentId={}", classId, studentId);
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Already enrolled in this class");
            }
            case FULL -> {
                log.warn("Enroll rejected: class full classId={} studentId={}", classId, studentId);
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Class is full");
            }
            case NOT_INITIALIZED ->
                    throw new IllegalStateException("Seat cache could not be initialized for classId=" + classId);
            case RESERVED -> { /* seat held, continue to the DB */ }
        }

        try {
            return persistenceService.persist(classId, student);
        } catch (DataIntegrityViolationException e) {
            seatService.release(classId, studentId);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already enrolled in this class");
        } catch (RuntimeException e) {
            seatService.release(classId, studentId);
            throw e;
        }
    }
}