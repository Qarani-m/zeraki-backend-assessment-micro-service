package com.mkarani.courses.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "courses", uniqueConstraints = @UniqueConstraint(columnNames = {"courseCode"}))
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CourseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String courseCode;
    private String name;
    private int studentCount;
}
