package com.spb.studentportalbackend.service.enrollment;

import com.spb.studentportalbackend.dto.enrollment.response.EnrollResponse;
import com.spb.studentportalbackend.entity.AcademicTerm;
import com.spb.studentportalbackend.entity.CourseClass;
import com.spb.studentportalbackend.entity.User;
import com.spb.studentportalbackend.repository.CourseClassRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnrollmentPersistenceService {

    private final CourseClassRepository courseClassRepository;

    // Separate bean from EnrollStudentService so @Transactional goes through the Spring proxy.
    // If anything below throws, the transaction rolls back (insert and count increment together).
    @Transactional
    public EnrollResponse persist(Long classId, User student) {

        CourseClass courseClass = courseClassRepository.findById(classId)
                .filter(c -> c.getDeletedAt() == null)
                .orElseThrow(() -> {
                    log.warn("Enroll rejected: class not found classId={}", classId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Class not found");
                });

        // Empty targetTerms means the class is open to every year/semester
        if (!courseClass.getTargetTerms().isEmpty()) {
            AcademicTerm studentTerm = new AcademicTerm(student.getCurrentYear(), student.getCurrentSemester());
            if (!courseClass.getTargetTerms().contains(studentTerm)) {
                log.warn("Enroll rejected: term mismatch classId={} studentId={} year={} semester={}",
                        classId, student.getId(), student.getCurrentYear(), student.getCurrentSemester());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "This class is not available for your year/semester");
            }
        }

        if (courseClassRepository.isStudentEnrolled(classId, student.getId())) {
            log.warn("Enroll rejected: already enrolled classId={} studentId={}", classId, student.getId());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already enrolled in this class");
        }

        // Atomic at the DB level: returns 0 rows if the class is full or was soft-deleted meanwhile
        if (courseClassRepository.incrementEnrolledCount(classId) == 0) {
            log.warn("Enroll rejected: class full in DB classId={}", classId);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Class is full");
        }

        courseClassRepository.addStudent(classId, student.getId());

        log.info("Student enrolled classId={} studentId={}", classId, student.getId());

        return new EnrollResponse(
                courseClass.getId(),
                courseClass.getClassName(),
                courseClass.getClassCode(),
                student.getId(),
                "Enrolled successfully"
        );
    }
}