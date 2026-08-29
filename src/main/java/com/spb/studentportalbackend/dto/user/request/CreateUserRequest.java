package com.spb.studentportalbackend.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserRequest {
    @NotBlank
    String username;

    @NotBlank
    String firstName;

    @NotBlank
    String lastName;

    @NotBlank
    String phoneNumber;

    @NotBlank
    @Email
    String mail;

    @NotBlank
    String password;

    @NotBlank
    String role;
}
