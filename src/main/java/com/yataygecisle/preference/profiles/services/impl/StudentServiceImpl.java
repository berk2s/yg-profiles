package com.yataygecisle.preference.profiles.services.impl;

import com.yataygecisle.preference.profiles.domain.Course;
import com.yataygecisle.preference.profiles.domain.Student;
import com.yataygecisle.preference.profiles.repository.CourseRepository;
import com.yataygecisle.preference.profiles.repository.StudentRepository;
import com.yataygecisle.preference.profiles.services.StudentService;
import com.yataygecisle.preference.profiles.web.exceptions.CourseNotFoundException;
import com.yataygecisle.preference.profiles.web.exceptions.StudentNotFoundException;
import com.yataygecisle.preference.profiles.web.mappers.StudentMapper;
import com.yataygecisle.preference.profiles.web.model.CreateStudentProfileDto;
import com.yataygecisle.preference.profiles.web.model.ErrorDescription;
import com.yataygecisle.preference.profiles.web.model.StudentProfileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Transactional
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
                    throw new StudentNotFoundException(ErrorDescription.STUDENT_PROFILE_NOT_FOUND.getErrorDesc());
                });

        return studentProfileMapper.studentProfileToStudentProfileDto(student);
    }

    @Transactional
    @Override
    public StudentProfileDto saveStudentProfile(UUID remoteStudentId, CreateStudentProfileDto createStudentProfile) {
        Student student = studentRepository
                .findByRemoteStudentId(remoteStudentId)
                .orElseThrow(() -> {
                    log.warn("Cannot find student profile by given remote student id [remoteStudentId: {}]", remoteStudentId.toString());
                    throw new StudentNotFoundException(ErrorDescription.STUDENT_PROFILE_NOT_FOUND.getErrorDesc());
                });

        createStudentProfile.getRemoteCourseId().forEach(courseId -> {
            UUID remoteCourseId = UUID.fromString(courseId);


            Course course = courseRepository
                    .findByRemoteCourseId(remoteCourseId)
                    .orElseThrow(() -> {
                        log.warn("Cannot find course by given remote course id [remoteCourseId: {}]", remoteCourseId.toString());
                        throw new CourseNotFoundException(ErrorDescription.COURSE_NOT_FOUND.getErrorDesc());
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

        log.info("Student Profile has been created [remoteStudentId: {}]", remoteStudentId.toString());

        return studentProfileMapper.studentProfileToStudentProfileDto(studentRepository.save(student));
    }

}
