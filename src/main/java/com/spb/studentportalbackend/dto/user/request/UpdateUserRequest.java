package com.spb.studentportalbackend.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserRequest {
    @NotNull
    Long id;

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
