package com.spb.studentportalbackend.dto.course.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReadCourseClassRequest {
    String className;
    String classCode;
    Long teacherId;
}
