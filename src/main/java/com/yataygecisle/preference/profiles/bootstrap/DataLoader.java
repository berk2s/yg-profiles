package com.yataygecisle.preference.profiles.bootstrap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yataygecisle.preference.profiles.domain.Course;
import com.yataygecisle.preference.profiles.domain.Student;
import com.yataygecisle.preference.profiles.repository.CourseRepository;
import com.yataygecisle.preference.profiles.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.UUID;

@Profile("local")
@RequiredArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {
        Student student = new Student();

//        String token = createAccessToken();
//        UUID userId = UUID.fromString(parseTokenToUserId(token));
//
//        UUID remoteStudentId = userId;
//
//        student.setRemoteStudentId(remoteStudentId);
//
//        studentRepository.save(student);

    }

    private String createAccessToken() throws JsonProcessingException {
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

    private String parseTokenToUserId(String token) throws JsonProcessingException {
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getDecoder();
        String payload = new String(decoder.decode(chunks[1]));
        JsonNode root = objectMapper.readTree(payload);
        JsonNode sub = root.path("sub");

        return sub.asText();
    }
}
