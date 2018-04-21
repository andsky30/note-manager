package com.skiba.notesmanager.service.impl;

import com.skiba.notesmanager.api.dto.NoteDisplay;
import com.skiba.notesmanager.api.dto.PaginationInfo;
import com.skiba.notesmanager.api.service.SelectedNotesService;
import com.skiba.notesmanager.model.Note;
import com.skiba.notesmanager.repository.NoteRepository;
import com.skiba.notesmanager.service.mapper.NoteToNoteDisplayMapper;
import com.skiba.notesmanager.service.mapper.PaginationInfoToPageRequestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SelectedNotesServiceImpl implements SelectedNotesService {

    private NoteRepository noteRepository;
    private NoteToNoteDisplayMapper noteToNoteDisplayMapper;
    private PaginationInfoToPageRequestMapper paginationInfoToPageRequestMapper;

    @Autowired
    public SelectedNotesServiceImpl(NoteRepository noteRepository,
                                    NoteToNoteDisplayMapper noteToNoteDisplayMapper,
                                    PaginationInfoToPageRequestMapper paginationInfoToPageRequestMapper) {
        this.noteRepository = noteRepository;
        this.noteToNoteDisplayMapper = noteToNoteDisplayMapper;
        this.paginationInfoToPageRequestMapper = paginationInfoToPageRequestMapper;
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
