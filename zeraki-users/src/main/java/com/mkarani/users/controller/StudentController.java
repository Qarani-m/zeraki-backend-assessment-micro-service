package com.mkarani.users.controller;

import com.mkarani.users.dto.StudentRequest;
import com.mkarani.users.entity.StudentEntity;
import com.mkarani.users.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // Add a student and assign them a course
    @PostMapping("/add")
    public ResponseEntity<?> addStudent(@RequestBody StudentRequest studentRequest) {
        try {
            StudentEntity student = studentService.addStudent(studentRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(student);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding student.");
        }
    }

    // Delete a student
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting student.");
        }
    }

    // Edit the name of a student
    @PutMapping("/{id}")
    public ResponseEntity<?> editStudentName(@PathVariable Long id, @RequestBody StudentRequest studentRequest) {
        try {
            StudentEntity student = studentService.editStudentName(id, studentRequest.getName());
            return ResponseEntity.ok().body(student);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating student name.");
        }
    }

    // Change the course the student is doing within the same institution
    @PutMapping("/{id}/change-course")
    public ResponseEntity<?> changeStudentCourse(@PathVariable Long id, @RequestBody Course course) {
        try {
            StudentEntity student = studentService.changeStudentCourse(id, course);
            return ResponseEntity.ok().body(student);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error changing student course.");
        }
    }

    // Transfer a student to another university and assign them a course
    @PutMapping("/{id}/transfer")
    public ResponseEntity<?> transferStudent(@PathVariable Long id, @RequestBody TransferRequest transferRequest) {
        try {
            StudentEntity student = studentService.transferStudent(id, transferRequest.getInstitutionId(), transferRequest.getCourse());
            return ResponseEntity.ok().body(student);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error transferring student.");
        }
    }

    // List all students in each institution and be able to search. Be able to filter the list of students by course. When listing students, show 10 students at a time.
    @GetMapping("/search")
    public ResponseEntity<List<StudentEntity>> searchStudents(@RequestParam(required = false) String institution,
                                                        @RequestParam(required = false) String course,
                                                        @RequestParam(required = false) Integer page) {
        try {
            List<StudentEntity> students = studentService.searchStudents(institution, course, page);
            return ResponseEntity.ok().body(students);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error searching students.");
        }
    }
}
