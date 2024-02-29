package com.mkarani.students.dto;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class StudentRequest {
    private String regNumber;
    
    private String name;
    private String email;
    private String course;
    private String institution;
}
