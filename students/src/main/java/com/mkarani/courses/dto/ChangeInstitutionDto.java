package com.mkarani.courses.dto;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ChangeInstitutionDto {
    private Long oldInstituteId;
    private Long newInstituteId;
    private String oldInstitutionCourseCode;
    private String newInstitutionCourseCode;
    private String newRegNumber;
}
