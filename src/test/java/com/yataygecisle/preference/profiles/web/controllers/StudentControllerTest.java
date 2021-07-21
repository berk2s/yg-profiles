package com.yataygecisle.preference.profiles.web.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yataygecisle.preference.profiles.domain.Course;
import com.yataygecisle.preference.profiles.domain.Student;
import com.yataygecisle.preference.profiles.web.model.CreateStudentProfileDto;
import com.yataygecisle.preference.profiles.web.model.ErrorDescription;
import com.yataygecisle.preference.profiles.web.model.ErrorType;
import com.yataygecisle.preference.profiles.web.model.InteractionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

public class StudentControllerTest extends IntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    String accessToken;
    String studentId;
    Student student;
    Course course;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        accessToken = createAccessToken();
        studentId = parseTokenToUserId(accessToken);

        HashMap map = createStudentProfile(UUID.fromString(studentId));

        student = (Student) map.get("student");
        course = (Course) map.get("course");
    }

    @DisplayName("Get Student Profile")
    @Test
    void getStudentProfile() throws Exception {

        mockMvc.perform(get(StudentController.ENDPOINT + "/" + student.getRemoteStudentId().toString())
                .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(student.getId().toString())))
                .andExpect(jsonPath("$.remoteStudentId", is(student.getRemoteStudentId().toString())))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.lastModifiedDate").isEmpty())
                .andExpect(jsonPath("$.likedCourses.length()", is(1)))
                .andExpect(jsonPath("$.likedCourses.[0].courseId", is(student.getLikedCourses().get(0).getId().getCourseId().toString())))
                .andExpect(jsonPath("$.likedCourses.[0].remoteCollegeId", is(student.getLikedCourses().get(0).getCourse().getRemoteCollegeId().toString())))
                .andExpect(jsonPath("$.likedCourses.[0].remoteFacultyId", is(student.getLikedCourses().get(0).getCourse().getRemoteFacultyId().toString())))
                .andExpect(jsonPath("$.likedCourses.[0].remoteCourseId", is(student.getLikedCourses().get(0).getCourse().getRemoteCourseId().toString())))
                .andExpect(jsonPath("$.likedCourses.[0].timesBasket", is(1)))
                .andExpect(jsonPath("$.likedCourses.[0].timesTotal", is(1)));

    }

    @DisplayName("Save Student Profile")
    @Test
    void saveStudentProfile() throws Exception {

        Course newCourse = createCourse();

        CreateStudentProfileDto createStudentProfile = new CreateStudentProfileDto();
        createStudentProfile.setRemoteCourseId(List.of(newCourse.getRemoteCourseId().toString()));
        createStudentProfile.setInteractionType(InteractionType.FROM_BASKET);

        mockMvc.perform(post(StudentController.ENDPOINT + "/" + student.getRemoteStudentId().toString())
                .header("Authorization", "Bearer " + accessToken)
                .content(objectMapper.writeValueAsString(createStudentProfile))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(student.getId().toString())))
                .andExpect(jsonPath("$.remoteStudentId", is(student.getRemoteStudentId().toString())))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.lastModifiedDate").isEmpty())
                .andExpect(jsonPath("$.likedCourses.length()", is(2)))
                .andExpect(jsonPath("$.likedCourses[*].courseId", anyOf(hasItem(newCourse.getId().toString()))))
                .andExpect(jsonPath("$.likedCourses[*].remoteCollegeId", anyOf(hasItem(newCourse.getRemoteCollegeId().toString()))))
                .andExpect(jsonPath("$.likedCourses[*].remoteFacultyId", anyOf(hasItem(newCourse.getRemoteFacultyId().toString()))))
                .andExpect(jsonPath("$.likedCourses[*].remoteCourseId", anyOf(hasItem(newCourse.getRemoteCourseId().toString()))))
                .andExpect(jsonPath("$.likedCourses[*].timesTotal", anyOf(hasItem(1))));

    }

    @DisplayName("Increase Student Profile Course")
    @Test
    void increaseStudentProfileCourse() throws Exception {

        CreateStudentProfileDto createStudentProfile = new CreateStudentProfileDto();
        createStudentProfile.setRemoteCourseId(List.of(course.getRemoteCourseId().toString()));
        createStudentProfile.setInteractionType(InteractionType.FROM_BASKET);

        mockMvc.perform(post(StudentController.ENDPOINT + "/" + student.getRemoteStudentId().toString())
                .header("Authorization", "Bearer " + accessToken)
                .content(objectMapper.writeValueAsString(createStudentProfile))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(student.getId().toString())))
                .andExpect(jsonPath("$.remoteStudentId", is(student.getRemoteStudentId().toString())))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.lastModifiedDate").isEmpty())
                .andExpect(jsonPath("$.likedCourses.length()", is(1)))
                .andExpect(jsonPath("$.likedCourses[*].courseId", anyOf(hasItem(course.getId().toString()))))
                .andExpect(jsonPath("$.likedCourses[*].remoteCollegeId", anyOf(hasItem(course.getRemoteCollegeId().toString()))))
                .andExpect(jsonPath("$.likedCourses[*].remoteFacultyId", anyOf(hasItem(course.getRemoteFacultyId().toString()))))
                .andExpect(jsonPath("$.likedCourses[*].remoteCourseId", anyOf(hasItem(course.getRemoteCourseId().toString()))))
                .andExpect(jsonPath("$.likedCourses[*].timesTotal", anyOf(hasItem(2))));

    }

    @DisplayName("Analyze Student Profile")
    @Test
    void analyzeStudentProfile() throws Exception {

        mockMvc.perform(get(StudentController.ENDPOINT + "/analyze/" + student.getRemoteStudentId().toString())
                .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId", is(student.getRemoteStudentId().toString())))
                .andExpect(jsonPath("$.possibilities[*].collegeId", anyOf(hasItem(course.getRemoteCollegeId().toString()))))
                .andExpect(jsonPath("$.possibilities[*].facultyId", anyOf(hasItem(course.getRemoteFacultyId().toString()))))
                .andExpect(jsonPath("$.possibilities[*].courseId", anyOf(hasItem(course.getRemoteCourseId().toString()))))
                .andExpect(jsonPath("$.possibilities[*].timesBasket", anyOf(hasItem(1))))
                .andExpect(jsonPath("$.possibilities[*].timesTotal", anyOf(hasItem(1))))
                .andExpect(jsonPath("$.possibilities[*].possibility", anyOf(hasItem(5.0))));

    }

    @DisplayName("Test Exceptions")
    @Nested
    class TestExceptions {

        @DisplayName("Get Student Profile Returns Not Found")
        @Test
        void getStudentProfileReturnsNotFound() throws Exception {

            mockMvc.perform(get(StudentController.ENDPOINT + "/" + UUID.randomUUID().toString())
                    .header("Authorization", "Bearer " + accessToken))
                    .andDo(print())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error", is(ErrorType.NOT_FOUND.getErr())))
                    .andExpect(jsonPath("$.error_description", is(ErrorDescription.STUDENT_PROFILE_NOT_FOUND.getErrorDesc())));

        }

        @DisplayName("Save Student Profile Returns Not Found")
        @Test
        void saveStudentProfileReturnsNotFound() throws Exception {


            CreateStudentProfileDto createStudentProfile = new CreateStudentProfileDto();
            createStudentProfile.setRemoteCourseId(List.of(UUID.randomUUID().toString()));
            createStudentProfile.setInteractionType(InteractionType.FROM_BASKET);

            mockMvc.perform(post(StudentController.ENDPOINT + "/" + UUID.randomUUID())
                    .header("Authorization", "Bearer " + accessToken)
                    .content(objectMapper.writeValueAsString(createStudentProfile))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error", is(ErrorType.NOT_FOUND.getErr())))
                    .andExpect(jsonPath("$.error_description", is(ErrorDescription.STUDENT_PROFILE_NOT_FOUND.getErrorDesc())));


        }

        @DisplayName("Save Student Profile Return Course Not Found")
        @Test
        void saveStudentProfileReturnsCourseNotFound() throws Exception {


            CreateStudentProfileDto createStudentProfile = new CreateStudentProfileDto();
            createStudentProfile.setRemoteCourseId(List.of(UUID.randomUUID().toString()));
            createStudentProfile.setInteractionType(InteractionType.FROM_BASKET);

            mockMvc.perform(post(StudentController.ENDPOINT + "/" + student.getRemoteStudentId().toString())
                    .header("Authorization", "Bearer " + accessToken)
                    .content(objectMapper.writeValueAsString(createStudentProfile))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error", is(ErrorType.NOT_FOUND.getErr())))
                    .andExpect(jsonPath("$.error_description", is(ErrorDescription.COURSE_NOT_FOUND.getErrorDesc())));


        }
        @DisplayName("Save Student Profile Return Invalid Interaction Type")
        @Test
        void saveStudentProfileReturnsInvalidInteractionType() throws Exception {


            CreateStudentProfileDto createStudentProfile = new CreateStudentProfileDto();
            createStudentProfile.setRemoteCourseId(List.of(UUID.randomUUID().toString()));

            mockMvc.perform(post(StudentController.ENDPOINT + "/" + student.getRemoteStudentId().toString())
                    .header("Authorization", "Bearer " + accessToken)
                    .content(objectMapper.writeValueAsString(createStudentProfile))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest());

        }

    }
}
