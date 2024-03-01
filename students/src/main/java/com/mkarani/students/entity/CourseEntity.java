package com.mkarani.students.entity;

import jakarta.persistence.*;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CourseEntity {

    private Long id;
    private String courseCode;
    private String name;
    private int studentCount;

}