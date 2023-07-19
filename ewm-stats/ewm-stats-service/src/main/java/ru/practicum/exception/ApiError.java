package ru.practicum.exception;

import lombok.Data;

import java.util.List;

@Data
public class ApiError extends Exception {
    private List<String> errors;

    private String message;

    private String reason;

    public ApiError(String message) {
        this.message = message;
    }
}
