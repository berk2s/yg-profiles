package com.yataygecisle.preference.profiles.services;

import com.yataygecisle.preference.profiles.web.model.CreateStudentProfileDto;
import com.yataygecisle.preference.profiles.web.model.ProfileAnalyzeDto;
import com.yataygecisle.preference.profiles.web.model.StudentProfileDto;

import java.util.UUID;

public interface StudentService {

    StudentProfileDto getStudentProfile(UUID studentId);

    StudentProfileDto saveStudentProfile(UUID remoteStudentId, CreateStudentProfileDto createStudentProfile);

    ProfileAnalyzeDto getStudentProfileAnalyze(UUID studentId);

}
