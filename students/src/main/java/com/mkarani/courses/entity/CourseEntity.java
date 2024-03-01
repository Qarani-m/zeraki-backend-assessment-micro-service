package com.mkarani.courses.entity;

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