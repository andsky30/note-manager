package com.skiba.notesmanager.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NoteCreation {

    @NotBlank(message = "Note title cannot be blank!")
    private String title;
    @NotBlank(message = "Note content  cannot be blank!")
    private String content;

}
