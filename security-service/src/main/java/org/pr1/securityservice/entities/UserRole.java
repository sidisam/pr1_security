package org.pr1.securityservice.entities;

import lombok.Getter;

@Getter
public enum UserRole {

    USER("USER"),

    ADMIN("ADMIN");

    private String value;

    UserRole(String value) {
        this.value = value;
    }
}
