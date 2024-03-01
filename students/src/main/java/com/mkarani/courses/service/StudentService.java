package com.mkarani.courses.service;


import com.mkarani.courses.dto.ChangeInstitutionDto;
import com.mkarani.courses.dto.StudentRequest;
import com.mkarani.courses.entity.StudentEntity;

import java.util.List;

public interface StudentService {
    String addStudent(StudentRequest studentDto) throws Exception;

    void deleteStudent(Long studentId);

    void editStudentName(Long studentId, String name);

    void changeStudentCourse(Long studentId, String course) throws Exception;

    StudentEntity transferStudent(Long studentId, ChangeInstitutionDto changeInstitutionDto) throws Exception;

    List<StudentEntity> listStudents();

    List<StudentEntity> listStudentsByCourse(String courseCode) throws Exception;

    List<StudentEntity> studentsInEachInstitution(String institution);

    List<StudentEntity> specificStudentIninstitution(String institution, String student);
}
