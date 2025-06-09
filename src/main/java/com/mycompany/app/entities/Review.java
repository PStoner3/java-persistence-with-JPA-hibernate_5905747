package com.mycompany.app.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "review")
public class Review {
  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  @Column(name = "review_id", nullable = false)
  private int id;

  private String comment;

  private int rating;

  @ManyToOne
  @JoinColumn(name = "teacher_id")
  private Teacher teacher;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Teacher getTeacherId() {
    return teacher;
  }

  public void setTeacherId(Teacher teacher) {
    this.teacher = teacher;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
    this.rating = rating;
  }

  /**
   * @return the teacher
   */
  public Teacher getTeacher() {
    return teacher;
  }

  /**
   * @param teacher the teacher to set
   */
  public void setTeacher(Teacher teacher) {
    this.teacher = teacher;
  }
}
