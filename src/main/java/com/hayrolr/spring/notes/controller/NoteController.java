package com.hayrolr.spring.notes.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.hayrolr.spring.notes.entity.Note;
import com.hayrolr.spring.notes.repository.NoteRepository;

import jakarta.validation.Valid;
import org.springframework.validation.Errors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class NoteController {

  @Autowired
  private NoteRepository noteRepository;

  @GetMapping("/notes")
  public String retrieveAll(Model model, @RequestParam(required = false) String keyword,
                       @RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "6") int size,
                       @RequestParam(defaultValue = "id,asc") String[] sort) {
    try {
      List<Note> notes = new ArrayList<>();

      String sortField = sort[0];
      String sortDirection = sort[1];

      Direction direction = sortDirection.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
      Order order = new Order(direction, sortField);

      Pageable pageable = PageRequest.of(page - 1, size, Sort.by(order));

      Page<Note> pageNotes;
      if (keyword == null) {
        pageNotes = noteRepository.findAll(pageable);
      } else {
        pageNotes = noteRepository.findByTitleContainingIgnoreCase(keyword, pageable);
        model.addAttribute("keyword", keyword);
      }

      notes = pageNotes.getContent();

      model.addAttribute("notes", notes);
      model.addAttribute("currentPage", pageNotes.getNumber() + 1);
      model.addAttribute("totalItems", pageNotes.getTotalElements());
      model.addAttribute("totalPages", pageNotes.getTotalPages());
      model.addAttribute("pageSize", size);
      model.addAttribute("sortField", sortField);
      model.addAttribute("sortDirection", sortDirection);
      model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
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
  public String saveNote(@Valid Note note, Errors errors, Model model, RedirectAttributes redirectAttributes) {
    if (errors!=null && errors.hasErrors()) {
      var id = note.getId();
      var pageTitle = id==null?"Create new Note":"Edit Note (ID: " + id + ")";
      model.addAttribute("pageTitle", pageTitle);
      return "note_form";
    }

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
