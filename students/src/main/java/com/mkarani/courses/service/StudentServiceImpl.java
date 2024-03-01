package com.mkarani.courses.service;


import com.mkarani.courses.dto.ChangeInstitutionDto;
import com.mkarani.courses.dto.CourseCountChange;
import com.mkarani.courses.dto.InstitutionRequest;
import com.mkarani.courses.dto.StudentRequest;
import com.mkarani.courses.entity.CourseEntity;
import com.mkarani.courses.entity.InstitutionEntity;
import com.mkarani.courses.entity.StudentEntity;
import com.mkarani.courses.exceptions.InstitutionExistsException;
import com.mkarani.courses.exceptions.StudentExistError;
import com.mkarani.courses.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class StudentServiceImpl implements StudentService{
    @Autowired
    private StudentRepository studentRepository;

    private void handleRestCommunication(RestTemplate template,String url, Object parameter,Object responseType){
        ResponseEntity<List<CourseEntity>> courseIdResponse = template.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CourseEntity>>() {},
                parameter.toString()
        );

    }

    @Override
    public String addStudent(StudentRequest studentRequest) throws Exception {
        RestTemplate template = new RestTemplate();
        ResponseEntity<List<InstitutionEntity>> response = template.exchange(
                "http://localhost:9896/api/institutions/search/{name}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<InstitutionEntity>>() {},
                studentRequest.getName()
        );
        List<InstitutionEntity> institutionIdList = response.getBody();
        Long institutionId = null;
        assert institutionIdList != null;
        if(institutionIdList.isEmpty()){
            throw new Exception("Institution with name: "+studentRequest.getInstitution()+" does not Exist");
        }
        institutionId = institutionIdList.get(0).getId();
        ResponseEntity<Optional<InstitutionEntity>> institutionEntity = template.exchange(
                "http://localhost:9896/api/institutions/find-by-id/{id}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Optional<InstitutionEntity>>() {},
                institutionId
        );
        if(Objects.requireNonNull(institutionEntity.getBody()).isEmpty()){
            throw new Exception("Institution with name2: "+studentRequest.getInstitution()+" does not Exist");
        }
        ResponseEntity<List<CourseEntity>> courseIdResponse = template.exchange(
                "http://localhost:9896/api/courses/findByCourseNameContaining/{courseName}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CourseEntity>>() {},
                studentRequest.getCourse()
        );

        Long courseId = Objects.requireNonNull(courseIdResponse.getBody()).get(0).getId();
        ResponseEntity<Optional<CourseEntity> > courseEntityResponse = template.exchange(
                "http://localhost:9896/api/courses/find-by-id/{id}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Optional<CourseEntity> >() {},
                courseId
        );

        Optional<CourseEntity> courseEntity = courseEntityResponse.getBody();
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
            InstitutionEntity institution = institutionEntity.getBody().get();
            institution.setStudentReg(Collections.singletonList(studentRequest.getRegNumber()));




            institution.setStudentCount(institution.getStudentCount() + 1);
            CourseEntity course = courseEntity.get();



            InstitutionRequest institutionRequest =InstitutionRequest.builder()
                    .name(institution.getName())
                    .address(institution.getAddress())
                    .city(institution.getCity())
                    .country(institution.getCountry())
                    .county(institution.getCounty())
                    .phoneNumber(institution.getPhoneNumber())
                    .coursesOffered(institution.getCoursesOffered())
                    .build();

            CourseCountChange courseCountChange = CourseCountChange.builder()
                    .studentCountDirection(true)

                    .id(courseId)
                    .build();


            HttpEntity<CourseCountChange> requestEntity = new HttpEntity<>(courseCountChange);

// Make the request with the HttpEntity containing the courseCountChange object
            ResponseEntity<CourseEntity> responseEntity = template.exchange(
                    "http://localhost:9896/api/courses/change-student-count",
                    HttpMethod.POST,  // Assuming you want to send it as a POST request
                    requestEntity,    // Pass the requestEntity containing the courseCountChange object
                    CourseEntity.class
            );


            HttpEntity<InstitutionRequest> updateEntity = new HttpEntity<>(institutionRequest);

// Make the request with the HttpEntity containing the courseCountChange object
            template.exchange(
                    "http://localhost:9896/api/courses/change-student-count",
                    HttpMethod.PUT,  // Assuming you want to send it as a POST request
                    updateEntity,    // Pass the requestEntity containing the courseCountChange object
                    InstitutionRequest.class
            );



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
