package com.skiba.notesmanager.service.impl;

import com.skiba.notesmanager.api.dto.NoteCreation;
import com.skiba.notesmanager.api.dto.NoteDisplay;
import com.skiba.notesmanager.api.service.NoteService;
import com.skiba.notesmanager.model.Note;
import com.skiba.notesmanager.repository.NoteRepository;
import com.skiba.notesmanager.service.mapper.NoteCreationToNoteMapper;
import com.skiba.notesmanager.service.mapper.NoteToNoteDisplayMapper;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final NoteToNoteDisplayMapper noteToNoteDisplayMapper;
    private final NoteCreationToNoteMapper noteCreationToNoteMapper;

    @Override
    public List<NoteDisplay> gettAllNotes() {
        return null;
    }

    @Override
    public NoteDisplay getSingleNoteById(Long noteId) {
        return null;
    }

    @Override
    public NoteDisplay addNote(NoteCreation noteCreation) {
        Note noteToSave = noteCreationToNoteMapper.map(noteCreation);
        Note savedNote = noteRepository.save(noteToSave);
        return noteToNoteDisplayMapper.map(savedNote);
    }

    @Override
    public void removenoteById(Long noteId) {

    }

    @Override
    public NoteDisplay updateNote(NoteCreation noteCreation, Long noteId) {
        return null;
    }
}
