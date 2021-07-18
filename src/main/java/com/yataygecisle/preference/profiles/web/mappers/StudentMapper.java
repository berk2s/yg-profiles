package com.yataygecisle.preference.profiles.web.mappers;

import com.yataygecisle.preference.profiles.domain.Student;
import com.yataygecisle.preference.profiles.web.model.StudentProfileDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.UUID;

@Mapper(imports = {UUID.class}, uses = {LikedCourseMapper.class})
public interface StudentMapper {

    @Mappings({
            @Mapping(target = "id", expression = "java( student.getId().toString() )"),
            @Mapping(target = "remoteStudentId", expression = "java( student.getRemoteStudentId().toString() )"),
    })
    StudentProfileDto studentProfileToStudentProfileDto(Student student);

}