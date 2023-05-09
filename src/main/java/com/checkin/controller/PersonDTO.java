package com.checkin.controller;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class PersonDTO {
    private Long id;
    private final String firstName;
    private final Boolean checkedIn;

    public PersonDTO(String firstName, Boolean checkedIn) {
//        this.id = id;
        this.firstName = firstName;
        this.checkedIn = checkedIn;
    }

    //    public Long getId() { return id };
    public String getFirstName() {
        return firstName;
    }

    public Boolean getStatus() {
        return checkedIn;
    }
}