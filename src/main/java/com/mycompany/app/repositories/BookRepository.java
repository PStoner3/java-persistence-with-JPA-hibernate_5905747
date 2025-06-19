package com.mycompany.app.repositories;

import java.io.Closeable;
import java.util.List;

import com.mycompany.app.entities.Book;

/**
 * * BookRepository interface defines the contract for book data access operations.
 */
public interface BookRepository extends Closeable{
  public void add(Book book);
  public void update(Book book);
  public void delete(Book book);
  public Book findBookById(int id);
  public Book findBookByTitle(String title);
  public List<Book> findBookByAuthor (String author);
}
