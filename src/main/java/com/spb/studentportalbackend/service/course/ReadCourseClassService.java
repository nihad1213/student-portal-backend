package com.spb.studentportalbackend.service.course;

import com.spb.studentportalbackend.dto.course.request.AcademicTermDto;
import com.spb.studentportalbackend.dto.course.request.ReadCourseClassRequest;
import com.spb.studentportalbackend.dto.course.response.ReadCourseClassResponse;
import com.spb.studentportalbackend.entity.CourseClass;
import com.spb.studentportalbackend.repository.CourseClassRepository;
import com.spb.studentportalbackend.repository.CourseClassSpecification;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ReadCourseClassService {

    CourseClassRepository courseClassRepository;

    public List<ReadCourseClassResponse> readAll() {
        List<ReadCourseClassResponse> classes = courseClassRepository.findAll(CourseClassSpecification.fromRequest(null))
                .stream()
                .map(this::toResponse)
                .toList();
        log.debug("Read all classes count={}", classes.size());
        return classes;
    }

    public ReadCourseClassResponse readById(Long id) {
        CourseClass courseClass = courseClassRepository.findById(id)
                .filter(c -> c.getDeletedAt() == null)
                .orElseThrow(() -> {
                    log.warn("Read class rejected: not found id={}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Class not found with id: " + id);
                });
        return toResponse(courseClass);
    }

    public List<ReadCourseClassResponse> search(ReadCourseClassRequest request) {
        List<ReadCourseClassResponse> classes = courseClassRepository.findAll(CourseClassSpecification.fromRequest(request))
                .stream()
                .map(this::toResponse)
                .toList();
        log.debug("Search classes criteria={} count={}", request, classes.size());
        return classes;
    }

    private ReadCourseClassResponse toResponse(CourseClass courseClass) {
        String teacherFullName = courseClass.getTeacher() != null
                ? courseClass.getTeacher().getFirstName() + " " + courseClass.getTeacher().getLastName()
                : "Unassigned";

        Long teacherId = courseClass.getTeacher() != null ? courseClass.getTeacher().getId() : null;

        List<AcademicTermDto> targetTerms = courseClass.getTargetTerms() != null
                ? courseClass.getTargetTerms().stream()
                .map(term -> new AcademicTermDto(term.getTargetYear(), term.getTargetSemester()))
                .toList()
                : List.of();

        return new ReadCourseClassResponse(
                courseClass.getId(),
                courseClass.getClassName(),
                courseClass.getClassCode(),
                courseClass.getMaxCapacity(),
                courseClass.getEnrolledCount(),
                teacherId,
                teacherFullName,
                courseClass.isFull(),
                targetTerms
        );
    }
}