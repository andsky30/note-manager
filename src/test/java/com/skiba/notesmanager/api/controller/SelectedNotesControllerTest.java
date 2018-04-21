package com.skiba.notesmanager.api.controller;

import com.skiba.notesmanager.api.dto.NoteDisplay;
import com.skiba.notesmanager.data.TestDataLoader;
import com.skiba.notesmanager.model.Note;
import com.skiba.notesmanager.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class SelectedNotesControllerTest extends AbstractTestNGSpringContextTests {

    private static final LocalDateTime DATE_OF_LAST_MODIFICATION_MORE_THAN_MONTH_AGO = LocalDateTime.of(2017, 01, 01, 01, 01, 01);
    private static final int INDEX_0 = 0;

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private TestDataLoader testDataLoader;

    @BeforeClass
    public void initializeDbWithTwoNotes() {
        testDataLoader.addTwoNotes();
    }

    @AfterClass
    public void cleanUp() {
        noteRepository.deleteAll();
    }

    @Test
    public void shouldReturnOnlyNotesModifiedMoreThanMonthAgo() {
        //given
        Optional<Note> noteOptional = noteRepository.findById(TestDataLoader.NOTE_ID_1);
        Note note = noteOptional.get();
        note.setDateOfLastModification(DATE_OF_LAST_MODIFICATION_MORE_THAN_MONTH_AGO);
        noteRepository.save(note);

        //when
        ResponseEntity<List<NoteDisplay>> responseEntity = testRestTemplate
                .exchange("/api/notes/not_updated",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<NoteDisplay>>() {
                        });

        //then
        //check response
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        //check user list
        List<NoteDisplay> returnedNotes = responseEntity.getBody();
        assertThat(returnedNotes).isNotNull();
        assertThat(returnedNotes.size()).isEqualTo(TestDataLoader.INITIAL_NOTES_IN_DB_SIZE_2 - 1);
        assertThat(returnedNotes.get(INDEX_0).getId()).isEqualTo(TestDataLoader.NOTE_ID_1);

        //revert changes
        note.setDateOfLastModification(LocalDateTime.now());
        noteRepository.save(note);
    }

    @Test
    public void name() {
        //given

        //when

        //then

    }
}