package com.mkarani.users.service;

import com.mkarani.users.dto.StudentRequest;
import com.mkarani.users.entity.StudentEntity;

public interface StudentService {
    StudentEntity addStudent(StudentRequest studentRequest);
}
