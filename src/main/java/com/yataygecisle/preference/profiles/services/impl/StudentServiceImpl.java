package com.yataygecisle.preference.profiles.services.impl;

import com.yataygecisle.preference.profiles.domain.Course;
import com.yataygecisle.preference.profiles.domain.Student;
import com.yataygecisle.preference.profiles.repository.CourseRepository;
import com.yataygecisle.preference.profiles.repository.StudentRepository;
import com.yataygecisle.preference.profiles.services.StudentService;
import com.yataygecisle.preference.profiles.web.mappers.StudentMapper;
import com.yataygecisle.preference.profiles.web.model.CreateStudentProfileDto;
import com.yataygecisle.preference.profiles.web.model.StudentProfileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final StudentMapper studentProfileMapper;

    @Override
    public StudentProfileDto getStudentProfile(UUID studentId) {
        Student student = studentRepository
                .findByRemoteStudentId(studentId)
                .orElseThrow(() -> {
                    log.warn("Cannot find student profile by given student id [studentId: {}]", studentId);
                    throw new RuntimeException(); // TOOD
                });

        return studentProfileMapper.studentProfileToStudentProfileDto(student);
    }

    @Override
    public StudentProfileDto saveStudentProfile(UUID remoteStudentId, CreateStudentProfileDto createStudentProfile) {
        Student student = studentRepository
                .findByRemoteStudentId(remoteStudentId)
                .orElseThrow(() -> {
                    log.warn("Cannot find student profile by given student id [studentId: {}]", remoteStudentId.toString());
                    throw new RuntimeException(); // TODO
                });

        createStudentProfile.getRemoteCourseId().forEach(courseId -> {
            UUID remoteCourseId = UUID.fromString(courseId);

            Course course = courseRepository
                    .findByRemoteCourseId(remoteCourseId)
                    .orElseThrow(() -> {
                        log.warn("Cannot find course by given course id [courseId: {}]", remoteCourseId.toString());
                        throw new RuntimeException(); // TODO
                    });

            if(student.isCourseExists(course)) {
                student.getLikedCourses().forEach(likedCourse -> {

                    if(likedCourse.getCourse().getRemoteCourseId().equals(remoteCourseId)
                            && likedCourse.getStudent().getRemoteStudentId().equals(remoteStudentId)) {

                        likedCourse.setTimes(likedCourse.getTimes()+1);

                    }

                });
            } else {
                student.addCourse(course);
            }
        });

        return studentProfileMapper.studentProfileToStudentProfileDto(studentRepository.save(student));
    }

}
