package com.yataygecisle.preference.profiles.repository;

import com.yataygecisle.preference.profiles.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {

    Optional<Student> findByRemoteStudentId(UUID remoteStudentId);

}
