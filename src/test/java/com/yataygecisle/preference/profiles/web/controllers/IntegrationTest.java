package com.yataygecisle.preference.profiles.web.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yataygecisle.preference.profiles.domain.Course;
import com.yataygecisle.preference.profiles.domain.Student;
import com.yataygecisle.preference.profiles.repository.CourseRepository;
import com.yataygecisle.preference.profiles.repository.StudentRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.*;

@Transactional
@Getter
@SpringBootTest
@ContextConfiguration
@AutoConfigureMockMvc
public class IntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    public String createAccessToken() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl
                = "http://localhost:8080/spring-rest/foos";
        ObjectMapper mapper = new ObjectMapper();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        map.add("client_id", "clientWithSecret");
        map.add("client_secret", "clientSecret");
        map.add("grant_type", "password");
        map.add("username", "username");
        map.add("password", "password");
        map.add("scope", "openid");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);


        ResponseEntity<String> response
                = restTemplate.postForEntity("http://localhost:8080/token",
                request,
                String.class);

        JsonNode root = mapper.readTree(response.getBody());
        JsonNode token = root.path("access_token");
        return token.asText();
    }

    public String parseTokenToUserId(String token) throws JsonProcessingException {
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getDecoder();
        String payload = new String(decoder.decode(chunks[1]));
        JsonNode root = objectMapper.readTree(payload);
        JsonNode sub = root.path("sub");

        return sub.asText();
    }

    @Transactional
    public HashMap createStudentProfile(UUID studentId) {
        Course savedCourse = createCourse();

        Optional<Course> course = courseRepository.findById(savedCourse.getId());

        Student student = new Student();
        student.setRemoteStudentId(studentId);
        student.addCourse(course.get());

        Student savedStudent = studentRepository.save(student);

        HashMap map = new HashMap<Student, Object>();
        map.put("student", savedStudent);
        map.put("course", savedCourse);

        return map;
    }

    @Transactional
    public Course createCourse() {
        Course course = new Course();
        course.setRemoteCourseId(UUID.randomUUID());
        course.setRemoteFacultyId(UUID.randomUUID());
        course.setRemoteCollegeId(UUID.randomUUID());

        return courseRepository.save(course);
    }

}
