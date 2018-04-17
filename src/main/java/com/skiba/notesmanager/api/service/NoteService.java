package com.skiba.notesmanager.api.service;

import com.skiba.notesmanager.api.dto.NoteCreation;
import com.skiba.notesmanager.api.dto.NoteDisplay;

import java.util.List;

public interface NoteService {

    List<NoteDisplay> gettAllNotes();

    NoteDisplay getSingleNoteById(Long noteId);

    NoteDisplay addNote(NoteCreation noteCreation);

    void removenoteById(Long noteId);

    NoteDisplay updateNote(NoteCreation noteCreation, Long noteId);

}
