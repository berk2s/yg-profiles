package com.yataygecisle.preference.profiles.web.controllers;

import com.yataygecisle.preference.profiles.services.StudentService;
import com.yataygecisle.preference.profiles.web.model.CreateStudentProfileDto;
import com.yataygecisle.preference.profiles.web.model.ProfileAnalyzeDto;
import com.yataygecisle.preference.profiles.web.model.StudentProfileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(StudentController.ENDPOINT)
@RestController
public class StudentController {

    public final static String ENDPOINT = "/student";

    private final StudentService studentService;

    @GetMapping(path = "/{studentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StudentProfileDto> getStudentProfile(@PathVariable UUID studentId) {
        return new ResponseEntity<>(studentService.getStudentProfile(studentId), HttpStatus.OK);
    }

    @PostMapping(path = "/{studentId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StudentProfileDto> saveStudentProfile(@PathVariable UUID studentId,
                                             @Valid @RequestBody CreateStudentProfileDto createStudentProfile) {
        return new ResponseEntity<>(studentService.saveStudentProfile(studentId, createStudentProfile),
                HttpStatus.CREATED);
    }

    @GetMapping(path = "/analyze/{studentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProfileAnalyzeDto> getProfileAnalyze(@PathVariable UUID studentId) {
        return new ResponseEntity<>(studentService.getStudentProfileAnalyze(studentId), HttpStatus.OK);
    }

}
