package com.spb.studentportalbackend.dto.enrollment.response;

public record EnrollResponse(
        Long classId,
        String className,
        String classCode,
        Long studentId,
        String message
) {}