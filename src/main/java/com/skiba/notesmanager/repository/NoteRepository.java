package com.skiba.notesmanager.repository;

import com.skiba.notesmanager.model.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long>, PagingAndSortingRepository<Note, Long> {

    List<Note> findNotesByDateOfLastModificationBefore(LocalDateTime localDateTime);

    Page<Note> findAll(Pageable pageable);

}
