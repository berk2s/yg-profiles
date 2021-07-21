package com.yataygecisle.preference.profiles.web.model;

import lombok.Getter;

@Getter
public enum ErrorDescription {
    STUDENT_PROFILE_NOT_FOUND("Student profile not found"),
    COURSE_NOT_FOUND("Course not found"),
    INVALID_INTERACTION_TYPE("Invalid interaction type");

    String errorDesc;

    ErrorDescription(String errorDesc) {
        this.errorDesc = errorDesc;
    }
}
