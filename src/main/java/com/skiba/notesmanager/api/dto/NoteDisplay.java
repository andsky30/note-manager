package com.skiba.notesmanager.api.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class NoteDisplay {

    private Long id;
    private String title;
    private String content;
    private String dateOfCreation;
    private String dateOfLastModification;
}


