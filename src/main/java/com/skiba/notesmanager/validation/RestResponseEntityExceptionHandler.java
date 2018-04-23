package com.skiba.notesmanager.validation;

import com.skiba.notesmanager.validation.exceptions.NoteNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseBody
    @ExceptionHandler(NoteNotFoundException.class)
    public ResponseEntity <Object>handleNoteNotFoundException(NoteNotFoundException ex){
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, new ApiSubError(ex.getMessage(), ErrorType.NOTE_NOT_FOUND));
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        List<ApiSubError> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(new ApiSubError(
                    error.getField() + ": " + error.getDefaultMessage(),
                    resolveErrorType(error.getField())));
        }
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, errors);
        ResponseEntity<Object> objectResponseEntity = handleExceptionInternal(
                ex, apiError, headers, apiError.getHttpStatus(), request);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    private ErrorType resolveErrorType(String errorField) {
        ErrorType errorType = null;
        switch (errorField) {
            case "title":
                errorType =  ErrorType.EMPTY_NOTE_TITLE;
                break;
            case "content":
                errorType =  ErrorType.EMPTY_NOTE_CONTENT;
                break;
            case "sortDirection":
                errorType =  ErrorType.INVALID_SORT_DIRECTON_TYPE;
                break;
            case "sortColumn":
                errorType =  ErrorType.INVALID_SORT_COLUMN_NAME;
                break;
        }
        return errorType;
    }
}
