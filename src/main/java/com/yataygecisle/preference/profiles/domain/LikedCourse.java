package com.yataygecisle.preference.profiles.domain;

import com.yataygecisle.preference.profiles.domain.Course;
import com.yataygecisle.preference.profiles.domain.LikedCourseId;
import com.yataygecisle.preference.profiles.domain.Student;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LikedCourse {

    @EmbeddedId
    private LikedCourseId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("studentId")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("courseId")
    private Course course;

    @ColumnDefault("1")
    @Column(name = "times")
    private Long times;

    public LikedCourse(Student student, Course course) {
        this.course = course;
        this.student = student;
        this.id = new LikedCourseId(student.getId(), course.getId());
    }

    @PrePersist
    public void initialize() {
        if(this.times == null) {
            this.times = 1L;
        }
    }

}
