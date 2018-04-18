package com.skiba.notesmanager.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public enum Message {

    MESSAGE_AFTER_NOTE_BY_ID_DELETION ("Note with ID: %d has been deleted successfully!!!"),

    //exception messages
    NOTE_NOT_FOUND_MESSAGE("Could not found note with ID: %d");

    private final String message;

}
