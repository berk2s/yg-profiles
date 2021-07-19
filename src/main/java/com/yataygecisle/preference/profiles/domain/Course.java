package com.yataygecisle.preference.profiles.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Course extends BaseEntity {

    @Column(name = "remote_college_id")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID remoteCollegeId;

    @Column(name = "remote_faculty_id")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID remoteFacultyId;

    @Column(name = "remote_course_id", unique = true)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID remoteCourseId;

    @OneToMany(
            mappedBy = "course",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<LikedCourse> likedCourses = new ArrayList<>();


}
