package com.skiba.notesmanager.api.controller;

import com.skiba.notesmanager.api.dto.NoteCreation;
import com.skiba.notesmanager.api.dto.NoteDisplay;
import com.skiba.notesmanager.api.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

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
}
