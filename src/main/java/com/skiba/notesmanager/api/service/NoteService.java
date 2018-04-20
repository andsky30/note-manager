package com.skiba.notesmanager.api.service;

import com.skiba.notesmanager.api.dto.NoteCreation;
import com.skiba.notesmanager.api.dto.NoteDisplay;
import com.skiba.notesmanager.api.dto.PaginationInfo;

import java.util.List;

public interface NoteService {

    List<NoteDisplay> gettAllNotes();

    NoteDisplay getSingleNote(Long noteId);

    NoteDisplay addNote(NoteCreation noteCreation);

    void removeNote(Long noteId);

    NoteDisplay updateNote(NoteCreation noteCreation, Long noteId);


}
