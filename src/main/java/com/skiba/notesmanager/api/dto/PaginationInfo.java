package com.skiba.notesmanager.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PaginationInfo {

    private int pageSize;
    private int pageOffset;
    private String sortDirection;
    private String sortColumn;
}
