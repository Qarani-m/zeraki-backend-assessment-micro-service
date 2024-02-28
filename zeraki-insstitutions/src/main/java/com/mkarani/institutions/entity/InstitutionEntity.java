package com.mkarani.institutions.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "institutions", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class InstitutionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private String city;
    private String county;
    private String country;
    private String phoneNumber;

}