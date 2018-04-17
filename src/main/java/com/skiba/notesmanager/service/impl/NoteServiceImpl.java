package com.skiba.notesmanager.service.impl;

import com.skiba.notesmanager.api.dto.NoteCreation;
import com.skiba.notesmanager.api.dto.NoteDisplay;
import com.skiba.notesmanager.api.dto.PaginationInfo;
import com.skiba.notesmanager.api.service.NoteService;
import com.skiba.notesmanager.model.Note;
import com.skiba.notesmanager.repository.NoteRepository;
import com.skiba.notesmanager.service.exception.NoteNotFoundException;
import com.skiba.notesmanager.service.mapper.NoteCreationToNoteMapper;
import com.skiba.notesmanager.service.mapper.NoteToNoteDisplayMapper;
import com.skiba.notesmanager.service.mapper.PaginationInfoToPageRequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final NoteToNoteDisplayMapper noteToNoteDisplayMapper;
    private final NoteCreationToNoteMapper noteCreationToNoteMapper;
    private final PaginationInfoToPageRequestMapper paginationInfoToPageRequestMapper;

    @Override
    public List<NoteDisplay> gettAllNotes() {
        return noteRepository.findAll().stream()
                .map(noteToNoteDisplayMapper::map)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public NoteDisplay getSingleNote(Long noteId) {
        Optional<Note> noteOptional = noteRepository.findById(noteId);
        if (!noteOptional.isPresent()) {
            throw new NoteNotFoundException(noteId);
        } else {
            return noteToNoteDisplayMapper.map(noteOptional.get());
        }
    }

    @Override
    public NoteDisplay addNote(NoteCreation noteCreation) {
        Note noteToSave = noteCreationToNoteMapper.map(noteCreation);
        Note savedNote = noteRepository.save(noteToSave);
        return noteToNoteDisplayMapper.map(savedNote);
    }

    @Override
    public void removeNote(Long noteId) {
        Optional<Note> noteOptional = noteRepository.findById(noteId);
        if (!noteOptional.isPresent()) {
            throw new NoteNotFoundException(noteId);
        } else {
            noteRepository.delete(noteOptional.get());
        }
    }

    @Override
    public NoteDisplay updateNote(NoteCreation noteCreation, Long noteId) {
        Optional<Note> noteOptional = noteRepository.findById(noteId);
        if (!noteOptional.isPresent()) {
            throw new NoteNotFoundException(noteId);
        } else {
            Note oldNote = noteOptional.get();
            updateNoteAttributes(noteCreation, oldNote);
            Note savedNote = noteRepository.save(oldNote);
            return noteToNoteDisplayMapper.map(savedNote);
        }
    }

    private void updateNoteAttributes(NoteCreation noteCreation, Note oldNote) {
        oldNote.setTitle(noteCreation.getTitle());
        oldNote.setContent(noteCreation.getContent());
        oldNote.setDateOfLastModification(LocalDateTime.now());
    }

    @Override
    public List<NoteDisplay> getNotesNotUpdatedForMoreThanMonth() {
        List<Note> notUpdatedNotes = noteRepository
                .findNotesByDateOfLastModificationBefore(LocalDateTime.now().minusMonths(1));
        return notUpdatedNotes.stream()
                .map(noteToNoteDisplayMapper::map)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<NoteDisplay> getNotesWithSortingAndPagination(PaginationInfo paginationInfo) {
        PageRequest pageRequest = paginationInfoToPageRequestMapper.map(paginationInfo);
        List<Note> notes = noteRepository.findAll(pageRequest).getContent();
        return notes.stream()
                .map(noteToNoteDisplayMapper::map)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
