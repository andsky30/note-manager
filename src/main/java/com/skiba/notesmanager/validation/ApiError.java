package com.skiba.notesmanager.validation;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Getter
public class ApiError {

    private HttpStatus httpStatus;
    private LocalDateTime timestamp;
    private List<ApiSubError> errors;

    public ApiError(){
        this.timestamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus httpStatus, List<ApiSubError> errors) {
        this();
        this.httpStatus = httpStatus;
        this.errors = errors;
    }

    public ApiError(HttpStatus httpStatus, ApiSubError subError) {
        this();
        this.httpStatus = httpStatus;
        this.errors = Arrays.asList(subError);
    }

}
