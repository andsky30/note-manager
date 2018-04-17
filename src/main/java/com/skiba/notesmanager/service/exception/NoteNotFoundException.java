package com.skiba.notesmanager.service.exception;

import com.skiba.notesmanager.message.Message;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoteNotFoundException extends RuntimeException {

    public NoteNotFoundException(Long noteId) {
        super(String.format(Message.NOTE_NOT_FOUND_MESSAGE.getMessage(), noteId));
    }
}
