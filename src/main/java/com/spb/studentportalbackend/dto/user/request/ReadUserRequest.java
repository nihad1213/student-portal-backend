package com.spb.studentportalbackend.dto.user.request;

import com.spb.studentportalbackend.common.RoleEnum;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReadUserRequest {
    String username;
    String firstName;
    String lastName;
    String phoneNumber;
    String mail;
    RoleEnum role;
    boolean activeOnly;
}
