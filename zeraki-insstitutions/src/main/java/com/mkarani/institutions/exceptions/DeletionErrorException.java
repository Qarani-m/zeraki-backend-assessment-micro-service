package com.mkarani.institutions.exceptions;



public class DeletionErrorException extends RuntimeException {
    public  DeletionErrorException(Long institutionId) {
        super("An institution with the Id (`"+institutionId+"`) was not found.");
    }
}
