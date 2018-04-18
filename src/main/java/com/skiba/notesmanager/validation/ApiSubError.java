package com.skiba.notesmanager.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ApiSubError {

    private String message;
    private ErrorType errorType;
}
