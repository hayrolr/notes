package com.hayrolr.spring.notes.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "notes")
public class Note {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;
  @NotBlank(message = "Title can not be blank!")
  @Column(length = 120, nullable = false)
  private String title;
  @Column(length = 256)
  private String content;
  @NotNull(message = "Please enter a valid date!")
  @DateTimeFormat(pattern = "MM/dd/yyyy")
  @Column(nullable = false)
  private LocalDate date;
  public Note() {}
  public Note(String title, String content, LocalDate date) {
    this.title = title;
    this.content = content;
    this.date = date;
  }
  public Integer getId() { return id;}
  public void setId(Integer id) {
    this.id = id;
  }
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public String getContent() {
    return content;
  }
  public void setContent(String content) {
    this.content = content;
  }
  public LocalDate getDate() {
    return date;
  }
  public void setDate(LocalDate date) {
    this.date = date;
  }

  @Override
  public String toString() {
    return "Note [id=" + id + ", title=" + title + ", content=" + content + ", date=" + date + "]";
  }

}