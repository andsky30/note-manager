package com.skiba.notesmanager.api.controller;

import com.skiba.notesmanager.api.dto.NoteCreation;
import com.skiba.notesmanager.api.dto.NoteDisplay;
import com.skiba.notesmanager.api.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static com.skiba.notesmanager.api.message.Message.*;

@RestController
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @GetMapping(value = "/api/notes")
    public ResponseEntity<List<NoteDisplay>> getAllNotes(){
        return ResponseEntity.ok(noteService.gettAllNotes());
    }

    @GetMapping(value = "/api/notes/{noteId}")
    public ResponseEntity<NoteDisplay> getSingleNote(@PathVariable Long noteId){
        return ResponseEntity.ok(noteService.getSingleNote(noteId));
    }

    @PostMapping(value = "/api/notes")
    public ResponseEntity<NoteDisplay> addNote(@RequestBody @Valid NoteCreation noteCreation) {
        NoteDisplay savedNote = noteService.addNote(noteCreation);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedNote.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedNote);
    }

    @DeleteMapping(value = "/api/notes/{noteId}")
    public ResponseEntity<?> deleteNote(@PathVariable Long noteId) {
        noteService.removeNote(noteId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(String.format(MESSAGE_AFTER_NOTE_BY_ID_DELETION, noteId));
    }

    @PutMapping(value = "/api/notes/{noteId}")
    public ResponseEntity<NoteDisplay> updateNote(@RequestBody NoteCreation noteCreation,
                                                  @PathVariable Long noteId) {
        NoteDisplay updatedNoteDisplay = noteService.updateNote(noteCreation, noteId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedNoteDisplay);
    }


}
