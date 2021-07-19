package com.yataygecisle.preference.profiles.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class LikedCourseId implements Serializable {

    @Column(name = "student_id")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID studentId;

    @Column(name = "course_id")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID courseId;

    public LikedCourseId(UUID studentId, UUID courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LikedCourseId that = (LikedCourseId) o;
        return Objects.equals(studentId, that.studentId) && Objects.equals(courseId, that.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, courseId);
    }
}