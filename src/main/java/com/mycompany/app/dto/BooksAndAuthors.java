package com.mycompany.app.dto;

import com.mycompany.app.entities.Address;
import com.mycompany.app.entities.Author;
import com.mycompany.app.entities.Book;

public record BooksAndAuthors(
  Book book,
  Author author,
  Address address) {
    
  
}
