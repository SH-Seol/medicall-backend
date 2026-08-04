package com.medicall.domain.patient;

public record ContactPerson(
        String name,
        String relationship,
        String phoneNumber
) {
    public boolean isEmpty(){
        return name == null && relationship == null && phoneNumber == null;
    }
}
