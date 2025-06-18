package com.mycompany.app.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.Table;

@NamedQueries({
    @NamedQuery(name = "Review.findMinRatingByBookName", query = "SELECT MIN(r.rating) FROM Review r WHERE r.book.name = :name"),
    @NamedQuery(name = "Review.findMaxRatingByBookName", query = "SELECT MAX(r.rating) FROM Review r WHERE r.book.name = :name"),
    @NamedQuery(name = "Review.findAvgRatingByBookName", query = "SELECT AVG(r.rating) FROM Review r WHERE r.book.name = :name")
})

@Entity
@Table(name = "review")
public class Review {
  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  @Column(name = "review_id", nullable = false)
  private int id;

  private String comment;

  @ManyToOne
  @JoinColumn(name = "book_id")
  private Book book;

  private int rating;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public Book getBook() {
    return book;
  }

  public void setBook(Book book) {
    this.book = book;
  }

  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
    this.rating = rating;
  }

  @Override
  public String toString() {
    return "Review [id=" + id + ", comment=" + comment + "]";
  }
}
