package com.spb.studentportalbackend.controller.v1;

import com.spb.studentportalbackend.dto.user.request.CreateUserRequest;
import com.spb.studentportalbackend.dto.user.request.UpdateUserRequest;
import com.spb.studentportalbackend.dto.user.response.CreateUserResponse;
import com.spb.studentportalbackend.dto.user.response.UpdateUserResponse;
import com.spb.studentportalbackend.service.user.CreateUserService;
import com.spb.studentportalbackend.service.user.UpdateUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    CreateUserService  createUserService;
    UpdateUserService updateUserService;

    @PostMapping("/create")
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody CreateUserRequest createUserRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createUserService.create(createUserRequest));
    }

    @PatchMapping("/update")
    public ResponseEntity<UpdateUserResponse> updateUser(@RequestBody UpdateUserRequest updateUserRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(updateUserService.update(updateUserRequest));
    }
}
