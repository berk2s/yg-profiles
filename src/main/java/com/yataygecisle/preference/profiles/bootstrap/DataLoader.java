package com.yataygecisle.preference.profiles.bootstrap;

import com.yataygecisle.preference.profiles.domain.Student;
import com.yataygecisle.preference.profiles.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Profile("local")
@RequiredArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {

    private final StudentRepository studentRepository;

    @Override
    public void run(String... args) throws Exception {
        Student student = new Student();
        student.setRemoteStudentId(UUID.randomUUID());

        studentRepository.save(student);
    }
}
