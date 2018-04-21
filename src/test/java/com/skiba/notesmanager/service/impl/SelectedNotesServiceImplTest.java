package com.skiba.notesmanager.service.impl;

import com.skiba.notesmanager.api.dto.NoteDisplay;
import com.skiba.notesmanager.api.service.SelectedNotesService;
import com.skiba.notesmanager.model.Note;
import com.skiba.notesmanager.repository.NoteRepository;
import com.skiba.notesmanager.service.mapper.NoteToNoteDisplayMapper;
import com.skiba.notesmanager.service.mapper.PaginationInfoToPageRequestMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SelectedNotesServiceImplTest {

    private static final String NOTE_TITLE_1 = "Title1";
    private static final String NOTE_CONTENT_1 = "NoteText 1";
    private static final String NOTE_TITLE_2 = "Title2";
    private static final String NOTE_CONTENT_2 = "NoteText 2";
    private static final Long NOTE_ID_1 = 1L;
    private static final Long NOTE_ID_2 = 2L;
    private static final LocalDateTime DATE_OF_LAST_MODIFICATION_MORE_THAN_MONTH_AGO = LocalDateTime.of(2017, 12, 12, 12, 12);
    private static final int NOTES_NOT_UPDATED_SIZE_1 = 1;

    @Mock
    private NoteRepository noteRepository;

    private static PaginationInfoToPageRequestMapper paginationInfoToPageRequestMapper = new PaginationInfoToPageRequestMapper();
    private static NoteToNoteDisplayMapper noteToNoteDisplayMapper = new NoteToNoteDisplayMapper();

    @InjectMocks
    private SelectedNotesService selectedNotesService = new SelectedNotesServiceImpl(noteRepository,
            noteToNoteDisplayMapper, paginationInfoToPageRequestMapper);

    private static List<Note> notes;
    private static Note note1;
    private static Note note2;

    @Before
    public void prepareNoteList() {
        note1 = Note.builder()
                .id(NOTE_ID_1)
                .title(NOTE_TITLE_1)
                .content(NOTE_CONTENT_1)
                .dateOfCreation(LocalDateTime.now())
                .dateOfLastModification(DATE_OF_LAST_MODIFICATION_MORE_THAN_MONTH_AGO)
                .build();
        note2 = Note.builder()
                .id(NOTE_ID_2)
                .title(NOTE_TITLE_2)
                .content(NOTE_CONTENT_2)
                .dateOfCreation(LocalDateTime.now())
                .dateOfLastModification(LocalDateTime.now())
                .build();
        notes = new ArrayList<>();
        notes.add(note1);
        notes.add(note2);
    }

    @After
    public void clearNotesList() {
        notes.clear();
    }

    @Test
    public void shouldReturnOnlyNotesModifiedMoreThanMothAgo() {
        //given
        when(noteRepository.findNotesByDateOfLastModificationBefore(any(LocalDateTime.class)))
                .thenReturn(notes.stream()
                        .filter(n -> n.getDateOfLastModification().isBefore(LocalDateTime.now().minusMonths(1)))
                        .collect(Collectors.toCollection(ArrayList::new)));

        //when
        List<NoteDisplay> notesNotUpdatedForMoreThanMonth = selectedNotesService.getNotesNotUpdatedForMoreThanMonth();

        //then
        assertThat(notesNotUpdatedForMoreThanMonth).hasSize(NOTES_NOT_UPDATED_SIZE_1);
    }
}