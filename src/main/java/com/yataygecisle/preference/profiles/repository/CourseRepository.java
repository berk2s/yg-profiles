package com.yataygecisle.preference.profiles.repository;

import com.yataygecisle.preference.profiles.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {

    Optional<Course> findByRemoteCourseId(UUID remoteCourseId);

}
