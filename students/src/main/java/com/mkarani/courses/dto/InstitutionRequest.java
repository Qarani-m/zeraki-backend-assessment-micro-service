package com.mkarani.courses.dto;


import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class InstitutionRequest {
    private String name;
    private String address;
    private String city;
    private String county;
    private String country;
    private String phoneNumber;
    private List<String> coursesOffered;
}
