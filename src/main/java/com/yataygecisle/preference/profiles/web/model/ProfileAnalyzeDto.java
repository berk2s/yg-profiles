package com.yataygecisle.preference.profiles.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProfileAnalyzeDto {

    private String studentId;

    @JsonProperty("possibilities")
    private List<CourseAnalyzeDto> courseAnalyzesDto = new ArrayList<>();

}
