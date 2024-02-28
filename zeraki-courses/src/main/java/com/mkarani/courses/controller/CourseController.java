package com.mkarani.courses.controller;


import com.mkarani.courses.dto.CourseRequest;
import com.mkarani.courses.entity.CourseEntity;
import com.mkarani.courses.service.CoursesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CoursesService courseService;

    // List all courses done by an institution
    @GetMapping("/by-institution/{institutionId}")
    public ResponseEntity<List<CourseEntity>> getCoursesByInstitution(@PathVariable Long institutionId) {
        try {
            List<CourseEntity> courses = courseService.getCoursesB  yInstitution(institutionId);
            return ResponseEntity.ok().body(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Filter courses by searching and changing institution
    @GetMapping("/search")
    public ResponseEntity<List<CourseEntity>> searchCourses(@RequestParam(required = false) String institution,
                                                      @RequestParam(required = false) String keyword) {
        try {
            List<CourseEntity> courses = courseService.searchCourses(institution, keyword);
            return ResponseEntity.ok().body(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Sort courses by name in ascending order
    @GetMapping("/sort/asc")
    public ResponseEntity<List<CourseEntity>> sortCoursesByNameAsc(@RequestParam(required = false) String institution) {
        try {
            List<CourseEntity> courses = courseService.sortCoursesByNameAsc(institution);
            return ResponseEntity.ok().body(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Sort courses by name in descending order
    @GetMapping("/sort/desc")
    public ResponseEntity<List<CourseEntity>> sortCoursesByNameDesc(@RequestParam(required = false) String institution) {
        try {
            List<CourseEntity> courses = courseService.sortCoursesByNameDesc(institution);
            return ResponseEntity.ok().body(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Delete a course
    @DeleteMapping("/{courseId}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long courseId) {
        try {
            courseService.deleteCourse(courseId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Add a course to an institution
    @PostMapping("/add")
    public ResponseEntity<?> addCourse(@RequestBody CourseRequest courseRequest) {
        try {
            CourseEntity course = courseService.addCourse(courseRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(course);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Edit the name of a course
    @PutMapping("/{courseId}")
    public ResponseEntity<?> editCourseName(@PathVariable Long courseId, @RequestBody CourseRequest courseRequest) {
        try {
            CourseEntity course = courseService.editCourseName(courseId, courseRequest.getName());
            return ResponseEntity.ok().body(course);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
