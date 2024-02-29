package com.mkarani.students.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "institutions", uniqueConstraints = @UniqueConstraint(columnNames = {"regNumber"}))
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
    private String course;
    private Long institution;
}
