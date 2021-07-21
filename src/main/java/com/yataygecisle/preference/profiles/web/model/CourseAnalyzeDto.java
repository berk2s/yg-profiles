package com.yataygecisle.preference.profiles.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CourseAnalyzeDto {

    private String collegeId;

    private String facultyId;

    private String courseId;

    private Long timesBasket;

    private Long timesTotal;

    private Double possibility;

}
