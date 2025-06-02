package com.mycompany.app;

import com.mycompany.app.entities.Student;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {
  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("artclass_persistence_unit");

    EntityManager em = emf.createEntityManager();

    try {
      em.getTransaction().begin();

      Student student = new Student();
      student.setName("John Doe");
      em.persist(student);

      em.getTransaction().commit();
    } finally {
      em.close();
      emf.close();
    }
  }
}