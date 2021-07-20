package com.yataygecisle.preference.profiles.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yataygecisle.preference.profiles.domain.Course;
import com.yataygecisle.preference.profiles.domain.LikedCourse;
import com.yataygecisle.preference.profiles.domain.Student;
import com.yataygecisle.preference.profiles.repository.CourseRepository;
import com.yataygecisle.preference.profiles.repository.StudentRepository;
import com.yataygecisle.preference.profiles.web.controllers.IntegrationTest;
import com.yataygecisle.preference.profiles.web.models.BasketDto;
import com.yataygecisle.preference.profiles.web.models.BasketItemDto;
import org.awaitility.Awaitility;
import org.awaitility.core.ThrowingRunnable;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static org.awaitility.Awaitility.waitAtMost;

//@Transactional
@ExtendWith(MockitoExtension.class)
class CreatedBasketListenerTest extends IntegrationTest {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    EntityManager entityManager;

    String token;
    String userId;
    BasketDto basket;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        token = createAccessToken();
        userId = parseTokenToUserId(token);


        createStudentProfile(UUID.fromString(userId));

    }

    @Async
    @Transactional
    @DisplayName("Verify Created Basket Listener is Called")
    @Test
    void testCreatedBasketListener() throws Exception {



        waitAtMost(20, TimeUnit.SECONDS)
                .until(() -> {

                    BasketItemDto[] basketItemDtos = getBasketInfo(token);

                    Course course = new Course();
                    course.setRemoteCourseId(UUID.fromString(basketItemDtos[0].getCourseId()));
                    course.setRemoteFacultyId(UUID.fromString(basketItemDtos[0].getFacultyId()));
                    course.setRemoteCollegeId(UUID.fromString(basketItemDtos[0].getCollegeId()));

                    return courseRepository.save(course).getId() != null;

                });

        basket = triggerToCreatingBasket(token, userId);

        TimeUnit.SECONDS.sleep(30);

        Student student = studentRepository.findByRemoteStudentId(UUID.fromString(userId)).orElseThrow(() ->  new RuntimeException());

        student.getLikedCourses().size();

        List<LikedCourse> likedCourses = student.getLikedCourses();

        assertThat(likedCourses.size())
                .isNotEqualTo(0);

    }

}