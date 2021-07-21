package com.yataygecisle.preference.profiles.services.impl;

import com.yataygecisle.preference.profiles.domain.Course;
import com.yataygecisle.preference.profiles.domain.LikedCourse;
import com.yataygecisle.preference.profiles.domain.Student;
import com.yataygecisle.preference.profiles.repository.CourseRepository;
import com.yataygecisle.preference.profiles.repository.StudentRepository;
import com.yataygecisle.preference.profiles.services.PossibilityService;
import com.yataygecisle.preference.profiles.services.StudentService;
import com.yataygecisle.preference.profiles.web.exceptions.CourseNotFoundException;
import com.yataygecisle.preference.profiles.web.exceptions.InvalidInteractionTypeException;
import com.yataygecisle.preference.profiles.web.exceptions.StudentNotFoundException;
import com.yataygecisle.preference.profiles.web.mappers.StudentMapper;
import com.yataygecisle.preference.profiles.web.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Transactional
@Slf4j
@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final StudentMapper studentProfileMapper;
    private final PossibilityService possibilityService;

    @PreAuthorize("hasAuthority('READ_STUDENT_PROFILE')")
    @PostAuthorize("#studentId.toString() == authentication.principal.getSubject()")
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

    @PreAuthorize("hasAuthority('WRITE_STUDENT_PROFILE')")
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

            if (student.isCourseExists(course)) {
                student.getLikedCourses().forEach(likedCourse -> {
                    if (likedCourse.getCourse().getRemoteCourseId().equals(remoteCourseId)
                            && likedCourse.getStudent().getRemoteStudentId().equals(remoteStudentId)) {
                        if(createStudentProfile.getInteractionType().equals(InteractionType.FROM_BASKET)) {
                            likedCourse.setTimesBasket(likedCourse.getTimesBasket() + 1);
                        } else {
                            log.warn("Unknown interaction type [interactionType: {}]", createStudentProfile.getInteractionType());
                            throw new InvalidInteractionTypeException(ErrorDescription.INVALID_INTERACTION_TYPE.getErrorDesc());
                        }

                        likedCourse.setTimesTotal(likedCourse.getTimesTotal() + 1);
                    }
                });
            } else {
                student.addCourse(course);
            }
        });

        log.info("Student Profile has been created [remoteStudentId: {}]", remoteStudentId.toString());

        return studentProfileMapper.studentProfileToStudentProfileDto(studentRepository.save(student));
    }

    @Override
    public ProfileAnalyzeDto getStudentProfileAnalyze(UUID studentId) {
        Student student = studentRepository.findByRemoteStudentId(studentId)
                .orElseThrow(() -> {
                    log.warn("Cannot find student by given remote student id [remoteStudentId: {}]", studentId);
                    throw new StudentNotFoundException(ErrorDescription.STUDENT_PROFILE_NOT_FOUND.getErrorDesc());
                });

        List<LikedCourse> likedCourses = student.getLikedCourses();

        ProfileAnalyzeDto profileAnalyze = new ProfileAnalyzeDto();
        profileAnalyze.setStudentId(studentId.toString());

        likedCourses.forEach(likedCourse -> {
            CourseAnalyzeDto courseAnalyze = new CourseAnalyzeDto();
            courseAnalyze.setCollegeId(likedCourse.getCourse().getRemoteCollegeId().toString());
            courseAnalyze.setFacultyId(likedCourse.getCourse().getRemoteFacultyId().toString());
            courseAnalyze.setCourseId(likedCourse.getCourse().getRemoteCourseId().toString());
            courseAnalyze.setTimesBasket(likedCourse.getTimesBasket());
            courseAnalyze.setTimesTotal(likedCourse.getTimesTotal());

            Double possibility = possibilityService.calculate(likedCourse.getTimesBasket());
            courseAnalyze.setPossibility(possibility);

            profileAnalyze.getCourseAnalyzesDto().add(courseAnalyze);
        });

        return profileAnalyze;
    }

}
