package com.mycompany.app.entities;

import com.mycompany.app.entities.keys.BookTypeKey;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

// Named queries for the BookType entity
@NamedQueries({
    @NamedQuery(name = "BookType.findAll", query = "SELECT b FROM BookType b"),
    @NamedQuery(name = "BookType.findBySubcodeAndName", query = "SELECT b FROM BookType b WHERE b.subCode = :subCode AND b.name LIKE :name"),
})

/**
 * Represents a type of book in the library system.
 * This entity is identified by a composite key consisting of type code and type
 * subcode.
 * The type code and subcode together uniquely identify a book type.
 * 
 * This class is mapped to the "book_type" table in the database.
 * It contains fields for the type code, type subcode, and type name.
 * The type code and subcode are used as the primary key for this entity.
 * The type name is a descriptive name for the book type.
 * 
 * The class overrides the equals and hashCode methods to ensure that two
 * BookType objects are considered equal if their type code and subcode are the
 * same.
 * The toString method provides a string representation of the BookType object.
 */
@Entity
@Table(name = "book_type")
@IdClass(BookTypeKey.class)
public class BookType {

  @Id
  @Column(name = "type_code", nullable = false)
  private String code;

  @Id
  @Column(name = "type_subcode", nullable = false)
  private String subCode;

  @Column(name = "type_name")
  private String name;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getSubCode() {
    return subCode;
  }

  public void setSubCode(String subCode) {
    this.subCode = subCode;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "BookType [code=" + code + ", subCode=" + subCode + ", name=" + name + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((code == null) ? 0 : code.hashCode());
    result = prime * result + ((subCode == null) ? 0 : subCode.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    BookType other = (BookType) obj;
    if (code == null) {
      if (other.code != null)
        return false;
    } else if (!code.equals(other.code))
      return false;
    if (subCode == null) {
      if (other.subCode != null)
        return false;
    } else if (!subCode.equals(other.subCode))
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }
}
