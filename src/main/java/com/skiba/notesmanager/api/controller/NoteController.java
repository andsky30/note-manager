package com.skiba.notesmanager.api.controller;

import com.skiba.notesmanager.api.dto.NoteCreation;
import com.skiba.notesmanager.api.dto.NoteDisplay;
import com.skiba.notesmanager.util.Message;
import com.skiba.notesmanager.api.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class NoteController {

    private NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

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
        String message = String.format(Message.MESSAGE_AFTER_NOTE_BY_ID_DELETION.getMessage(), noteId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(message);
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
