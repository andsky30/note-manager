package com.skiba.notesmanager.service.mapper;

import com.skiba.notesmanager.api.dto.NoteDisplay;
import com.skiba.notesmanager.model.Note;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@NoArgsConstructor
public class NoteToNoteDisplayMapper {

    public static final DateTimeFormatter LOCAL_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public NoteDisplay map(Note note) {
        String dateOfCreation = localDateTimeToStringFormatter(note.getDateOfCreation());
        String dateOfLastModification = localDateTimeToStringFormatter(note.getDateOfLastModification());
        return new NoteDisplay(note.getId(),
                note.getTitle(),
                note.getContent(),
                dateOfCreation,
                dateOfLastModification);
    }

    private String localDateTimeToStringFormatter(LocalDateTime localDateTime) {
        return localDateTime.format(LOCAL_DATE_TIME_FORMATTER);
    }
}
