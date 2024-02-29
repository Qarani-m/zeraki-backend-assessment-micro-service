package com.mkarani.students.exceptions;

public class StudentExistError extends RuntimeException {
    public StudentExistError(String studentId) {
        super("An institution with the  id (`"+studentId+"`) does not exist.");
    }
}
