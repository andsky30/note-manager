package com.skiba.notesmanager.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@NoArgsConstructor
@RequiredArgsConstructor
@Setter @Getter
@Entity
@Table(name = "note")
public class Note {

    @Id
    @GeneratedValue
    @NotNull
    @Column(name = "note_id")
    @Setter(AccessLevel.NONE)
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
    @Setter(AccessLevel.NONE)
    private LocalDateTime dateOfCreation;
    @NotNull
    @NonNull
    @PastOrPresent
    private LocalDateTime dateOfLastModification;
}
