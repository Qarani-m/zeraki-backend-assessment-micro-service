package com.mkarani.courses.controller;



import com.mkarani.courses.dto.CourseRequest;
import com.mkarani.courses.entity.CourseEntity;
import com.mkarani.courses.service.CoursesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CoursesService courseService;

    // List all courses done by an institution
    @GetMapping("/courses-by-institution")
    public ResponseEntity<Map<String, List<String>>> getCoursesByInstitution() {
        try {
            Map<String, List<String>> courses = courseService.getCoursesByInstitution();
            return ResponseEntity.ok().body(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    //
// Endpoint to filter courses by searching
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<CourseEntity>> searchCourses(@PathVariable String keyword) {
        List<CourseEntity> filteredCourses = courseService.searchCourses(keyword);
        return ResponseEntity.ok().body(filteredCourses);
    }
    //
//    // Sort courses by name in descending order
    @GetMapping("/sort/{direction}")
    public ResponseEntity<List<CourseEntity>> sortCoursesByNameDesc(@PathVariable String direction) {
        try {
            List<CourseEntity> courses = courseService.sortCoursesByName(direction);
            return ResponseEntity.ok().body(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    //
//    // Delete a course
    @DeleteMapping("/{courseId}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long courseId) {
        try {
            courseService.deleteCourse(courseId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/all-courses")
    public ResponseEntity<?> getAllCourses(){
        List<CourseEntity>courses = courseService.getAllCourses();
        if(courses.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No courses WEre found");
        }
        return  ResponseEntity.status(HttpStatus.OK).body(courses);

    }
    //
//    // Add a course to an institution
    @PostMapping("/new")
    public ResponseEntity<?> addCourse(@RequestBody CourseRequest courseRequest) {
        try {
            CourseEntity course = courseService.addCourse(courseRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(course);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    //
//    // Edit the name of a course
    @PutMapping("/{courseId}")
    public ResponseEntity<?> editCourseName(@PathVariable Long courseId, @RequestBody CourseRequest courseRequest) {
        try {
            System.out.println(courseRequest.getName());
            CourseEntity course = courseService.editCourseName(courseId, courseRequest.getName());
            return ResponseEntity.ok().body(course);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    // Endpoint to retrieve courses based on the selected institution
    @GetMapping("/change-by-institution/{institutionId}")
    public ResponseEntity<List<CourseEntity>> getCoursesByInstitution(@PathVariable Long institutionId) {
        List<CourseEntity> courses = courseService.getCoursesByInstitution2(institutionId);
        return ResponseEntity.ok().body(courses);
    }
}
