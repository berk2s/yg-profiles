package com.yataygecisle.preference.profiles.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikedCourseDto {

    private String courseId;

    private String remoteCollegeId;

    private String remoteFacultyId;

    private String remoteCourseId;

    private Long times;

}
