package com.skiba.notesmanager.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
@Entity
@Table(name = "note")
public class Note {

    @Id
    @GeneratedValue
    @NotNull
    @Column(name = "note_id")
    private Long id;
    @NotNull
    @NonNull
    private String title;
    @NotNull
    @NonNull
    private String content;
    @NotNull
    @NonNull
    @PastOrPresent
    private LocalDate dateOfCreation;
    @NotNull
    @NonNull
    @PastOrPresent
    private LocalDate dateOfLastModification;
}
