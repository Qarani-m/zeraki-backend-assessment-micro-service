package com.mkarani.courses.exceptions;

public class InstitutionExistsException extends RuntimeException {
    public InstitutionExistsException(String institutionName) {
        super("An institution with the same name (`"+institutionName+"`) already exists.");
    }
}
