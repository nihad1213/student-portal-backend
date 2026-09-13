package com.spb.studentportalbackend.controller.v1;

import com.spb.studentportalbackend.dto.common.request.DeleteRecordRequest;
import com.spb.studentportalbackend.dto.common.response.DeleteRecordResponse;
import com.spb.studentportalbackend.dto.user.request.CreateUserRequest;
import com.spb.studentportalbackend.dto.user.request.ReadUserRequest;
import com.spb.studentportalbackend.dto.user.request.UpdateUserRequest;
import com.spb.studentportalbackend.dto.user.response.CreateUserResponse;
import com.spb.studentportalbackend.dto.user.response.ReadUserResponse;
import com.spb.studentportalbackend.dto.user.response.UpdateUserResponse;
import com.spb.studentportalbackend.service.user.CreateUserService;
import com.spb.studentportalbackend.service.user.DeleteUserService;
import com.spb.studentportalbackend.service.user.ReadUserService;
import com.spb.studentportalbackend.service.user.UpdateUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    CreateUserService  createUserService;
    UpdateUserService updateUserService;
    DeleteUserService deleteUserService;
    ReadUserService readUserService;

    @PostMapping("/create")
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody CreateUserRequest createUserRequest) {
        log.info("POST /user/create username={}", createUserRequest.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(createUserService.create(createUserRequest));
    }

    @GetMapping("/read")
    public ResponseEntity<List<ReadUserResponse>> readUsers() {
        log.debug("GET /user/read");
        return ResponseEntity.status(HttpStatus.OK).body(readUserService.readAll());
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<ReadUserResponse> readUser(@PathVariable Long id) {
        log.debug("GET /user/read/{}", id);
        return ResponseEntity.status(HttpStatus.OK).body(readUserService.readById(id));
    }

    @PostMapping("/search")
    public ResponseEntity<List<ReadUserResponse>> searchUsers(@RequestBody ReadUserRequest readUserRequest) {
        log.debug("POST /user/search criteria={}", readUserRequest);
        return ResponseEntity.status(HttpStatus.OK).body(readUserService.search(readUserRequest));
    }

    @PatchMapping("/update")
    public ResponseEntity<UpdateUserResponse> updateUser(@RequestBody UpdateUserRequest updateUserRequest) {
        log.info("PATCH /user/update id={}", updateUserRequest.getId());
        return ResponseEntity.status(HttpStatus.OK).body(updateUserService.update(updateUserRequest));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<DeleteRecordResponse> deleteUser(@RequestBody DeleteRecordRequest deleteUserRequest) {
        log.info("DELETE /user/delete id={}", deleteUserRequest.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(deleteUserService.delete(deleteUserRequest));
    }
}
