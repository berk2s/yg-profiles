package com.yataygecisle.preference.profiles.listeners;

import com.yataygecisle.commons.models.CreatedBasketQueue;
import com.yataygecisle.commons.models.Queues;
import com.yataygecisle.preference.profiles.domain.Course;
import com.yataygecisle.preference.profiles.domain.Student;
import com.yataygecisle.preference.profiles.repository.CourseRepository;
import com.yataygecisle.preference.profiles.repository.StudentRepository;
import com.yataygecisle.preference.profiles.services.StudentService;
import com.yataygecisle.preference.profiles.web.model.CreateStudentProfileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class CreatedBasketListener {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Transactional
    @RabbitListener(queues = Queues.CREATED_BASKET)
    public void receivedMessage(CreatedBasketQueue basket) {
        UUID remoteStudentId = UUID.fromString(basket.getPerformedBy());

        CreateStudentProfileDto createStudentProfile
                = new CreateStudentProfileDto();

        basket.getBasketItems().forEach(basketItem -> {
            createStudentProfile.getRemoteCourseId().add(basketItem.getCourseId());
        });

        Student student = studentRepository
                .findByRemoteStudentId(remoteStudentId)
                .orElseThrow(() -> {
                    log.warn("Cannot find student profile by given remote student id [remoteStudentId: {}]", remoteStudentId.toString());
                    throw new RuntimeException(); // TODO
                });

        createStudentProfile.getRemoteCourseId().forEach(courseId -> {
            UUID remoteCourseId = UUID.fromString(courseId);


            Course course = courseRepository
                    .findByRemoteCourseId(remoteCourseId)
                    .orElseThrow(() -> {
                        log.warn("Cannot find course by given remote course id [remoteCourseId: {}]", remoteCourseId.toString());
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

//        studentService.saveStudentProfile(remoteStudentId, createStudentProfile);

          log.info("Recevied Queue ({}) has been saved as a student profile [remoteStudentId: {}]", Queues.CREATED_BASKET, remoteStudentId.toString());
    }

}
