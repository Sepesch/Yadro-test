package com.yadro.yadro.app.exception;

public class PersonNotFoundException extends RuntimeException {
    public PersonNotFoundException(Long id) {
        super("Person with id " + id + " not found");
    }
}