package com.mkarani.courses.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CourseCountChange {
    private boolean studentCountDirection;
    private  int number= 1;
    private Long id;
}
