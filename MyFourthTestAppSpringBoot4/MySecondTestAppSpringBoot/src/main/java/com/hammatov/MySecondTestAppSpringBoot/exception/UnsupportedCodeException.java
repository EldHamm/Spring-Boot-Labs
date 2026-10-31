package com.hammatov.MySecondTestAppSpringBoot.exception;

// Выбрасывается, когда в запросе пришло значение, которое сервис не поддерживает (uid = 123)
public class UnsupportedCodeException extends Exception {

    public UnsupportedCodeException(String message) {
        super(message);
    }

}
