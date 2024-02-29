package com.mkarani.students.service;


import com.mkarani.students.dto.ChangeInstitutionDto;
import com.mkarani.students.dto.StudentRequest;
import com.mkarani.students.entity.StudentEntity;
import com.mkarani.students.exceptions.InstitutionExistsException;
import com.mkarani.students.exceptions.StudentExistError;
import com.mkarani.students.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService{
    @Autowired
    private StudentRepository studentRepository;


    @Override
    public String addStudent(StudentRequest studentRequest) throws Exception {
        List<InstitutionEntity> institutionIdList = institutionRepository.findByNameContaining(studentRequest.getInstitution());



        Long institutionId = null;
        if(institutionIdList.isEmpty()){
            throw new Exception("Institution with name: "+studentRequest.getInstitution()+" does not Exist");
        }
        institutionId = institutionIdList.get(0).getId();

        Optional<InstitutionEntity> institutionEntity = institutionRepository.findById(institutionId);
        if(institutionEntity.isEmpty()){
            throw new Exception("Institution with name2: "+studentRequest.getInstitution()+" does not Exist");
        }
        Long courseId = courseRepository.findByCourseNameContaining(studentRequest.getCourse()).get(0).getId();
        Optional<CourseEntity> courseEntity = courseRepository.findById(courseId);
        if(courseEntity.isEmpty()){
            throw new Exception("Course with name: "+studentRequest.getCourse()+" does not Exist");
        }
        StudentEntity studentEntity = StudentEntity.builder()
                .regNumber(studentRequest.getRegNumber())
                .name(studentRequest.getName())
                .email(studentRequest.getEmail())
                .course(studentRequest.getCourse())
                .institution(institutionId)
                .build();
        try {
            studentRepository.save(studentEntity);
            InstitutionEntity institution = institutionEntity.get();
            institution.setStudentReg(Collections.singletonList(studentRequest.getRegNumber()));
            institution.setStudentCount(institution.getStudentCount() + 1);
            institutionRepository.save(institution);
            CourseEntity course = courseEntity.get();
            course.setStudentCount(course.getStudentCount() + 1);
            courseRepository.save(course);

            return  "Student Registered Succesfully";
        }catch (Exception e){
            return e.getMessage();
        }
    }

    @Override
    public void deleteStudent(Long studentId) {
        Optional<StudentEntity> studentEntity = studentRepository.findById(studentId);
        if(studentEntity.isEmpty()){
            throw  new StudentExistError(studentId.toString());
        }
        studentRepository.deleteById(studentId);
    }

    @Override
    public void editStudentName(Long studentId, String name) {
        Optional<StudentEntity> studentEntity = studentRepository.findById(studentId);
        if(studentEntity.isEmpty()){
            throw  new StudentExistError(studentId.toString());
        }
        StudentEntity student = studentEntity.get();
        student.setName(name);
        studentRepository.save(student);
    }

    @Override
    public void changeStudentCourse(Long studentId, String course) throws Exception {
        Optional<StudentEntity> studentEntity = studentRepository.findById(studentId);
        if(studentEntity.isEmpty()){
            throw  new StudentExistError(studentId.toString());
        }
        Long courseId = courseRepository.findByCourseNameContaining(course).get(0).getId();
        Optional<CourseEntity> courseEntity = courseRepository.findById(courseId);
        if(courseEntity.isEmpty()){
            throw new Exception("Course with name: "+course+" does not Exist");
        }
        StudentEntity student = studentEntity.get();
        CourseEntity courses = courseEntity.get();
        int realNumber = courses.getStudentCount()-1;
        courses.setStudentCount(realNumber);
        student.setCourse(course);
        courseRepository.save(courses);
        studentRepository.save(student);
    }

    @Override
    public StudentEntity transferStudent(Long studentId, ChangeInstitutionDto changeInstitutionDto) throws Exception {

     try{
         Optional<InstitutionEntity> oldInstitutionEntity = institutionRepository.findById(changeInstitutionDto.getOldInstituteId());
        if (oldInstitutionEntity.isEmpty()) {
            throw new InstitutionExistsException("Institution with name: " + changeInstitutionDto.getOldInstituteId().toString() + " does not Exist");
        }
        Optional<StudentEntity> oldStudentEntity = studentRepository.findById(studentId);
        if (oldStudentEntity.isEmpty()) {
            throw new InstitutionExistsException("Student with id: " + studentId + " does not Exist");
        }
//        Remove student from the old institution
        InstitutionEntity oldInstitution = oldInstitutionEntity.get();
        List<String> oldInstitutionRegNumbers = oldInstitution.getStudentReg();
        StudentEntity oldStudent = oldStudentEntity.get();
        oldInstitutionRegNumbers.remove(oldStudent.getRegNumber());
        institutionRepository.save(oldInstitution);

//        Add Student to the new institution

        Optional<InstitutionEntity> newInstitutionEntity = institutionRepository.findById(changeInstitutionDto.getNewInstituteId());
        if (newInstitutionEntity.isEmpty()) {
            throw new InstitutionExistsException("Institution with name: " + changeInstitutionDto.getNewInstituteId().toString() + " does not Exist");
        }

        InstitutionEntity newInstitution = newInstitutionEntity.get();
        List<String> studentRegNumbers = newInstitution.getStudentReg();
        studentRegNumbers.add(changeInstitutionDto.getNewRegNumber());
        oldStudent.setInstitution(changeInstitutionDto.getNewInstituteId());
        institutionRepository.save(newInstitution);

        return studentRepository.save(oldStudent);

    }catch(Exception e){
        System.out.println(e.getMessage());
        return  null;

    }}

    @Override
    public List<StudentEntity> listStudents() {
        return studentRepository.findAll();
    }

    @Override
    public List<StudentEntity> listStudentsByCourse(String courseCode) throws Exception {
        try{
            return studentRepository.findByCourse(courseCode);

        }catch(Exception e){
            throw new Exception("Error when deleting student");
        }
    }

    @Override
    public List<StudentEntity> studentsInEachInstitution(String institution) {
        List<InstitutionEntity> institutionEntity = institutionRepository.findByNameContaining(institution);
        Long institutionId = institutionEntity.get(0).getId();
        return studentRepository.findAllByInstitution(institutionId);
    }

    @Override
    public List<StudentEntity> specificStudentIninstitution(String institution, String student) {
        List<StudentEntity> students = studentsInEachInstitution(institution);
        List<StudentEntity> returnList = new ArrayList<>();
        for(StudentEntity studentEntity: students){
            if(studentEntity.getName().contains(student)){
                returnList.add(studentEntity);

            }
        }
        return returnList;
    }


}
