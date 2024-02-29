package com.mkarani.students.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.mkarani.students.dto.CourseRequest;
import com.mkarani.students.entity.CourseEntity;
import com.mkarani.students.entity.InstitutionEntity;
import com.mkarani.students.exceptions.DeletionErrorException;
import com.mkarani.students.exceptions.InstitutionExistsException;
import com.mkarani.students.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class CourseServicesImpl implements CoursesService{


    @Autowired
    private CourseRepository courseRepository;

    @Override
    public Map<String, List<String>> getCoursesByInstitution() {
        Map<String, List<String>> courseMapList = new HashMap<>();
        RestTemplate template = new RestTemplate();
        ResponseEntity<List<InstitutionEntity>> response = template.exchange(
                "http://localhost:9896/api/institutions/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<InstitutionEntity>>() {}
        );
        List<InstitutionEntity> institutionList = response.getBody();
        assert institutionList != null;
        for (InstitutionEntity institution : institutionList) {
            String institutionName = institution.getName();
            List<String> courses = new ArrayList<>();
            for (String courseCode : institution.getCoursesOffered()) {
                String courseName = getCourseName(courseCode);
                courses.add(courseName);
            }
            courseMapList.put(institutionName, courses);
        }
        return courseMapList;
    }

    private String getCourseName(String courseCode) {
        List<CourseEntity> courses = courseRepository.findByCourseCode(courseCode);
        if (!courses.isEmpty()) {
            return courses.get(0).getName();
        } else {
            return "--no-course-found--";
        }
    }



    @Override
    public CourseEntity addCourse(CourseRequest courseRequest) {
        CourseEntity courseEntity = CourseEntity.builder()
                .courseCode(courseRequest.getCourseCode())
                .name(courseRequest.getName())
                .studentCount(0)
                .build();

        return courseRepository.save(courseEntity);
    }

    @Override
    public String deleteCourse(Long courseId) {
        try{
            courseRepository.deleteById(courseId);
            return "Institution with Id "+courseId+" deleted Successfull";
        }catch (Exception e){
            throw new DeletionErrorException(courseId);

        }
    }
    @Override
    public CourseEntity editCourseName(Long id, String newInstitutionName) throws JsonProcessingException {



        Optional<CourseEntity> existingCourseByName = courseRepository.findByName(newInstitutionName);
        Optional<CourseEntity> existingCourseById = courseRepository.findById(id);
        if (existingCourseByName.isPresent()) {
            throw new InstitutionExistsException(newInstitutionName);
        }
        if (existingCourseById.isEmpty()) {
            throw new IllegalArgumentException("Institution with the Id "+id+" does not exist");
        }
        CourseEntity courseEntity = existingCourseById.get();
        courseEntity.setName(newInstitutionName);
        return courseRepository.save(courseEntity);
    }
    @Override
    public List<CourseEntity> sortCoursesByName(String direction) {
        if(direction.toUpperCase().contains("DESC")){
            return courseRepository.findAllOrderedByNameDesc();
        }
        return courseRepository.findAllOrderedByNameAsc();
    }

    @Override
    public List<CourseEntity> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public List<CourseEntity> searchCourses(String keyword) {
        return courseRepository.findByCourseNameContaining(keyword);
    }

    @Override
    public List<CourseEntity> getCoursesByInstitution2(Long institutionId) {
        return null;
    }
}
