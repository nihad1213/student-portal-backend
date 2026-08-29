package com.spb.studentportalbackend.dto.user.response;

import com.spb.studentportalbackend.common.RoleEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserResponse {
    Long id;
    String username;
    String mail;
    RoleEnum role;
}
