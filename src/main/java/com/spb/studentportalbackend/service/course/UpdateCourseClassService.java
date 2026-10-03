package com.spb.studentportalbackend.service.course;

import com.spb.studentportalbackend.common.RoleEnum;
import com.spb.studentportalbackend.dto.course.request.AcademicTermDto;
import com.spb.studentportalbackend.dto.course.request.UpdateCourseClassRequest;
import com.spb.studentportalbackend.dto.course.response.UpdateCourseClassResponse;
import com.spb.studentportalbackend.entity.AcademicTerm;
import com.spb.studentportalbackend.entity.CourseClass;
import com.spb.studentportalbackend.entity.User;
import com.spb.studentportalbackend.repository.CourseClassRepository;
import com.spb.studentportalbackend.repository.UserRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UpdateCourseClassService {

    CourseClassRepository courseClassRepository;
    UserRepository userRepository;

    @Transactional
    public UpdateCourseClassResponse update(UpdateCourseClassRequest request) {

        CourseClass courseClass = courseClassRepository.findById(request.getId())
                .orElseThrow(() -> {
                    log.warn("Update class rejected: class not found id={}", request.getId());
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Class not found");
                });

        if (!courseClass.getClassCode().equals(request.getClassCode())
                && courseClassRepository.existsByClassCode(request.getClassCode())) {
            log.warn("Update class rejected: class code already taken classCode={}", request.getClassCode());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Class code already exists");
        }

        if (request.getMaxCapacity() < courseClass.getEnrolledCount()) {
            log.warn("Update class rejected: max capacity cannot be less than enrolled count maxCapacity={} enrolledCount={}",
                    request.getMaxCapacity(), courseClass.getEnrolledCount());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Max capacity cannot be less than current enrolled students count (" + courseClass.getEnrolledCount() + ")");
        }

        User teacher = userRepository.findById(request.getTeacherId())
                .orElseThrow(() -> {
                    log.warn("Update class rejected: teacher not found teacherId={}", request.getTeacherId());
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found");
                });

        if (teacher.getRole() != RoleEnum.TEACHER) {
            log.warn("Update class rejected: assigned user is not a teacher userId={} role={}", teacher.getId(), teacher.getRole());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Assigned user must have TEACHER role");
        }

        courseClass.setClassName(request.getClassName());
        courseClass.setClassCode(request.getClassCode());
        courseClass.setMaxCapacity(request.getMaxCapacity());
        courseClass.setTeacher(teacher);

        if (request.getTargetTerms() != null) {
            Set<AcademicTerm> updatedTerms = request.getTargetTerms().stream()
                    .map(dto -> new AcademicTerm(dto.getTargetYear(), dto.getTargetSemester()))
                    .collect(Collectors.toSet());

            courseClass.getTargetTerms().clear();
            courseClass.getTargetTerms().addAll(updatedTerms);
        }

        CourseClass updated = courseClassRepository.save(courseClass);
        log.info("Class updated id={} classCode={}", updated.getId(), updated.getClassCode());

        String teacherFullName = teacher.getFirstName() + " " + teacher.getLastName();

        List<AcademicTermDto> responseTerms = updated.getTargetTerms().stream()
                .map(term -> new AcademicTermDto(term.getTargetYear(), term.getTargetSemester()))
                .toList();

        return new UpdateCourseClassResponse(
                updated.getId(),
                updated.getClassName(),
                updated.getClassCode(),
                updated.getMaxCapacity(),
                updated.getEnrolledCount(),
                teacher.getId(),
                teacherFullName,
                responseTerms
        );
    }
}