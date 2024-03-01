package com.mkarani.courses.controller;


import com.mkarani.courses.dto.ChangeCourseDto;
import com.mkarani.courses.dto.StudentNewNameDto;
import com.mkarani.courses.dto.StudentRequest;
import com.mkarani.courses.entity.StudentEntity;
import com.mkarani.courses.exceptions.InstitutionExistsException;
import com.mkarani.courses.exceptions.StudentExistError;
import com.mkarani.courses.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mkarani.courses.dto.ChangeInstitutionDto;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    @Autowired
    private StudentService studentService;

    // Add a student and assign them a course
    @PostMapping("/new")
    public ResponseEntity<?> addStudent(@RequestBody StudentRequest studentDto) throws Exception {
        try{
            String message = studentService.addStudent(studentDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(message);

        }catch (StudentExistError e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());


        }
    }
    // Delete a student
    @DeleteMapping("/delete/{studentId}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long studentId) {
        try{
            studentService.deleteStudent(studentId);
            return ResponseEntity.noContent().build();

        }catch (StudentExistError e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    // Edit the name of a student
    @PutMapping("/edit/{studentId}")
    public ResponseEntity<?> editStudentName(@PathVariable Long studentId, @RequestBody StudentNewNameDto studentDto) {
        try{
            studentService.editStudentName(studentId, studentDto.getName());
            return ResponseEntity.ok().build();

        }catch (StudentExistError e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    // Change the course the student is doing within the same institution
    @PutMapping("/change-course/{studentId}")
    public ResponseEntity<?> changeStudentCourse(@PathVariable Long studentId, @RequestBody ChangeCourseDto courseDto) throws Exception {
        try {
            studentService.changeStudentCourse(studentId, courseDto.getCourse());
            return ResponseEntity.ok().build();
        }catch (StudentExistError e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/transfer/{studentId}")
    public ResponseEntity<?> transferStudent(@PathVariable Long studentId, @RequestBody ChangeInstitutionDto institutionDto) throws Exception {
        try {

            StudentEntity student = studentService.transferStudent(studentId, institutionDto);
            return ResponseEntity.ok().body(student);

        }catch (InstitutionExistsException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }


    }

    @GetMapping("/list-students")
    public  ResponseEntity<List<StudentEntity>> listStudents(){
        List<StudentEntity> studentEntities = studentService.listStudents();
        return  ResponseEntity.ok(studentEntities);
    }
    //    List all students in each institution
    @GetMapping("/students-in-institution/{institution}")
    public ResponseEntity<?> studentsInEachInewnstitution(@PathVariable String institution){
        try{
            List<StudentEntity> studentEntities = studentService.studentsInEachInstitution(institution);
            return ResponseEntity.status(HttpStatus.OK).body(studentEntities);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //    List a specific student in a specific institution
    @GetMapping("/students-in-institution/{institution}/{studentName}")
    public ResponseEntity<?> specificStudentIninstitution(@PathVariable String institution, @PathVariable String student) {
        try{
            List<StudentEntity> studentEntities = studentService.specificStudentIninstitution(institution, student);
            return ResponseEntity.status(HttpStatus.OK).body(studentEntities);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    //Filter students by course
    @GetMapping("/filter-students-by-course/{courseCode}")
    public ResponseEntity<?>  filterStudentsByCourse(@PathVariable String courseCode){
        try{
            List<StudentEntity> studentEntities = studentService.listStudentsByCourse(courseCode);
            return ResponseEntity.status(HttpStatus.OK).body(studentEntities);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
