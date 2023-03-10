package com.hayrolr.spring.notes.repository;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hayrolr.spring.notes.entity.Note;
@Repository
@Transactional
public interface NoteRepository extends JpaRepository<Note, Integer> {
  Page<Note> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);

}
