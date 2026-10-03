package com.spb.studentportalbackend.dto.course.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCourseClassRequest {
    @NotNull(message = "Class ID is required")
    Long id;

    @NotBlank(message = "Class name is required")
    String className;

    @NotBlank(message = "Class code is required")
    String classCode;

    @NotNull(message = "Max capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    Integer maxCapacity;

    @NotNull(message = "Teacher ID is required")
    Long teacherId;

    @NotEmpty
    List<AcademicTermDto> targetTerms;
}
