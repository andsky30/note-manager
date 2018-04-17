package com.skiba.notesmanager.service.mapper;

import com.skiba.notesmanager.api.dto.NoteDisplay;
import com.skiba.notesmanager.model.Note;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@NoArgsConstructor
public class NoteToNoteDisplayMapper {

    public static final DateTimeFormatter LOCAL_DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public NoteDisplay map(Note note) {
        String dateOfCreation = localDateToStringFormatter(note.getDateOfCreation());
        String dateOfLastModification = localDateToStringFormatter(note.getDateOfLastModification());
        return new NoteDisplay(note.getId(),
                note.getTitle(),
                note.getContent(),
                dateOfCreation,
                dateOfLastModification);
    }

    private String localDateToStringFormatter(LocalDate localDate) {
        return localDate.format(LOCAL_DATE_FORMATTER);
    }
}
