package com.mkarani.institutions.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InstitutionRequest {
    private String name;
    private String address;
    private String city;
    private String county;
    private String country;
    private String phoneNumber;
}
