package com.skiba.notesmanager.api.service;

import com.skiba.notesmanager.api.dto.NoteDisplay;
import com.skiba.notesmanager.api.dto.PaginationInfo;

import java.util.List;

public interface SelectedNotesService {

    List<NoteDisplay> getNotesNotUpdatedForMoreThanMonth();

    List<NoteDisplay> getNotesWithSortingAndPagination(PaginationInfo paginationInfo);
}
