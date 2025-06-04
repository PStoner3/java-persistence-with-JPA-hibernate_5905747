package com.mycompany.app;

import com.mycompany.app.entities.Student;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {
  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("artclass_persistence_unit");

    // create(emf);
    // update(emf);
    // attachAndDetach(emf);
    remove(emf);

    emf.close();
  }

  private static void create(EntityManagerFactory emf) {
    EntityManager em = emf.createEntityManager();
    try {
      em.getTransaction().begin();

      Student student = new Student();
      student.setName("John");
      em.persist(student);

      em.getTransaction().commit();
    } finally {
      em.close();
    }
  }

  private static void update(EntityManagerFactory emf) {
    EntityManager em = emf.createEntityManager();
    try {
      em.getTransaction().begin();

      Student student = em.find(Student.class, 1);
      student.setName("Peter");
      em.merge(student);

      em.getTransaction().commit();
    } finally {
      em.close();
    }
  }

  private static void attachAndDetach(EntityManagerFactory emf) {
    Student student = new Student();
    student.setName("Mary");

    try (EntityManager em = emf.createEntityManager()) {
      em.getTransaction().begin();

      // Attaching the entity
      // em.merge(student);

      // Detaching the entity
      em.detach(student);

      // The entity is now detached and can be used outside the transaction context
      student.setName("Anna");
      System.out.println("Detached student name: " + student.getName());

      em.getTransaction().commit();
    }
  }

  private static void remove(EntityManagerFactory emf) {
    try(EntityManager em = emf.createEntityManager()) {
      em.getTransaction().begin();

      Student student = em.find(Student.class, 3);
      em.remove(student);

      em.getTransaction().commit();
    }
  }
}