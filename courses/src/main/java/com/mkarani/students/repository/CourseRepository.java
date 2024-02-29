package com.mkarani.students.repository;


import com.mkarani.students.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<CourseEntity, Long> {
    Optional<CourseEntity> findByName(String nameNode);

    @Query(value = "SELECT * FROM institutions ORDER BY name DESC", nativeQuery = true)
    List<CourseEntity> findAllOrderedByNameDesc();

    @Query(value = "SELECT * FROM institutions ORDER BY name ASC", nativeQuery = true)
    List<CourseEntity> findAllOrderedByNameAsc();

    List<CourseEntity> findByCourseCode(String courseCode);
    @Query(value = "SELECT * FROM courses WHERE name LIKE %:keyword%", nativeQuery = true)
    List<CourseEntity> findByCourseNameContaining(String keyword);


}
