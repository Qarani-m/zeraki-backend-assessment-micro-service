package com.mkarani.courses.dto;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CourseRequest {
    private String courseCode;
    private String name;

}
