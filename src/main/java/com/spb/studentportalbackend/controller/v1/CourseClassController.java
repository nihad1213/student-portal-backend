package com.spb.studentportalbackend.controller.v1;

import com.spb.studentportalbackend.dto.common.request.DeleteRecordRequest;
import com.spb.studentportalbackend.dto.common.response.DeleteRecordResponse;
import com.spb.studentportalbackend.dto.course.request.CreateCourseClassRequest;
import com.spb.studentportalbackend.dto.course.request.ReadCourseClassRequest;
import com.spb.studentportalbackend.dto.course.request.UpdateCourseClassRequest;
import com.spb.studentportalbackend.dto.course.response.CreateCourseClassResponse;
import com.spb.studentportalbackend.dto.course.response.ReadCourseClassResponse;
import com.spb.studentportalbackend.dto.course.response.UpdateCourseClassResponse;
import com.spb.studentportalbackend.service.course.CreateCourseClassService;
import com.spb.studentportalbackend.service.course.DeleteCourseClassService;
import com.spb.studentportalbackend.service.course.ReadCourseClassService;
import com.spb.studentportalbackend.service.course.UpdateCourseClassService;
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
@RequestMapping("/api/v1/course-class")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseClassController {

    CreateCourseClassService createCourseClassService;
    UpdateCourseClassService updateCourseClassService;
    DeleteCourseClassService deleteCourseClassService;
    ReadCourseClassService readCourseClassService;

    @PostMapping("/create")
    public ResponseEntity<CreateCourseClassResponse> createCourseClass(@RequestBody CreateCourseClassRequest createCourseClassRequest) {
        log.info("POST /course-class/create className={}", createCourseClassRequest.getClassName());
        return ResponseEntity.status(HttpStatus.CREATED).body(createCourseClassService.create(createCourseClassRequest));
    }

    @GetMapping("/read")
    public ResponseEntity<List<ReadCourseClassResponse>> readCourseClasses() {
        log.debug("GET /course-class/read");
        return ResponseEntity.status(HttpStatus.OK).body(readCourseClassService.readAll());
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<ReadCourseClassResponse> readCourseClass(@PathVariable Long id) {
        log.debug("GET /course-class/read/{}", id);
        return ResponseEntity.status(HttpStatus.OK).body(readCourseClassService.readById(id));
    }

    @PostMapping("/search")
    public ResponseEntity<List<ReadCourseClassResponse>> searchCourseClasses(@RequestBody ReadCourseClassRequest readCourseClassRequest) {
        log.debug("POST /course-class/search criteria={}", readCourseClassRequest);
        return ResponseEntity.status(HttpStatus.OK).body(readCourseClassService.search(readCourseClassRequest));
    }

    @PatchMapping("/update")
    public ResponseEntity<UpdateCourseClassResponse> updateCourseClass(@RequestBody UpdateCourseClassRequest updateCourseClassRequest) {
        log.info("PATCH /course-class/update id={}", updateCourseClassRequest.getId());
        return ResponseEntity.status(HttpStatus.OK).body(updateCourseClassService.update(updateCourseClassRequest));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<DeleteRecordResponse> deleteCourseClass(@RequestBody DeleteRecordRequest deleteRecordRequest) {
        log.info("DELETE /course-class/delete id={}", deleteRecordRequest.getId());
        return ResponseEntity.status(HttpStatus.OK).body(deleteCourseClassService.delete(deleteRecordRequest));
    }
}