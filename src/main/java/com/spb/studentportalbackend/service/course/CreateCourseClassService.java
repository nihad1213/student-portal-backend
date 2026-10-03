package com.spb.studentportalbackend.service.course;

import com.spb.studentportalbackend.common.RoleEnum;
import com.spb.studentportalbackend.dto.course.request.AcademicTermDto;
import com.spb.studentportalbackend.dto.course.request.CreateCourseClassRequest;
import com.spb.studentportalbackend.dto.course.response.CreateCourseClassResponse;
import com.spb.studentportalbackend.entity.AcademicTerm;
import com.spb.studentportalbackend.entity.CourseClass;
import com.spb.studentportalbackend.entity.User;
import com.spb.studentportalbackend.repository.CourseClassRepository;
import com.spb.studentportalbackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CreateCourseClassService {

    CourseClassRepository courseClassRepository;
    UserRepository userRepository;

    @Transactional
    public CreateCourseClassResponse create(CreateCourseClassRequest request) {
        if (courseClassRepository.existsByClassCode(request.getClassCode())) {
            log.warn("Create class rejected: class code already exists classCode={}", request.getClassCode());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Class code already exists");
        }

        User teacher = userRepository.findById(request.getTeacherId())
                .orElseThrow(() -> {
                    log.warn("Create class rejected: teacher not found teacherId={}", request.getTeacherId());
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found");
                });

        if (teacher.getRole() != RoleEnum.TEACHER) {
            log.warn("Create class rejected: assigned user is not a teacher userId={} role={}", teacher.getId(), teacher.getRole());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Assigned user must have TEACHER role");
        }

        CourseClass courseClass = new CourseClass();
        courseClass.setClassName(request.getClassName());
        courseClass.setClassCode(request.getClassCode());
        courseClass.setMaxCapacity(request.getCapacity());
        courseClass.setEnrolledCount(0);
        courseClass.setTeacher(teacher);

        if (request.getTargetTerms() != null && !request.getTargetTerms().isEmpty()) {
            Set<AcademicTerm> terms = request.getTargetTerms().stream()
                    .map(dto -> new AcademicTerm(dto.getTargetYear(), dto.getTargetSemester()))
                    .collect(Collectors.toSet());
            courseClass.setTargetTerms(terms);
        }

        CourseClass saved = courseClassRepository.save(courseClass);
        log.info("Class created id={} classCode={}", saved.getId(), saved.getClassCode());

        String teacherFullName = teacher.getFirstName() + " " + teacher.getLastName();

        List<AcademicTermDto> responseTerms = saved.getTargetTerms().stream()
                .map(term -> new AcademicTermDto(term.getTargetYear(), term.getTargetSemester()))
                .toList();

        return new CreateCourseClassResponse(
                saved.getId(),
                saved.getClassName(),
                saved.getClassCode(),
                saved.getMaxCapacity(),
                saved.getEnrolledCount(),
                teacher.getId(),
                teacherFullName,
                responseTerms
        );
    }
}