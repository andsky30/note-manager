package com.skiba.notesmanager.service.mapper;

import com.skiba.notesmanager.api.dto.NoteCreation;
import com.skiba.notesmanager.model.Note;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@NoArgsConstructor
public class NoteCreationToNoteMapper {

    public Note map(NoteCreation noteCreation) {
        return new Note(noteCreation.getTitle(),
                noteCreation.getContent(),
                LocalDate.now(),
                LocalDate.now());
    }
}
