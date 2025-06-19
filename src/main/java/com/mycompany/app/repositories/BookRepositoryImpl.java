package com.mycompany.app.repositories;

import java.util.List;

import com.mycompany.app.entities.Book;

import jakarta.persistence.EntityManager;

/**
 * BookRepositoryImpl is an implementation of the BookRepository interface.
 * It provides methods for accessing and manipulating book data in a database.
 */
public class BookRepositoryImpl implements BookRepository {

  // Implement methods from BookRepository interface here

  // Additional methods and logic can be added as needed
  // Ensure to handle database connections, queries, and any other necessary
  // operations.
  // Remember to follow best practices for repository patterns and data access
  // You may also want to include error handling and logging as appropriate.

  private EntityManager em;

  /**
   * Constructor to initialize the EntityManager.
   * 
   * @param em The EntityManager to be used for database operations.
   */
  public BookRepositoryImpl(EntityManager em) {
    this.em = em;
  }

  /**
   * Adds a new book to the repository.
   * 
   * @param book The book to be added.
   */
  @Override
  public void add(Book book) {
    try {
      em.getTransaction().begin();

      em.persist(book);
      
      em.getTransaction().commit();
    } catch (Exception e) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw e; // Rethrow the exception after rollback
    }
  }

  /**
   * Updates an existing book in the repository.
   * 
   * @param book The book to be updated.
   */
  @Override
  public void update(Book book) {
    try {
      em.getTransaction().begin();

      em.merge(book);
      
      em.getTransaction().commit();
    } catch (Exception e) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw e; // Rethrow the exception after rollback
    }
  }

  /**
   * Deletes a book from the repository.
   * 
   * @param book The book to be deleted.
   */
  @Override
  public void delete(Book book) {
    try {
      em.getTransaction().begin();

      if (em.contains(book)) {
        em.remove(book);
      } else {
        // If the book is not managed, find it first
        Book managedBook = em.find(Book.class, book.getId());
        if (managedBook != null) {
          em.remove(managedBook);
        }
      }
      
      em.getTransaction().commit();
    } catch (Exception e) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw e; // Rethrow the exception after rollback
    } 
  }

  /**
   * Finds a book by its ID.
   * 
   * @param id The ID of the book to be found.
   * @return The book with the specified ID, or null if not found.
   */
  @Override
  public Book findBookById(int id) {
    try {
      return em.find(Book.class, id);
    } catch (Exception e) {
      // Handle exceptions appropriately, such as logging or rethrowing
      throw new RuntimeException("Error finding book by ID: " + id, e);
    }
  }

  /**
   * Finds a book by its title.
   * 
   * @param title The title of the book to be found.
   * @return The book with the specified title, or null if not found.
   */
  @Override
  public Book findBookByTitle(String title) {
    try {
      return em.createQuery("SELECT b FROM Book b WHERE b.name = :title", Book.class)
               .setParameter("title", title)
               .getSingleResult();
    } catch (Exception e) {
      // Handle exceptions appropriately, such as logging or rethrowing
      throw new RuntimeException("Error finding book by title: " + title, e);
    } 
  }

  /**
   * Finds books by the author's name.
   * 
   * @param author The name of the author whose books are to be found.
   * @return A list of books written by the specified author.
   */
  @Override
  public List<Book> findBookByAuthor(String author) {
    try {
      return em.createQuery("SELECT b FROM Book b WHERE b.author.name = :author", Book.class)
               .setParameter("author", author)
               .getResultList();
    } catch (Exception e) {
      // Handle exceptions appropriately, such as logging or rethrowing
      throw new RuntimeException("Error finding books by author: " + author, e);
    }
  }

  /**
   * Closes the EntityManager to release resources.
   * This method is called when the repository is no longer needed.
   */
  @Override
  public void close()  {
    // Ensure the EntityManager is closed when the repository is no longer needed
    // This is important to release resources and avoid memory leaks
    // If the EntityManager is managed by a container (like in a Java EE environment),
    // you may not need to close it here, as the container will handle it.
    // However, if you are managing the EntityManager manually, closing it here is
    // a good practice to ensure resources are released properly.
    // Note: If you close the EntityManager here, ensure that no further operations
    // are performed on it after this point, as it will no longer be valid.
    // If you are using a try-with-resources statement, the close method will be called
    // automatically when the try block is exited, ensuring proper resource management.
    if (em != null && em.isOpen()) {
      em.close();
    }
  }
}
