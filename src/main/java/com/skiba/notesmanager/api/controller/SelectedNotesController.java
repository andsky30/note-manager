package com.skiba.notesmanager.api.controller;

import com.skiba.notesmanager.api.dto.NoteDisplay;
import com.skiba.notesmanager.api.dto.PaginationInfo;
import com.skiba.notesmanager.api.service.SelectedNotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SelectedNotesController {

    private SelectedNotesService selectedNotesService;

    @Autowired
    public SelectedNotesController(SelectedNotesService selectedNotesService) {
        this.selectedNotesService = selectedNotesService;
    }

    @GetMapping(value = "/api/notes/not_updated")
    public ResponseEntity<List<NoteDisplay>> getNotUpdatedNotes(){
        return ResponseEntity.ok(selectedNotesService.getNotesNotUpdatedForMoreThanMonth());
    }

    @PostMapping(value = "/api/notes/pagination")
    public ResponseEntity<List<NoteDisplay>> getPaginatedNotes(@RequestBody PaginationInfo paginationInfo){
        return ResponseEntity.ok(selectedNotesService.getNotesWithSortingAndPagination(paginationInfo));
    }
}
