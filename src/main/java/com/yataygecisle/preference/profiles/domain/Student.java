package com.yataygecisle.preference.profiles.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Student extends BaseEntity {

    @Column(name = "remote_student_id", unique = true)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID remoteStudentId;

    @OneToMany(
            mappedBy = "student",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<LikedCourse> likedCourses = new ArrayList<>();

    @Transactional
    public void addCourse(Course course) {
        LikedCourse likedCourse = new LikedCourse(this, course);
        likedCourses.add(likedCourse);
        course.getLikedCourses().add(likedCourse);
    }

    public boolean isCourseExists(Course course) {
        if(course == null)
            return false;

        return likedCourses.stream()
                .map(LikedCourse::getCourse)
                .anyMatch(c -> c.getId().equals(course.getId()) &&
                        c.getRemoteCourseId().equals(course.getRemoteCourseId()) &&
                        c.getRemoteFacultyId().equals(course.getRemoteFacultyId()) &&
                        c.getRemoteCollegeId().equals(course.getRemoteCollegeId()));
    }

    public void removeTag(Course course) {
        for (Iterator<LikedCourse> iterator = likedCourses.iterator(); iterator.hasNext(); ) {
            LikedCourse likedCourse = iterator.next();

            if (likedCourse.getStudent().equals(this) &&
                    likedCourse.getCourse().equals(course)) {
                iterator.remove();
                likedCourse.getCourse().getLikedCourses().remove(likedCourse);
                likedCourse.setCourse(null);
                likedCourse.setStudent(null);
            }
        }
    }

}
