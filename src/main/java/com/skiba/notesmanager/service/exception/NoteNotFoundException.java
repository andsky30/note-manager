package com.skiba.notesmanager.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoteNotFoundException extends RuntimeException {

    private static final String NOTE_NOT_FOUND_MESSAGE = "Could not found note with ID: %d";

    public NoteNotFoundException(Long noteId) {
        super(String.format(NOTE_NOT_FOUND_MESSAGE,noteId));
    }
}
