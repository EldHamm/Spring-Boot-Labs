package com.hammatov.MySecondTestAppSpringBoot.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Codes {

    SUCCESS("success"),
    FAILED("failed");

    private final String name;

    Codes(String name) {
        this.name = name;
    }

    // В JSON попадает строка «success» или «failed», а не имя константы
    @JsonValue
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

}
