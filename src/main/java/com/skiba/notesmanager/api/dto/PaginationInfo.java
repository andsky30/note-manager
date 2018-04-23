package com.skiba.notesmanager.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
public class PaginationInfo {

    @NotNull(message = "Page size cannot be null!")
    private int pageSize;
    @NotNull(message = "Page offset cannot be null!")
    private int pageOffset;
    @NotNull(message = "Sort column cannot be null!")
    @Pattern(regexp = "^(id|title|content|dateOfCreation|dateOfLastModification)$", message = "Invalid sort column!")
    private String sortColumn;
    @Pattern(regexp = "^(asc|desc)$", message = "Invalid sort direction! Should be: 'asc' or 'desc'")
    @NotNull(message = "Sort direction type cannot be null!")
    private String sortDirection;
}
