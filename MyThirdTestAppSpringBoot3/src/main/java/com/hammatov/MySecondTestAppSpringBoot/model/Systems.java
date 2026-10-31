package com.hammatov.MySecondTestAppSpringBoot.model;

// Системы-отправители. В JSON передаётся имя константы: "ERP", "CRM" или "WMS"
public enum Systems {

    ERP("Enterprise Resource Planning"),
    CRM("Customer Relationship Management"),
    WMS("Warehouse Management System");

    private final String description;

    Systems(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
