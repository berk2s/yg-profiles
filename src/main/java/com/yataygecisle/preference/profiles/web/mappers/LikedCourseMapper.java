package com.yataygecisle.preference.profiles.web.mappers;

import com.yataygecisle.preference.profiles.domain.LikedCourse;
import com.yataygecisle.preference.profiles.web.model.LikedCourseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.UUID;

@Mapper(imports = {UUID.class})
public interface LikedCourseMapper {

    @Mappings({
            @Mapping(target = "courseId", expression = "java( likedCourse.getId().getCourseId().toString() )"),
            @Mapping(target = "remoteCollegeId", expression = "java( likedCourse.getCourse().getRemoteCollegeId().toString() )"),
            @Mapping(target = "remoteFacultyId", expression = "java( likedCourse.getCourse().getRemoteFacultyId().toString() )"),
            @Mapping(target = "remoteCourseId", expression = "java( likedCourse.getCourse().getRemoteCourseId().toString() )"),
            @Mapping(target = "times", expression = "java( likedCourse.getTimes() )"),
    })
    LikedCourseDto likedCourseToLikedCourseDto(LikedCourse likedCourse);

}
