package com.skiba.notesmanager.api.controller;

import com.skiba.notesmanager.api.dto.NoteDisplay;
import com.skiba.notesmanager.api.dto.PaginationInfo;
import com.skiba.notesmanager.data.TestDataLoader;
import com.skiba.notesmanager.model.Note;
import com.skiba.notesmanager.repository.NoteRepository;
import com.skiba.notesmanager.validation.ApiError;
import com.skiba.notesmanager.validation.ApiSubError;
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
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String SORT_DIRECTION_ASC = "asc";
    public static final String INVALID_SORT_DIRECTION_TYPE = "aggagahahgghasghahs";
    public static final String INVALID_SORT_COLUMN_NAME = "sdfsdfsdfsdf";

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
    public void shouldReturnOneNoteDisplayOnOnePage() {
        //given
        PaginationInfo paginationInfo = PaginationInfo.builder()
                .pageSize(1)
                .pageOffset(1)
                .sortColumn(COLUMN_NAME_TITLE)
                .sortDirection(SORT_DIRECTION_ASC)
                .build();
        HttpEntity<PaginationInfo> requestEntity = new HttpEntity<>(paginationInfo);

        //when
        ResponseEntity<List<NoteDisplay>> responseEntity = testRestTemplate
                .exchange("/api/notes/pagination",
                        HttpMethod.POST,
                        requestEntity,
                        new ParameterizedTypeReference<List<NoteDisplay>>() {
                        });

        //then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        //check expected note display
        List<NoteDisplay> returnedNotes = responseEntity.getBody();
        assertThat(returnedNotes.size()).isEqualTo(1);
        assertThat(returnedNotes.get(INDEX_0).getTitle()).isEqualTo(TestDataLoader.NOTE_TITLE_2);
    }

    @Test
    public void shouldReturnApiErrorBecauseOfInvalidSortDirectionType() {
        //given
        PaginationInfo paginationInfo = PaginationInfo.builder()
                .pageSize(3)
                .pageOffset(1)
                .sortColumn(COLUMN_NAME_TITLE)
                .sortDirection(INVALID_SORT_DIRECTION_TYPE)
                .build();
        HttpEntity<PaginationInfo> requestEntity = new HttpEntity<>(paginationInfo);

        //when
        ResponseEntity<ApiError> responseEntity = testRestTemplate
                .exchange("/api/notes/pagination",
                        HttpMethod.POST,
                        requestEntity,
                        ApiError.class);

        //then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        //check expected note display
        List<ApiSubError> errors = responseEntity.getBody().getErrors();
        assertThat(errors.size()).isEqualTo(1);
        assertThat(errors.get(INDEX_0).getErrorType()).isEqualTo(ErrorType.INVALID_SORT_DIRECTON_TYPE);
    }

    @Test
    public void shouldReturnApiErrorBecauseOfInvalidSortColumnName() {
        //given
        PaginationInfo paginationInfo = PaginationInfo.builder()
                .pageSize(1)
                .pageOffset(1)
                .sortColumn(INVALID_SORT_COLUMN_NAME)
                .sortDirection(SORT_DIRECTION_ASC)
                .build();
        HttpEntity<PaginationInfo> requestEntity = new HttpEntity<>(paginationInfo);

        //when
        ResponseEntity<ApiError> responseEntity = testRestTemplate
                .exchange("/api/notes/pagination",
                        HttpMethod.POST,
                        requestEntity,
                        ApiError.class);

        //then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        //check expected note display
        List<ApiSubError> errors = responseEntity.getBody().getErrors();
        assertThat(errors.size()).isEqualTo(1);
        assertThat(errors.get(INDEX_0).getErrorType()).isEqualTo(ErrorType.INVALID_SORT_COLUMN_NAME);
    }
}