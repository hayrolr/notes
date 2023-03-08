package com.hayrolr.spring.notes.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.hayrolr.spring.notes.entity.Note;
import com.hayrolr.spring.notes.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class NoteController {

  @Autowired
  private NoteRepository noteRepository;

  @GetMapping("/notes")
  public String retrieveAll(Model model, @Param("keyword") String keyword) {
    try {
      List<Note> notes = new ArrayList<Note>();

      if (keyword == null) {
        noteRepository.findAll().forEach(notes::add);
      } else {
        noteRepository.findByTitleContainingIgnoreCase(keyword).forEach(notes::add);
        model.addAttribute("keyword", keyword);
      }

      model.addAttribute("notes", notes);
    } catch (Exception e) {
      model.addAttribute("message", e.getMessage());
    }

    return "notes";
  }

  @GetMapping("/notes/new")
  public String addNote(Model model) {
    Note note = new Note();
    note.setDate(LocalDate.now());

    model.addAttribute("note", note);
    model.addAttribute("pageTitle", "Create new Note");

    return "note_form";
  }

  @PostMapping("/notes/save")
  public String saveNote(Note note, RedirectAttributes redirectAttributes) {
    try {
      noteRepository.save(note);

      redirectAttributes.addFlashAttribute("message", "The Note has been saved successfully!");
    } catch (Exception e) {
      redirectAttributes.addAttribute("message", e.getMessage());
    }

    return "redirect:/notes";
  }

  @GetMapping("/notes/{id}")
  public String editNote(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
    try {
      Note note = noteRepository.findById(id).get();

      model.addAttribute("note", note);
      model.addAttribute("pageTitle", "Edit Note (ID: " + id + ")");

      return "note_form";
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("message", e.getMessage());

      return "redirect:/notes";
    }
  }

  @GetMapping("/notes/delete/{id}")
  public String deleteNote(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
    try {
      noteRepository.deleteById(id);

      redirectAttributes.addFlashAttribute("message", "The Note with id=" + id + " has been deleted successfully!");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("message", e.getMessage());
    }

    return "redirect:/notes";
  }

}
