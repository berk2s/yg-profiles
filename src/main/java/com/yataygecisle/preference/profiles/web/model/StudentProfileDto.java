package com.yataygecisle.preference.profiles.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class StudentProfileDto {

    private String id;

    private String remoteStudentId;

    private Timestamp createdAt;

    private Timestamp lastModifiedDate;

    private List<LikedCourseDto> likedCourses = new ArrayList<>();

}