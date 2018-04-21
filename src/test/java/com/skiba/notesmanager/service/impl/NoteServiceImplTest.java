package com.skiba.notesmanager.service.impl;

import com.skiba.notesmanager.api.dto.NoteCreation;
import com.skiba.notesmanager.api.dto.NoteDisplay;
import com.skiba.notesmanager.api.service.NoteService;
import com.skiba.notesmanager.model.Note;
import com.skiba.notesmanager.repository.NoteRepository;
import com.skiba.notesmanager.service.mapper.NoteCreationToNoteMapper;
import com.skiba.notesmanager.service.mapper.NoteToNoteDisplayMapper;
import com.skiba.notesmanager.validation.exceptions.NoteNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NoteServiceImplTest {

    private static final String NOTE_TITLE_1 = "Title1";
    private static final String NOTE_CONTENT_1 = "NoteText 1";
    private static final String NOTE_TITLE_2 = "Title2";
    private static final String NOTE_CONTENT_2 = "NoteText 2";
    private static final int INITIAL_NOTES_SIZE = 2;
    private static final Long NOTE_ID_1 = 1L;
    private static final Long NOTE_ID_2 = 2L;
    private static final Long NOT_EXISTING_NOTE_ID = 234L;
    private static final String NOTE_TITLE_3 = "Title3";
    private static final String NOTE_CONTENT_3 = "NOte text 3";
    private static final long NOTE_ID_3 = 3L;

    @Mock
    private NoteRepository noteRepository;

    private static NoteToNoteDisplayMapper noteToNoteDisplayMapper = new NoteToNoteDisplayMapper();
    private static NoteCreationToNoteMapper noteCreationToNoteMapper = new NoteCreationToNoteMapper();

    @InjectMocks
    private NoteService noteService = new NoteServiceImpl(noteRepository, noteToNoteDisplayMapper, noteCreationToNoteMapper);

    private static List<Note> notes;
    private static Note note1;
    private static Note note2;

    @Before
    public void prepareNoteList() {
        note1 = noteCreationToNoteMapper.map(new NoteCreation(NOTE_TITLE_1, NOTE_CONTENT_1));
        note2 = noteCreationToNoteMapper.map(new NoteCreation(NOTE_TITLE_2, NOTE_CONTENT_2));
        note1.setId(NOTE_ID_1);
        note2.setId(NOTE_ID_2);
        notes = new ArrayList<>();
        notes.add(note1);
        notes.add(note2);
    }

    @After
    public void clearNotesList() {
        notes.clear();
    }

    @Test
    public void shouldReturnTwoNoteDisplays() {
        //given
        when(noteRepository.findAll()).thenReturn(notes);

        //when
        List<NoteDisplay> noteDisplays = noteService.gettAllNotes();

        //then
        assertThat(noteDisplays).isNotEmpty();
        assertThat(noteDisplays).hasSize(INITIAL_NOTES_SIZE);

        Optional<NoteDisplay> note2Optional = noteDisplays.stream()
                .filter(n -> n.getId().equals(NOTE_ID_2))
                .findFirst();
        assertThat(note2Optional.isPresent()).isTrue();
        assertThat(note2Optional.get().getTitle()).isEqualTo(NOTE_TITLE_2);
        assertThat(note2Optional.get().getContent()).isEqualTo(NOTE_CONTENT_2);
    }

    @Test
    public void shouldReturnOneNote() {
        //given
        when(noteRepository.findById(NOTE_ID_1)).thenReturn(Optional.of(note1));

        //when
        NoteDisplay singleNote = noteService.getSingleNote(NOTE_ID_1);

        //then
        assertThat(singleNote).isNotNull();
        assertThat(singleNote.getId()).isEqualTo(NOTE_ID_1);
        assertThat(singleNote.getContent()).isEqualTo(NOTE_CONTENT_1);
    }

    @Test(expected = NoteNotFoundException.class)
    public void shouldReturnNotFoundExceptionWhenInvalidNoteId() {

        //when
        noteService.getSingleNote(NOT_EXISTING_NOTE_ID);
    }

    @Test
    public void shouldAddOneNote() {
        //given
        NoteCreation noteToAdd = new NoteCreation(NOTE_TITLE_3, NOTE_CONTENT_3);
        Note noteEntity = noteCreationToNoteMapper.map(noteToAdd);
        noteEntity.setId(NOTE_ID_3);
        when(noteRepository.save(any(Note.class))).thenReturn(addNoteToList(noteEntity));

        //when
        NoteDisplay addedNoteDisplay = noteService.addNote(noteToAdd);

        //then
        assertThat(notes).hasSize(INITIAL_NOTES_SIZE + 1);
        assertThat(addedNoteDisplay).isNotNull();
        assertThat(addedNoteDisplay.getContent()).isEqualTo(NOTE_CONTENT_3);

    }

    private Note addNoteToList(Note note) {
        notes.add(note);
        return note;
    }

    @Test
    public void shouldUpdateNote() {
        //given
        String updatedNoteTitle = NOTE_TITLE_3;
        NoteCreation updatedNoteCreation = new NoteCreation(updatedNoteTitle, NOTE_CONTENT_2);

        when(noteRepository.findById(NOTE_ID_2)).thenReturn(Optional.of(note2));
        when(noteRepository.save(any(Note.class))).thenReturn(note2);

        //when
        NoteDisplay updatedNoteDisplay = noteService.updateNote(updatedNoteCreation, NOTE_ID_2);

        //then
        assertThat(updatedNoteDisplay).isNotNull();
        assertThat(updatedNoteDisplay.getTitle()).isEqualTo(NOTE_TITLE_3);
    }

    @Test
    public void shouldRemoveOneNote() {
        //given
        when(noteRepository.findById(NOTE_ID_1)).thenReturn(Optional.of(note1));
        doAnswer(a -> removeNoteFromList(note1)).when(noteRepository).delete(note1);

        //when
        noteService.removeNote(NOTE_ID_1);

        //then
        verify(noteRepository, times(1)).delete(any(Note.class));
        assertThat(notes).hasSize(INITIAL_NOTES_SIZE - 1);
    }

    private Object removeNoteFromList(Note note) {
        notes.remove(note);
        return null;
    }

    @Test(expected = NoteNotFoundException.class)
    public void shouldReturnNotFoundExceptionWhenInvalidNoteIdWhileRemoving() {

        //when
        noteService.removeNote(NOT_EXISTING_NOTE_ID);
    }
}