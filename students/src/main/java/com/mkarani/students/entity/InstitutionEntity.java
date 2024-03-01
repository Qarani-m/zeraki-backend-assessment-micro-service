package com.mkarani.students.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class InstitutionEntity {
    private Long id;
    private String name;
    private String address;
    private String city;
    private String county;
    private String country;
    private String phoneNumber;
    private List<String> studentReg;
    private List<String> coursesOffered;
    private Long studentCount;
}
