package com.skiba.notesmanager.api.controller;

import com.skiba.notesmanager.api.dto.NoteCreation;
import com.skiba.notesmanager.api.dto.NoteDisplay;
import com.skiba.notesmanager.data.TestDataLoader;
import com.skiba.notesmanager.model.Note;
import com.skiba.notesmanager.repository.NoteRepository;
import com.skiba.notesmanager.validation.ApiError;
import com.skiba.notesmanager.validation.ErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class NoteControllerTest extends AbstractTestNGSpringContextTests {

    private static final int INITIAL_NOTES_IN_DB_SIZE_2 = 2;
    private static final int INDEX_0 = 0;
    private static final Long NOT_EXISTING_NOTE_ID = 234L;
    private static final String NOTE_TITLE_3 = "Title3";
    private static final String NOTE_CONTENT_3 = "NOte text 3";
    private static final long NOTE_ID_3 = 3L;
    private static final String BLANK_TEXT = "      ";

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

    @Test
    public void shouldReturnAllUsers() {
        //given

        //when
        ResponseEntity<List<NoteDisplay>> responseEntity = testRestTemplate
                .exchange("/api/notes",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<NoteDisplay>>() {
                        });

        //then
        //check response
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        //check user list
        List<NoteDisplay> returnedUsers = responseEntity.getBody();
        assertThat(returnedUsers).isNotNull();
        assertThat(returnedUsers.size()).isEqualTo(INITIAL_NOTES_IN_DB_SIZE_2);
    }

    @Test
    public void shouldReturnSingleNoteById() {
        //given

        //when
        ResponseEntity<NoteDisplay> responseEntity = testRestTemplate
                .exchange("/api/notes/" + TestDataLoader.NOTE_ID_2,
                        HttpMethod.GET,
                        null,
                        NoteDisplay.class);

        //then
        //check response
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        //check single note
        NoteDisplay returnedNote = responseEntity.getBody();
        assertThat(returnedNote).isNotNull();
        assertThat(returnedNote.getContent()).isEqualTo(TestDataLoader.NOTE_CONTENT_2);
    }

    @Test
    public void shouldReturnNotFoundStatusWhenNoteIdIsInvalid() {

        //given

        //when
        ResponseEntity<ApiError> responseEntity = testRestTemplate
                .getForEntity("/api/notes/" + NOT_EXISTING_NOTE_ID,
                        ApiError.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody().getErrors().get(INDEX_0).getErrorType()).isEqualTo(ErrorType.NOTE_NOT_FOUND);
    }

    @Test
    public void shouldAddOneNote() {
        //given
        NoteCreation noteCreationToAdd = new NoteCreation(NOTE_TITLE_3, NOTE_CONTENT_3);
        HttpEntity<NoteCreation> requestEntity = new HttpEntity<>(noteCreationToAdd);

        //when
        ResponseEntity<NoteDisplay> responseEntity = testRestTemplate
                .exchange("/api/notes",
                        HttpMethod.POST,
                        requestEntity,
                        NoteDisplay.class);

        //then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        NoteDisplay noteDisplay = responseEntity.getBody();
        assertThat(noteDisplay.getContent()).isEqualTo(NOTE_CONTENT_3);
        assertThat(noteRepository.findAll()).hasSize(INITIAL_NOTES_IN_DB_SIZE_2 + 1);

        //remove added note to not mess with another tests
        noteRepository.deleteById(noteDisplay.getId());
    }

    @Test
    public void shouldNotAllowToAddNoteWithoutContent() {
        //given
        NoteCreation noteToAdd = new NoteCreation(NOTE_TITLE_3, null);
        HttpEntity<NoteCreation> requestEntity = new HttpEntity<>(noteToAdd);

        //when
        ResponseEntity<ApiError> responseEntity = testRestTemplate
                .exchange("/api/notes",
                        HttpMethod.POST,
                        requestEntity,
                        ApiError.class);

        //then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody().getErrors().get(INDEX_0).getErrorType()).isEqualTo(ErrorType.EMPTY_NOTE_CONTENT);
    }

    @Test
    public void shouldNotAllowToAddNoteWithBlankContent() {
        //given
        NoteCreation noteToAdd = new NoteCreation(NOTE_TITLE_3, BLANK_TEXT);
        HttpEntity<NoteCreation> requestEntity = new HttpEntity<>(noteToAdd);

        //when
        ResponseEntity<ApiError> responseEntity = testRestTemplate
                .exchange("/api/notes",
                        HttpMethod.POST,
                        requestEntity,
                        ApiError.class);

        //then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody().getErrors().get(INDEX_0).getErrorType()).isEqualTo(ErrorType.EMPTY_NOTE_CONTENT);
    }

    @Test
    public void shouldRemoveOneNote() {
        //given
        Optional<Note> noteToRemoveOptional = noteRepository.findById(TestDataLoader.NOTE_ID_1);
        Note noteToRemove = noteToRemoveOptional.get();

        //when
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange("/api/notes/" + TestDataLoader.NOTE_ID_1,
                        HttpMethod.DELETE,
                        null,
                        String.class);

        //then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(noteRepository.findAll()).hasSize(INITIAL_NOTES_IN_DB_SIZE_2 - 1);

        //add removed note to not mess with other test
        noteRepository.save(noteToRemove);
    }

    @Test
    public void shouldReturnNotFoundStatusWhenNoteToRemoveIdIsInvalid() {
        //given

        //when
        ResponseEntity<ApiError> responseEntity = testRestTemplate
                .exchange("/api/notes/" + NOT_EXISTING_NOTE_ID,
                        HttpMethod.DELETE,
                        null,
                        ApiError.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody().getErrors().get(INDEX_0).getErrorType()).isEqualTo(ErrorType.NOTE_NOT_FOUND);
    }

    @Test
    public void shouldUpdateOneNote() {
        //given
        NoteCreation updatedNoteCretion = new NoteCreation(NOTE_TITLE_3, NOTE_CONTENT_3);
        HttpEntity<NoteCreation> requestEntity = new HttpEntity<>(updatedNoteCretion);

        //when
        ResponseEntity<NoteDisplay> responseEntity = testRestTemplate
                .exchange("/api/notes/" + TestDataLoader.NOTE_ID_2,
                        HttpMethod.PUT,
                        requestEntity,
                        NoteDisplay.class);

        //then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        NoteDisplay returnedNoteDisplay = responseEntity.getBody();
        assertThat(returnedNoteDisplay.getTitle()).isEqualTo(NOTE_TITLE_3);
        assertThat(returnedNoteDisplay.getContent()).isEqualTo(NOTE_CONTENT_3);
        assertThat(returnedNoteDisplay.getId()).isNotNull();

        //revert changes
        Optional<Note> changedNoteOptional = noteRepository.findById(TestDataLoader.NOTE_ID_2);
        Note changedNote = changedNoteOptional.get();
        changedNote.setTitle(TestDataLoader.NOTE_TITLE_2);
        changedNote.setContent(TestDataLoader.NOTE_CONTENT_2);
        noteRepository.save(changedNote);
    }

    @Test
    public void shouldReturnNotFoundStatusWhenNoteToUpdateIdIsInvalid() {
        //given
        NoteCreation updatedNoteCretion = new NoteCreation(NOTE_TITLE_3, NOTE_CONTENT_3);
        HttpEntity<NoteCreation> requestEntity = new HttpEntity<>(updatedNoteCretion);

        //when
        ResponseEntity<ApiError> responseEntity = testRestTemplate
                .exchange("/api/notes/" + NOT_EXISTING_NOTE_ID,
                        HttpMethod.PUT,
                        requestEntity,
                        ApiError.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody().getErrors().get(INDEX_0).getErrorType()).isEqualTo(ErrorType.NOTE_NOT_FOUND);
    }
}