package com.mycompany.app;

import java.util.List;

import com.mycompany.app.entities.ArtClass;
import com.mycompany.app.entities.Review;
import com.mycompany.app.entities.Student;
import com.mycompany.app.entities.Teacher;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {
  public static void main(String[] args) {

    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("artclass_persistence_unit")) {

      // create(emf);
      // update(emf);
      // attachAndDetach(emf);
      // remove(emf);
      // createClassTeacherRelationship(emf);
      // createTeacherReviewRelationship(emf);
      createStudentClassRelationship(emf);
    }
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
    try (EntityManager em = emf.createEntityManager()) {
      em.getTransaction().begin();

      Student student = em.find(Student.class, 3);
      em.remove(student);

      em.getTransaction().commit();
    }
  }

  private static void createClassTeacherRelationship(EntityManagerFactory emf) {
    try (EntityManager em = emf.createEntityManager()){
      em.getTransaction().begin();

      Teacher teacher = new Teacher();
      ArtClass artClass = new ArtClass();

      teacher.setName("Donatello");

      artClass.setName("Renaissance Art History");
      artClass.setDayOfWeek("Monday");
      artClass.setTeacher(teacher);

      em.persist(artClass);
      em.persist(teacher);

      em.getTransaction().commit();
    }
  }

  private static void createTeacherReviewRelationship(EntityManagerFactory emf){
    try (EntityManager em = emf.createEntityManager()){
      em.getTransaction().begin();

      Teacher teacher = new Teacher();
      teacher.setName("Raphael");

      Review review1 = new Review();
      review1.setTeacher(teacher);
      review1.setRating(5);
      review1.setComment("Excellent instructor. Learned a lot");

      Review review2 = new Review();
      review2.setTeacher(teacher);
      review2.setRating(3);
      review2.setComment("Rather Boring");

      teacher.setReviews(List.of(review1, review2));

      em.persist(teacher);

      em.getTransaction().commit();
    }
  }

  private static void createStudentClassRelationship(EntityManagerFactory emf){
    try(EntityManager em = emf.createEntityManager()){
      em.getTransaction().begin();

      Student student1 = new Student();
      Student student2 = new Student();

      ArtClass class1 = new ArtClass();
      ArtClass class2 = new ArtClass();

      Teacher teacherClass1 = new Teacher();
      teacherClass1.setName("Alyssa Monks");

      Teacher teacherClass2 = new Teacher();
      teacherClass2.setName("Constantin Brancusi");

      student1.setName("Auguste Rodin");
      student2.setName("Pablo Picasso");

      class1.setDayOfWeek("Tuesday");
      class1.setName("Realism");
      class1.setTeacher(teacherClass1);
      class1.setStudents(List.of(student1, student2));

      class2.setDayOfWeek("Thursday");
      class2.setName("Classical Sculpture");
      class2.setTeacher(teacherClass2);
      class2.setStudents(List.of(student1));

      em.persist(class1);
      em.persist(class2);

      em.getTransaction().commit();
    }
  }
}