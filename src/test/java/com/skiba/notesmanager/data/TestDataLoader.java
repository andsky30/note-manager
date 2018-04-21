package com.skiba.notesmanager.data;

import com.skiba.notesmanager.api.dto.NoteCreation;
import com.skiba.notesmanager.model.Note;
import com.skiba.notesmanager.repository.NoteRepository;
import com.skiba.notesmanager.service.mapper.NoteCreationToNoteMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
@NoArgsConstructor
@AllArgsConstructor
public class TestDataLoader implements ApplicationRunner {

    public static final String NOTE_TITLE_1 = "Title1";
    public static final String NOTE_CONTENT_1 = "NoteText 1";
    public static final String NOTE_TITLE_2 = "Title2";
    public static final String NOTE_CONTENT_2 = "NoteText 2";
    public static final int INITIAL_NOTES_SIZE = 2;
    public static final Long NOTE_ID_1 = 1L;
    public static final Long NOTE_ID_2 = 2L;


    @Autowired
    private NoteRepository noteRepository;

    private NoteCreationToNoteMapper noteCreationToNoteMapper = new NoteCreationToNoteMapper();

    @Override
    public void run(ApplicationArguments args) throws Exception {

    }

    public void addTwoNotes() {
        Note note1 = noteCreationToNoteMapper.map(new NoteCreation(NOTE_TITLE_1, NOTE_CONTENT_1));
        Note note2 = noteCreationToNoteMapper.map(new NoteCreation(NOTE_TITLE_2, NOTE_CONTENT_2));
        note1.setId(NOTE_ID_1);
        note2.setId(NOTE_ID_2);
        noteRepository.save(note1);
        noteRepository.save(note2);
    }
}
