package com.mkarani.users.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "students", uniqueConstraints = @UniqueConstraint(columnNames = {"regNumber"}))
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String regNumber;
    private String name;
    private String email;
    private List courses;
    private String institution;

}
