package com.skiba.notesmanager.api.dto;

import lombok.Getter;

@Getter
public class PaginationInfo {

    private int pageSize;
    private int pageOffset;
    private String sortDirection;
    private String sortColumn;
}
