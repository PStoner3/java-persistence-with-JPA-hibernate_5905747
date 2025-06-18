-- This DDL is based on the Linkedin Learning course "Java Persistence with JPA and Hibernate" by Buddhini Samarakkody
-- https://www.linkedin.com/learning/java-persistence-with-jpa-and-hibernate/
-- Each statement or section of statements will be identified be the module number to which the apply.
-- These statements are executed progressively, meaning there is no need to recreate the entire database each time.
-- Simply uncomment the module number section and execute that code only.
-- This script is used to create the database and tables for the course listed above.
-- This code is for module 01_01 of the Java Persistence with JPA and Hibernate course.
-- It creates the database and tables for the course.
-- Create a database
DROP DATABASE IF EXISTS library;
CREATE DATABASE IF NOT EXISTS library;
-- Use the database
USE library;
-- Crate tables in the database
CREATE TABLE IF NOT EXISTS author(
  author_id INT AUTO_INCREMENT PRIMARY KEY,
  author_name VARCHAR(255),
  street VARCHAR(255),
  city VARCHAR(255),
  postal_code VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS book(
  book_id INT AUTO_INCREMENT PRIMARY KEY,
  book_name VARCHAR(255),
  isbn VARCHAR(255),
  author_id INT,
  price DECIMAL,
  FOREIGN KEY (author_id) REFERENCES author(author_id)
);
CREATE TABLE IF NOT EXISTS book_type(
  type_code VARCHAR(255),
  type_subcode VARCHAR(255),
  type_name VARCHAR(255),
  PRIMARY KEY(type_code, type_subcode)
);
CREATE TABLE IF NOT EXISTS item(
  item_code VARCHAR(255),
  item_number INT,
  item_name VARCHAR(255),
  PRIMARY KEY(item_code, item_number)
);
CREATE TABLE IF NOT EXISTS review(
  review_id INT AUTO_INCREMENT PRIMARY KEY,
  comment VARCHAR(255),
  book_id INT,
  rating INT,
  FOREIGN KEY (book_id) REFERENCES book(book_id)
);
CREATE TABLE IF NOT EXISTS user(
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  user_name VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS grp(
  group_id INT AUTO_INCREMENT PRIMARY KEY,
  group_name VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS user_group(
  user_id INT,
  group_id INT,
  FOREIGN KEY (user_id) REFERENCES user(user_id),
  FOREIGN KEY (group_id) REFERENCES grp(group_id)
);

-- This code is for module 07_01 of the Java Performance Linkedin Learning course.-- USE library;
INSERT INTO book_type(type_code, type_subcode, type_name)
values ("C001", "SC001", "Fiction-Horror"),
  ("C001", "SC002", "Fiction-Mistry"),
  ("C001", "SC003", "Fiction-Thriller");

-- This code is for module 07_02 of the Java Performance Linkedin Learning course.
-- USE library;
INSERT INTO author(author_name, street, city, postal_code)
values 
  ("Jane", "street1", "London", "12345"),
  ("Allen", "street2", "New York", "45678"),
  ("John", "street3", "London", "56789")
ON DUPLICATE KEY UPDATE 
  author_name = VALUES(author_name), 
  street = VALUES(street), 
  city = VALUES(city), 
  postal_code = VALUES(postal_code);


INSERT INTO book(book_name, isbn, author_id)
values 
  ("Book1", "111-1111", 1),
  ("Book2", "222-2222", 1),
  ("Book3", "333-3333", 2),
  ("Book4", "444-4444", null)
ON DUPLICATE KEY UPDATE
  book_name = VALUES(book_name), 
  isbn = VALUES(isbn), 
  author_id = VALUES(author_id);


-- This code is for module 07_04 of the Java Performance Linkedin Learning course.
INSERT INTO author(author_name, street, city, postal_code)
values 
  ("Jane", "street1", "London", "12345"),
  ("Allen", "street2", "New York", "45678"),
  ("John", "street3", "London", "56789"),
  ("David", "street4", "London", "76789"),
  ("Doe", "street4", "Paris", "643343"),
  ("Austin", "street5", "Paris", "343333")
ON DUPLICATE KEY UPDATE 
  author_name = VALUES(author_name), 
  street = VALUES(street), 
  city = VALUES(city), 
  postal_code = VALUES(postal_code);

INSERT INTO book(book_name, isbn, author_id, price)
values 
  ("Book1", "111-1111", 1, 1000),
  ("Book2", "222-2222", 1, 1500),
  ("Book3", "333-3333", 2, 2000),
  ("Book4", "444-444", 3, 1000),
  ("Book5", "555-555", 2, 1100)
ON DUPLICATE KEY UPDATE
  book_name = VALUES(book_name), 
  isbn = VALUES(isbn), 
  author_id = VALUES(author_id), 
  price = VALUES(price);


INSERT INTO review(comment, book_id, rating)
VALUES("Excellent!", 1, 5),
  ("Very good", 1, 4),
  ("Good", 2, 3),
  ("Not too bad", 3, 3),
  ("Not too bad", 4, 3),
  ("Not too bad", 5, 3)
ON DUPLICATE KEY UPDATE
  comment = VALUES(comment), 
  book_id = VALUES(book_id), 
  rating = VALUES(rating);

