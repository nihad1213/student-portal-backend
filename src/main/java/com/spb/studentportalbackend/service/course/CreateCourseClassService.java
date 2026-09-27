package com.spb.studentportalbackend.service.course;

import com.spb.studentportalbackend.common.RoleEnum;
import com.spb.studentportalbackend.dto.course.request.CreateCourseClassRequest;
import com.spb.studentportalbackend.dto.course.response.CreateCourseClassResponse;
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
            log.warn("Create class is rejected: class code already exists classCode={}", request.getClassCode());
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

        CourseClass saved = courseClassRepository.save(courseClass);
        log.info("Class created id={} classCode={}", saved.getId(), saved.getClassCode());

        String teacherFullName = teacher.getFirstName() + " " + teacher.getLastName();

        return new CreateCourseClassResponse(
                saved.getId(),
                saved.getClassName(),
                saved.getClassCode(),
                saved.getMaxCapacity(),
                saved.getEnrolledCount(),
                teacher.getId(),
                teacherFullName
        );
    }
}

