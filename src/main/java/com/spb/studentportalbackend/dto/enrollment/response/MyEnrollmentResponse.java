package com.spb.studentportalbackend.dto.enrollment.response;

import com.spb.studentportalbackend.dto.course.request.AcademicTermDto;

import java.util.List;

public record MyEnrollmentResponse(
        Long classId,
        String className,
        String classCode,
        Long teacherId,
        String teacherFullName,
        List<AcademicTermDto> targetTerms
) {}