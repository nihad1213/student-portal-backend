package com.spb.studentportalbackend.dto.enrollment.response;

public record UnenrollResponse(
        Long classId,
        Long studentId,
        String message
) {}