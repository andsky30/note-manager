package com.skiba.notesmanager.api.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NoteCreation {

    @NotBlank(message = "Note title cannot be blank!")
    private String title;
    @NotBlank(message = "Note content cannot be blank!")
    private String content;

}
