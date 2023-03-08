package com.hayrolr.spring.notes.repository;

import java.util.List;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hayrolr.spring.notes.entity.Note;
@Repository
@Transactional
public interface NoteRepository extends JpaRepository<Note, Integer> {
  List<Note> findByTitleContainingIgnoreCase(String keyword);

}
