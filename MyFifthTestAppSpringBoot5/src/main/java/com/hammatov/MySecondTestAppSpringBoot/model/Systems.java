package com.hammatov.MySecondTestAppSpringBoot.model;

import com.fasterxml.jackson.annotation.JsonValue;

// Системы-отправители. В JSON передаётся значение name: "ERP", "CRM", "WMS" или "Service 1"
public enum Systems {

    ERP("ERP", "Enterprise Resource Planning"),
    CRM("CRM", "Customer Relationship Management"),
    WMS("WMS", "Warehouse Management System"),
    // Сервис 1 подставляет это значение в request перед отправкой на Сервис 2
    SERVICE_1("Service 1", "Сервис 1 (отправитель request)");

    private final String name;
    private final String description;

    Systems(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name;
    }

}
