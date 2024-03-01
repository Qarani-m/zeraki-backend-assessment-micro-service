package com.mkarani.students.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mkarani.students.dto.CourseRequest;
import com.mkarani.students.entity.CourseEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CoursesService {
    Map<String, List<String>> getCoursesByInstitution();

    CourseEntity addCourse(CourseRequest courseRequest);

    String deleteCourse(Long courseId);

    CourseEntity editCourseName(Long courseId, String name) throws JsonProcessingException;

    List<CourseEntity> sortCoursesByName(String direction);

    List<CourseEntity> getAllCourses();

    List<CourseEntity> searchCourses(String keyword);

    List<CourseEntity> getCoursesByInstitution2(Long institutionId);

    List<CourseEntity> findByCourseNameContaining(String courseName);

    Optional<CourseEntity> findById(Long id);
}
