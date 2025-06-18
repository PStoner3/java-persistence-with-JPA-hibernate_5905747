package com.mycompany.app;

import java.util.List;
import java.util.Set;

import com.mycompany.app.dto.BooksAndAuthors;
import com.mycompany.app.entities.Address;
import com.mycompany.app.entities.Author;
import com.mycompany.app.entities.Book;
import com.mycompany.app.entities.BookType;
import com.mycompany.app.entities.CardPayment;
import com.mycompany.app.entities.CashPayment;
import com.mycompany.app.entities.Category;
import com.mycompany.app.entities.Fiction;
import com.mycompany.app.entities.Field;
import com.mycompany.app.entities.Group;
import com.mycompany.app.entities.Item;
import com.mycompany.app.entities.NonFiction;
import com.mycompany.app.entities.Review;
import com.mycompany.app.entities.Student;
import com.mycompany.app.entities.Student2;
import com.mycompany.app.entities.Teacher;
import com.mycompany.app.entities.Teacher2;
import com.mycompany.app.entities.User;
import com.mycompany.app.entities.keys.ItemKey;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

/**
 * This application introduces basic concepts of using an ORM, in this case
 * Hibernate, as a JPA provider.
 * <p>
 * The main class demonstrates how to create, find, update, detach, reattach,
 * and remove instances of the Book entity using JPA. It also shows how to use
 * the getReference method to obtain a reference to an entity without
 * immediately
 * loading it from the database.
 * <p>
 * To run this application, ensure you have the necessary dependencies for JPA
 * and Hibernate in your project. The example uses an in-memory H2 database
 * for simplicity, but you can configure it to use any other database by
 * changing
 * the JDBC URL and driver in the persistence.xml file.
 * <p>
 * An example persistence.xml file is provided below, which should be placed in
 * the src/main/resources/META-INF directory of your project. This file defines
 * the persistence unit and the properties required to connect to the database.
 *
 * <pre>{@code
 * <?xml version="1.0" encoding="UTF-8"?>
 * <persistence xmlns="http://xmlns.jcp.org/xml/ns/persistence"
 *           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 *           xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence
 *           http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd"
 *           version="2.1">
 *  <persistence-unit name="library_persistence_unit">
 *      <class>com.mycompany.app.entities.Book</class>
 *      <properties>
 *          <property name="jakarta.persistence.jdbc.driver" value=
"org.h2.Driver"/>
 *          <property name="jakarta.persistence.jdbc.url" value=
"jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"/>
 *          <property name="jakarta.persistence.jdbc.user" value="sa"/>
 *          <property name="jakarta.persistence.jdbc.password" value=""/>
 *          <property name="hibernate.hbm2ddl.auto" value="create-drop"/>
 *          <property name="hibernate.dialect" value=
"org.hibernate.dialect.H2Dialect"/>
 *      </properties>
 *  </persistence-unit>
 * </persistence>
 * }</pre>
 *
 * <p>
 * The example uses an in-memory H2 database, which is suitable for testing and
 * development purposes.
 * <p>
 * Ensure you have the necessary dependencies in your pom.xml or build.gradle
 * file
 * for JPA and Hibernate.
 * For example, if you are using Maven (pom.xml), example dependencies would be:
 *
 * <pre>{@code
 * <dependency>
 *    <groupId>org.hibernate</groupId>
 *    <artifactId>hibernate-core</artifactId>
 *    <version>5.4.32.Final</version>
 * </dependency>
 * <dependency>
 *    <groupId>org.hibernate</groupId>
 *    <artifactId>hibernate-entitymanager</artifactId>
 *    <version>5.4.32.Final</version>
 * </dependency>
 * <dependency>
 *    <groupId>com.h2database</groupId>
 *    <artifactId>h2</artifactId>
 *    <version>1.4.200</version>
 *    <scope>runtime</scope>
 * </dependency>
 * <dependency>
 *    <groupId>jakarta.persistence</groupId>
 *    <artifactId>jakarta.persistence-api</artifactId>
 *    <version>2.2.3</version>
 * </dependency>
 *}</pre>
 *
 * <p>
 * Or, if you are using Gradle (build.gradle), dependencies would
 * be:
 *
 * <pre>{@code
 *   dependencies {
 *     implementation 'org.hibernate.orm:hibernate-core:6.4.4.Final'
 *     implementation 'jakarta.persistence:jakarta.persistence-api:3.1.0'
 *     // Add your JDBC driver as needed, e.g. for H2:
 *     runtimeOnly 'com.h2database:h2:2.2.224'
 *     // ...other dependencies...
 *   }
 * }</pre>
 * <p>
 * The above dependencies are examples and may need to be adjusted based on
 * your project setup. Make sure to adjust the versions according to your
 * project setup and requirements.
 * <p>
 * You can also use a different database by changing the JDBC URL and driver
 * in the persistence.xml file.
 * For example, to use MySQL, you would change the JDBC URL to:
 *
 * <pre>{@code
 * <property name="jakarta.persistence.jdbc.url" value=
 * "jdbc:mysql://localhost:3306/mydb"/>
 * }</pre>
 *
 * Or, if using PostgreSQL, you would change the JDBC URL to:
 *
 * <pre>{@code
 * <property name="jakarta.persistence.jdbc.url" value=
 * "jdbc:postgresql://localhost:5432/mydb"/>
 * }
 * </pre>
 *
 * <p>
 * The JDBC driver for MySQL or PostgreSQL must also be included in your
 * project dependencies.
 * For example, if you are using MySQL, you would add a dependency
 *
 * <pre>{@code
 * <dependency>
 *    <groupId>mysql</groupId>
 *    <artifactId>mysql-connector-java</artifactId>
 *    <version>8.0.26</version>
 * </dependency>
 * }</pre>
 *
 * or in a Gradle build script (build.gradle), you would add:
 *
 * <pre>{@code
 * dependencies {
 *  runtimeOnly 'mysql:mysql-connector-java:8.0.26'
 * }
 * }</pre>
 *
 * <p>
 * If PostgreSQL is your choice, you would add the PostgreSQL driver
 * dependency in your pom.xml like this:
 * *
 *
 * <pre>{@code
 * <dependency>
 *   <groupId>org.postgresql</groupId>
 *   <artifactId>postgresql</artifactId>
 *  <version>42.2.20</version>
 * </dependency>
 * }</pre>
 *
 * or in a Gradle build script (build.gradle), you would add:
 *
 * <pre>{@code
 * dependencies {
 *  runtimeOnly 'org.postgresql:postgresql:42.2.20'
 * }
 * }</pre>
 *
 * <p>
 * Always adjust the JDBC URL, user, and password according to your database
 * setup. Adjust the version numbers according to your project requirements and
 * the
 * latest available versions. Ensure that the database is running and accessible
 * when you run the
 * application.
 *
 * <p>
 * This application is a simple demonstration of how to use JPA with Hibernate
 * to perform basic CRUD operations on an entity. It is intended for educational
 * purposes and to provide a starting point for working with JPA in Java
 * applications.
 *
 * <p>
 * Note: The code provided here is a basic example and does not include error
 * handling or advanced features such as transaction management, caching, or
 * query optimization. In a production application, you would want to implement
 * proper error handling, logging, and possibly use a more sophisticated
 * configuration for your persistence unit.
 *
 * <p>
 * This code is provided as-is and is intended for educational purposes.
 * It is recommended to refer to the official JPA and Hibernate documentation
 * for more detailed information and best practices when working with JPA in
 * Java applications.
 *
 * <p>
 * To execute and test the various methods in this class, you can
 * uncomment the corresponding method calls in the main method. Each method
 * demonstrates a different aspect of JPA entity management, such as creating,
 * finding, updating, detaching, reattaching, and removing entities.
 * You can run the application to see how each method works and observe the
 * output in the console.
 *
 * <p>
 * Lesson 07_04 describes using SQL aggregate function in JPQL. The method
 * for this lesson still invokes a transaction for the JPQL queries.
 * This may seem counterintuitive to use a transaction for a read-only
 * query. However, doind so provides several important considerations:
 * 1. Ensuring data consistency and isolation during the query execution by:
 *    - Preventing inconsistent reads:
 *        - The transaction ensures that the query sees a consistent snapshot of
 *           the database at the time the transaction started, even in concurrent
 *           environments.
 *    - Isolation levels:
 *        - The transaction can be configured with different isolation levels to
 *          control how concurrent transactions interact with each other.
 *        - ACID properties:
 *            - The transaction ensures that the query execution adheres to the ACID
 *              properties (Atomicity, Consistency, Isolation, Durability) of the
 *              database.
 * 2. Performance optimization with `readOnly = true`:
 *    - Hint for optimization:
 *        - The `readOnly = true` marking tells the persistence provider, pp, (i.e.
 *          Hibernate) and the database no modifications will be made to the data.
 *    - Reduced overhead:
 *        - This hint informs the pp to skip steps like tracking changes to entities
 *        - Database oprtinization:
 *            - Database will often apply vendor specific optimizations for read-only
 *              transactions, such as reducing locking overhead.
 * 
 * <p>
 * One can read about more of the performance and management enhancements at
 * these resources:
 *    - https://www.geeksforgeeks.org/using-transactions-for-read-only-operations-in-spring-boot/#:~:text=In%20a%20Spring%20application%2C%20the,Transaction%20Management%20Using%20@Transactional%20Annotation
 *    - https://stackoverflow.com/questions/6855878/is-there-ever-a-reason-to-use-a-database-transaction-for-read-only-sql-statement
 *    - https://medium.com/@AlexanderObregon/the-mechanics-behind-how-spring-boot-handles-read-only-transactions-for-optimized-database-access-5302051b3934#:~:text=If%20the%20database%20supports%20read%2Donly%20transactions%2C%20it%20may%20also,while%20cutting%20out%20unnecessary%20work.
 * and other by asking google: jpa why use transaction when executing an aggregation query.
 * 
 * <p>
 * This will become our standard as we move towards using ORM in our
 * projects.
 *
 */

public class Main {
  public static void main(String[] args) {

    try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("library_persistence_unit")) {
      // Uncomment the method calls below to test different functionalities
      // createInstance(emf);
      // createMultipleInstances(emf);
      // findAndUpdateInstance(emf);
      // detachAndReattachInstance(emf);
      // removeInstance(emf);
      // useGetReference(emf);
      // useRefreah(emf);
      // createEntityWithCompositeKey(emf);
      // createOneToOneRelationship(emf);
      // createOneToManyRelationship(emf);
      // createManyToManyRelationship(emf);
      // mappedSuperclassStrategy(emf);
      // singleTableStrategy(emf);
      // joinedTableStrategy(emf);
      // tablePerClassStrategy(emf);
      // compositionWithAssociation(emf);
      // compositionWithEmbedable(emf);
      // writeJpqlQuery(emf);
      // rqiteTypedQueryJqpl(emf);
      // writeJpqlWithWhere(emf);
      // writeJpqlJoin(emf);
      // writeJpqlWithNamedQuery(emf);
      // aggregateFunctions(emf);
      // orderByClause(emf);
      // groupByClause(emf);
      // havingClause(emf);
      // nativeQueries(emf);
      criteriaQueries(emf);
    }
  }

  /**
   * This method creates a single instance of the Book entity and persists it
   * to the database.
   *
   * @param emf The EntityManagerFactory used to create EntityManager instances.
   */
  // This method demonstrates how to create and persist a single Book entity.
  // It creates a Book instance with a name and ISBN, persists it to the database,
  // and commits the transaction.
  // The method uses the EntityManager to manage the persistence context and
  // ensure that the entity is saved to the database.
  // The method is designed to be called from the main method to demonstrate
  // the creation and persistence of a Book entity in a JPA context.
  // It is a basic example of how to use JPA to create and persist entities in a
  // relational database using Hibernate as the JPA provider.
  // The method is annotated with @SuppressWarnings("unused") to indicate that it
  // is intentionally not used in the current context, but it can be uncommented
  // in the main method to execute it and see the results.
  @SuppressWarnings("unused")
  private static void createInstance(EntityManagerFactory emf) {
    EntityManager em = emf.createEntityManager();

    try {
      em.getTransaction().begin();

      Book book = new Book();
      book.setName("my book");
      book.setIsbn("123-4567890123");
      em.persist(book);
      System.out.println(book);

      em.getTransaction().commit();
    } finally {
      em.close();
    }
  }

  /**
   * This method demonstrates how to create multiple instances of the Book entity
   * and persists them to the database.
   *
   * @param emf The EntityManagerFactory used to create EntityManager instances.
   */
  // This method five Book instances with unique names and ISBNs, persists them
  // to the database, and commits the transaction. It uses the EntityManager
  // to manage the persistence context and ensure that the entities are saved
  // to the database. The method also ensures that the transaction is properly
  // managed by beginning a transaction, committing it after persisting the
  // entities, and closing the EntityManager to release resources.
  // The method is designed to be called from the main method to demonstrate
  // the creation and persistence of Book entities in a JPA context.
  // It is a basic example of how to use JPA to create and persist entities in a
  // relational database using Hibernate as the JPA provider.
  // The method is annotated with @SuppressWarnings("unused") to indicate that it
  // is intentionally not used in the current context, but it can be uncommented
  // in the main method to execute it and see the results.
  @SuppressWarnings("unused")
  private static void createMultipleInstances(EntityManagerFactory emf) {
    EntityManager em = emf.createEntityManager();

    try {
      em.getTransaction().begin();

      for (int i = 1; i <= 5; i++) {
        Book book = new Book();
        book.setName("my book " + i);
        book.setIsbn(i + "23-4567890123");
        em.persist(book);
      }
      em.flush(); // Ensure the entities are persisted before committing
      em.getTransaction().commit();
    } finally {
      em.close();
    }
  }

  /**
   * This method finds an existing instance of the Book entity, updates its
   * ISBN, and commits the changes to the database.
   *
   * @param emf The EntityManagerFactory used to create EntityManager instances.
   */
  // This method demonstrates how to find an existing Book entity by its ID,
  // update its ISBN, and commit the changes to the database. While this method
  // updates the ISBN of the Book entity, any other properties of the
  // Book entity can also be updated in a similar manner. The book entity is
  // printed to the console after the update to show the changes made.
  // It uses the EntityManager to manage the persistence context and ensure that
  // the changes are saved to the database.
  // The method is designed to be called from the main method to demonstrate
  // the process of finding and updating an entity in a JPA context.
  // It is a basic example of how to use JPA to find and update entities in a
  // relational database using Hibernate as the JPA provider.
  // The method is annotated with @SuppressWarnings("unused") to indicate that it
  // is intentionally not used in the current context, but it can be uncommented
  // in the main method to execute it and see the results.
  @SuppressWarnings("unused")
  private static void findAndUpdateInstance(EntityManagerFactory emf) {
    EntityManager em = emf.createEntityManager();

    try {
      em.getTransaction().begin();

      Book book = em.find(Book.class, 2);
      if (book != null) {
        book.setIsbn("223-4567890123");
        System.out.println(book);
      }

      em.getTransaction().commit();
    } finally {
      em.close();
    }
  }

  /**
   * This method demonstrates how to detach an entity from the persistence context
   * and then reattach it by merging it back into the context.
   *
   * @param emf The EntityManagerFactory used to create EntityManager instances.
   */
  // This method creates a new Book instance, sets its ID, name, and ISBN, and
  // then merges
  // it into the persistence context. The merge operation updates the entity in
  // the database if it already exists or creates a new entity if it does not.
  // The method is designed to be called from the main method to demonstrate
  // the process of detaching and reattaching an entity in a JPA context.
  // It is a basic example of how to use JPA to manage the lifecycle of entities
  // in a relational database using Hibernate as the JPA provider.
  // The method is annotated with @SuppressWarnings("unused") to indicate that it
  // is intentionally not used in the current context, but it can be uncommented
  // in the main method to execute it and see the results.
  @SuppressWarnings("unused")
  private static void detachAndReattachInstance(EntityManagerFactory emf) {
    EntityManager em = emf.createEntityManager();

    try {
      em.getTransaction().begin();

      Book book = new Book();
      book.setId(1);
      book.setName("my book");
      book.setIsbn("123-4567890123");
      em.merge(book);
      em.getTransaction().commit();
    } finally {
      em.close();
    }
  }

  /**
   * This method removes an existing instance of the Book entity from the
   * persistence context and commits the changes to the database.
   *
   * @param emf The EntityManagerFactory used to create EntityManager instances.
   */
  // This method demonstrates how to remove an existing Book entity from the
  // persistence context and commit the changes to the database.
  // It retrieves the Book entity with the given ID, removes it from the
  // persistence
  // context, and commits the transaction to persist the changes.
  // The book entity is printed to the console after removal to show that it has
  // been removed from the persistence context.
  // It also prints the removed Book entity to the console.
  // The method uses the EntityManager to manage the persistence context and
  // ensure that the entity is removed from the database.
  // The method is designed to be called from the main method to demonstrate
  // the process of removing an entity in a JPA context.
  // It is a basic example of how to use JPA to remove entities from a
  // relational database using Hibernate as the JPA provider.
  // The method is annotated with @SuppressWarnings("unused") to indicate that it
  // is intentionally not used in the current context, but it can be uncommented
  // in the main method to execute it and see the results.
  @SuppressWarnings("unused")
  private static void removeInstance(EntityManagerFactory emf) {
    EntityManager em = emf.createEntityManager();

    try {
      em.getTransaction().begin();

      Book book = em.find(Book.class, 1);
      em.remove(book);
      System.out.println(book);
      // Note: The book is now in a removed state, and it will not be managed by the
      // EntityManager anymore.
      em.getTransaction().commit();
    } finally {
      em.close();
    }
  }

  /**
   * This method demonstrates how to use the getReference method to obtain a
   * reference to an entity without immediately loading it from the database.
   *
   * @param emf The EntityManagerFactory used to create EntityManager instances.
   */
  // This method retrieves a reference to the Book entity with the given ID,
  // which does not hit the database immediately. The actual database access
  // happens when a property of the book is accessed, triggering the loading of
  // the entity. The method is designed to be called from the main method to
  // demonstrate the use of getReference in a JPA context.
  // It is a basic example of how to use JPA to obtain a reference to an entity
  // without immediately loading it from the database using Hibernate as the JPA
  // provider.
  // The method is annotated with @SuppressWarnings("unused") to indicate that it
  // is intentionally not used in the current context, but it can be uncommented
  // in the main method to execute it and see the results.
  @SuppressWarnings("unused")
  private static void useGetReference(EntityManagerFactory emf) {
    EntityManager em = emf.createEntityManager();

    try {
      em.getTransaction().begin();

      // getReference does not hit the database immediately; it returns a proxy.
      Book book = em.getReference(Book.class, 2);

      // The book variable is a proxy that represents the Book entity with given ID.
      // It does not load the entity from the database until a property is accessed.
      // This is useful for performance optimization, as it allows you to defer
      // loading the entity until it is actually needed.
      System.out.println(book);

      em.getTransaction().commit();
    } finally {
      em.close();
    }
  }

  /**
   * This method demonstrates how to use the refresh method to ensure that an
   * entity reflects the latest state from the database.
   *
   * @param emf The EntityManagerFactory used to create EntityManager instances.
   */
  // This method retrieves a reference to the Book entity with the given ID,
  // modifies its name, and then calls the refresh method to reload the entity
  // from the database, ensuring that it reflects the latest state.
  // The method is designed to be called from the main method to demonstrate
  // the use of refresh in a JPA context.
  // It is a basic example of how to use JPA to refresh an entity and ensure it
  // reflects the latest state from the database using Hibernate as the JPA
  // provider.
  // The method is annotated with @SuppressWarnings("unused") to indicate that it
  // is intentionally not used in the current context, but it can be uncommented
  // in the main method to execute it and see the results.
  @SuppressWarnings("unused")
  private static void useRefreah(EntityManagerFactory emf) {
    EntityManager em = emf.createEntityManager();

    try {
      em.getTransaction().begin();

      Book book = em.getReference(Book.class, 2);
      System.out.println("Before change " + book);
      book.setName("Updated Book Name");
      System.out.println("After change " + book);

      // Refresh the entity to ensure it reflects the latest state
      em.refresh(book);
      System.out.println("After refresh " + book);

      em.getTransaction().commit();
    } finally {
      em.close();
    }
  }

  /**
   * This method demonstrates how to create an entity with a composite key.
   *
   * @param emf The EntityManagerFactory used to create EntityManager instances.
   */
  // This method creates a BookType entity with a composite key consisting
  // of a code and subCode, or an Item entity with a composite key consisting
  // of a code and a number. The method persists these entities to the database
  // and commits the transaction.
  // The method is designed to be called from the main method to demonstrate
  // the creation of entities with composite keys in a JPA context.
  // It is a basic example of how to use JPA to create and persist entities with
  // composite keys in a relational database using Hibernate as the JPA provider.
  // The method is annotated with @SuppressWarnings("unused") to indicate that it
  // is intentionally not used in the current context, but it can be uncommented
  // in the main method to execute it and see the results.
  @SuppressWarnings("unused")
  private static void createEntityWithCompositeKey(EntityManagerFactory emf) {
    EntityManager em = emf.createEntityManager();

    try {
      em.getTransaction().begin();

      // Remove the block comment markers surrounding the following
      // block of code to create a BookType entity with a composite key.
      /*
       * // Create a new BookType instance with a composite key
       * BookType bookType = new BookType();
       * bookType.setCode("FIC");
       * bookType.setSubCode("SF001");
       * bookType.setName("Fiction");
       *
       * // Persist the BookType entity
       * em.persist(bookType);
       */

      // Remove the block comment markers surrounding the following
      // block of code to create an Item entity with a composite key.
      /*
       * // Create a new Item instance with a composite key
       * ItemKey id = new ItemKey();
       * id.setCode("FIC");
       * id.setNumber(100);
       *
       * Item item = new Item();
       * item.setId(id);
       * item.setName("Science Fiction Book");
       *
       * em.persist(item);
       */
      em.getTransaction().commit();
    } finally {
      em.close();
    }
  }

  /**
   * This method demonstrates how to create a one-to-one relationship
   * between the Book and Author
   * entities and persists them to the database.
   *
   * @param emf The EntityManagerFactory used to create EntityManager instances.
   */
  // This method creates a Book instance and an Author instance,
  // sets the relationship between them, persists both entities to the
  // database, and commits the transaction.
  // The method uses the EntityManager to manage the persistence context and
  // ensure that the entities are saved to the database.
  // The method is designed to be called from the main method to demonstrate
  // the creation of a one-to-one relationship in a JPA context.
  // It is a basic example of how to use JPA to create and persist entities with
  // a one-to-one relationship in a relational database using Hibernate as the JPA
  // provider.
  // The method is annotated with @SuppressWarnings("unused") to indicate that it
  // is intentionally not used in the current context, but it can be uncommented
  // in the main method to execute it and see the results.
  @SuppressWarnings("unused")
  private static void createOneToOneRelationship(EntityManagerFactory emf) {

    try (EntityManager em = emf.createEntityManager()) {
      em.getTransaction().begin();

      Book book = new Book();
      book.setName("another one of my books");
      book.setIsbn("623-4567890123");

      Author author = new Author();
      author.setName("John Doe");

      book.setAuthor(author);

      em.persist(book);
      em.persist(author);

      em.getTransaction().commit();
    }
  }

  /**
   * This method demonstrates how to create a one-to-many relationship
   * between the Book and Review entities and persists them to the database.
   *
   * @param emf The EntityManagerFactory used to create EntityManager instances.
   */
  // This method creates a Book instance and two Review instances,
  // sets the relationship between them, persists both entities to the
  // database, and commits the transaction.
  // The method uses the EntityManager to manage the persistence context and
  // ensure that the entities are saved to the database.
  // The method is designed to be called from the main method to demonstrate
  // the creation of a one-to-many relationship in a JPA context.
  // It is a basic example of how to use JPA to create and persist entities with
  // a one-to-many relationship in a relational database using Hibernate as the
  // JPA
  // provider.
  // The method is annotated with @SuppressWarnings("unused") to indicate that it
  // is intentionally not used in the current context, but it can be uncommented
  // in the main method to execute it and see the results.
  @SuppressWarnings("unused")
  private static void createOneToManyRelationship(EntityManagerFactory emf) {
    Book book = new Book();
    book.setName("Book with Authors");
    book.setIsbn("123-4567890123");

    Review review1 = new Review();
    review1.setComment("Great book!");
    review1.setBook(book);

    Review review2 = new Review();
    review2.setComment("Very informative.");
    review2.setBook(book);

    book.setReviews(List.of(review1, review2));

    try (EntityManager em = emf.createEntityManager()) {
      Author author = em.find(Author.class, 1);
      book.setAuthor(author);

      em.getTransaction().begin();

      em.persist(book);

      em.getTransaction().commit();
    }
  }

  /**
   * This method demonstrates how to create a many-to-many relationship
   * between the Group and User entities and persists them to the database.
   *
   * @param emf The EntityManagerFactory used to create EntityManager instances.
   */
  // This method creates a Group instance and two User instances,
  // sets the relationship between them, persists both entities to the
  // database, and commits the transaction.
  // The method uses the EntityManager to manage the persistence context and
  // ensure that the entities are saved to the database.
  // The method is designed to be called from the main method to demonstrate
  // the creation of a many-to-many relationship in a JPA context.
  // It is a basic example of how to use JPA to create and persist entities with
  // a many-to-many relationship in a relational database using Hibernate as the
  // JPA
  // provider.
  // The method is annotated with @SuppressWarnings("unused") to indicate that it
  // is intentionally not used in the current context, but it can be uncommented
  // in the main method to execute it and see the results.
  @SuppressWarnings("unused")
  private static void createManyToManyRelationship(EntityManagerFactory emf) {
    try (EntityManager em = emf.createEntityManager()) {
      em.getTransaction().begin();

      User user1 = new User();
      user1.setName("Alice");

      User user2 = new User();
      user2.setName("Bob");

      Group group1 = new Group();
      group1.setName("Developers");

      Group group2 = new Group();
      group2.setName("Designers");

      group1.setUsers(List.of(user1, user2));
      group2.setUsers(List.of(user1));

      em.persist(group1);
      em.persist(group2);

      em.getTransaction().commit();
    }
  }

  /**
   * This method demonstrates how to use the mapped superclass strategy
   * to create entities that share common attributes.
   *
   * @param emf The EntityManagerFactory used to create EntityManager instances.
   */
  // This method creates instances of Student and Teacher entities,
  // which inherit common attributes from a mapped superclass.
  // It sets specific attributes for each entity, persists them to the
  // database, and commits the transaction.
  // The method uses the EntityManager to manage the persistence context and
  // ensure that the entities are saved to the database.
  // The method is designed to be called from the main method to demonstrate
  // the use of mapped superclass strategy in a JPA context.
  // It is a basic example of how to use JPA to create and persist entities that
  // share common attributes in a relational database using Hibernate as the JPA
  // provider.
  // The method is annotated with @SuppressWarnings("unused") to indicate that it
  // is intentionally not used in the current context, but it can be uncommented
  // in the main method to execute it and see the results.
  @SuppressWarnings("unused")
  private static void mappedSuperclassStrategy(EntityManagerFactory emf) {
    try (EntityManager em = emf.createEntityManager()) {
      em.getTransaction().begin();

      Student student = new Student();
      student.setName("John Doe");
      student.setStudentCode("S12345");

      Teacher teacher = new Teacher();
      teacher.setName("Jane Smith");
      teacher.setTeacherCode("T67890");

      em.persist(student);
      em.persist(teacher);

      em.getTransaction().commit();
    }
  }

  /**
   * This method demonstrates how to use the single table inheritance strategy
   * to create entities that share a single table in the database.
   *
   * @param emf The EntityManagerFactory used to create EntityManager instances.
   */
  // This method creates instances of Student2 and Teacher2 entities,
  // which inherit common attributes from a base class Member2 and are stored
  // in a single table in the database using the single table inheritance
  // strategy.
  // It sets specific attributes for each entity, persists them to the
  // database, and commits the transaction.
  // The method uses the EntityManager to manage the persistence context and
  // ensure that the entities are saved to the database.
  // The method is designed to be called from the main method to demonstrate
  // the use of single table inheritance strategy in a JPA context.
  // It is a basic example of how to use JPA to create and persist entities that
  // share a single table in a relational database using Hibernate as the JPA
  // provider.
  // The method is annotated with @SuppressWarnings("unused") to indicate that it
  // is intentionally not used in the current context, but it can be uncommented
  // in the main method to execute it and see the results.
  @SuppressWarnings("unused")
  private static void singleTableStrategy(EntityManagerFactory emf) {
    try (EntityManager em = emf.createEntityManager()) {
      em.getTransaction().begin();

      Student2 student = new Student2();
      student.setName("John Doe");
      student.setStudentCode("S12345");

      Teacher2 teacher = new Teacher2();
      teacher.setName("Jane Smith");
      teacher.setTeacherCode("T67890");

      em.persist(student);
      em.persist(teacher);

      em.getTransaction().commit();
    }
  }

  /**
   * This method demonstrates how to use the joined table inheritance strategy
   * to create entities that are stored in separate tables but share a common
   * base class.
   *
   * @param emf The EntityManagerFactory used to create EntityManager instances.
   */
  // This method creates instances of Fiction and NonFiction entities,
  // which inherit common attributes from a base class Genre and are stored
  // in separate tables using the joined table inheritance strategy.
  // It sets specific attributes for each entity, persists them to the
  // database, and commits the transaction.
  // The method uses the EntityManager to manage the persistence context and
  // ensure that the entities are saved to the database.
  // The method is designed to be called from the main method to demonstrate
  // the use of joined table inheritance strategy in a JPA context.
  // It is a basic example of how to use JPA to create and persist entities that
  // share a common base class but are stored in separate tables in a
  // relational database using Hibernate as the JPA provider.
  // The method is annotated with @SuppressWarnings("unused") to indicate that it
  // is intentionally not used in the current context, but it can be uncommented
  // in the main method to execute it and see the results.
  @SuppressWarnings("unused")
  private static void joinedTableStrategy(EntityManagerFactory emf) {
    try (EntityManager em = emf.createEntityManager()) {
      em.getTransaction().begin();

      // Create instances of Fiction and NonFiction entities
      Fiction fiction = new Fiction();
      fiction.setCode("FIC");
      fiction.setSetting("Dystopian Future");

      NonFiction nonFiction = new NonFiction();
      nonFiction.setCode("NFIC");
      nonFiction.setTopic("Science");

      // Persist the entities
      em.persist(fiction);
      em.persist(nonFiction);

      em.getTransaction().commit();
    }
  }

  /**
   * This method demonstrates how to use the table-per-class inheritance strategy
   * to create entities that are stored in separate tables for each subclass.
   *
   * @param emf The EntityManagerFactory used to create EntityManager instances.
   */
  // This method creates instances of CardPayment and CashPayment entities,
  // which inherit common attributes from a base class Payment and are stored
  // in separate tables using the table-per-class inheritance strategy.
  // It sets specific attributes for each entity, persists them to the
  // database, and commits the transaction.
  // The method uses the EntityManager to manage the persistence context and
  // ensure that the entities are saved to the database.
  // The method is designed to be called from the main method to demonstrate
  // the use of table-per-class inheritance strategy in a JPA context.
  // It is a basic example of how to use JPA to create and persist entities that
  // share a common base class but are stored in separate tables in a
  // relational database using Hibernate as the JPA provider.
  // The method is annotated with @SuppressWarnings("unused") to indicate that it
  // is intentionally not used in the current context, but it can be uncommented
  // in the main method to execute it and see the results.
  @SuppressWarnings("unused")
  private static void tablePerClassStrategy(EntityManagerFactory emf) {
    try (EntityManager em = emf.createEntityManager()) {
      em.getTransaction().begin();

      CardPayment cardPayment = new CardPayment();
      cardPayment.setCardNumber("1234-5678-9012-3456");
      cardPayment.setAmount(100.0);
      em.persist(cardPayment);

      CashPayment cashPayment = new CashPayment();
      cashPayment.setCode("CASH123");
      cashPayment.setAmount(50.0);
      em.persist(cashPayment);

      em.getTransaction().commit();
    }
  }

  /**
   * This method demonstrates how to create a composition relationship
   * with an association between Category and Field entities.
   *
   * @param emf The EntityManagerFactory used to create EntityManager instances.
   */
  // This method creates instances of Category and Field entities,
  // establishes a many-to-many relationship between them, and persists
  // the entities to the database.
  // It sets the categories for each field and the fields for each category,
  // ensuring that the relationship is bidirectional.
  // The method uses the EntityManager to manage the persistence context and
  // ensure that the entities are saved to the database.
  // The method is designed to be called from the main method to demonstrate
  // the creation of a composition relationship with an association in a JPA
  // context.
  // It is a basic example of how to use JPA to create and persist entities with
  // a composition relationship in a relational database using Hibernate as the
  // JPA provider.
  // The method is annotated with @SuppressWarnings("unused") to indicate that it
  // is intentionally not used in the current context, but it can be uncommented
  // in the main method to execute it and see the results.
  @SuppressWarnings("unused")
  private static void compositionWithAssociation(EntityManagerFactory emf) {
    try (EntityManager em = emf.createEntityManager()) {
      em.getTransaction().begin();

      // Create a new Category instances
      Category category1 = new Category();
      category1.setName("Music");

      Category category2 = new Category();
      category2.setName("Art");

      // Create a new Field instances and associate it with the category1
      Field field1 = new Field();
      field1.setName("History");

      Field field2 = new Field();
      field2.setName("New advences");

      // Set the categories for each field
      field1.setCategories(Set.of(category1, category2));
      field2.setCategories(Set.of(category1, category2));

      // Set the fields for each category
      category1.setFields(Set.of(field1, field2));
      category2.setFields(Set.of(field1, field2));

      // Persist the entities
      em.persist(field1);
      em.persist(field2);

      em.getTransaction().commit();
    }
  }

  /**
   * This method demonstrates how to create a composition relationship
   * with an embeddable Address entity in the Author entity.
   *
   * @param emf The EntityManagerFactory used to create EntityManager instances.
   */
  // This method creates an Author entity with an embedded Address entity,
  // sets the address for the author, persists the author to the database,
  // and commits the transaction.
  // The method uses the EntityManager to manage the persistence context and
  // ensure that the entities are saved to the database.
  // The method is designed to be called from the main method to demonstrate
  // the creation of a composition relationship with an embeddable entity in a JPA
  // context.
  // It is a basic example of how to use JPA to create and persist entities with
  // a composition relationship using an embeddable entity in a relational
  // database
  // using Hibernate as the JPA provider.
  // The method is annotated with @SuppressWarnings("unused") to indicate that it
  // is intentionally not used in the current context, but it can be uncommented
  // in the main method to execute it and see the results.
  @SuppressWarnings("unused")
  private static void compositionWithEmbedable(EntityManagerFactory emf) {
    try (EntityManager em = emf.createEntityManager()) {
      em.getTransaction().begin();

      // Create an Author entity with an embedded Address entity
      Author author = new Author();
      author.setName("William Shakespeare");

      // Create an Address entity and set its properties
      // The Address entity is marked as @Embeddable, allowing it to be embedded
      // within the Author entity.
      // This allows the Address entity to be treated as part of the Author entity,
      // and its fields will be stored in the same table as the Author entity.
      // The Address entity does not have its own identity and is typically used
      // to encapsulate address-related fields within an entity.
      // The Address entity is created and its properties are set.
      Address address = new Address();
      address.setStreet("Stratford-upon-Avon");
      address.setCity("Warwickshire");
      address.setPostalCode("CV37 6QW");

      // Set the address for the author
      // The Author entity has an Address field that is marked with @Embedded,
      // indicating that it contains an embedded Address entity.
      // This allows the Address entity to be stored as part of the Author entity
      // in the same table, and its fields will be mapped to columns in the Author
      // table.
      // The address is set for the author, establishing the composition relationship
      // between the Author and Address entities.
      // The Address entity is embedded within the Author entity, allowing it to be
      // treated as part of the Author entity and its fields to be stored in the same
      // table as the Author entity.
      // The Address entity does not have its own identity and is typically used to
      // encapsulate address-related fields within an entity.
      // The Address entity is created and its properties are set.
      // The Address entity is embedded within the Author entity, allowing it to be
      // treated as part of the Author entity and its fields to be stored in the same
      // table as the Author entity.
      author.setAddress(address);

      // Persist the author entity
      // The Author entity is persisted to the database, which means that it will be
      // saved to the database and its state will be managed by the EntityManager.
      // The EntityManager is responsible for managing the persistence context and
      // ensuring that the entities are saved to the database.
      em.persist(author);

      em.getTransaction().commit();
    }
  }

  /**
   * This method demonstrates how to write a JPQL query
   * to retrieve all BookType entities from the database.
   *
   * @param emf
   */
  // This method uses the EntityManager to create a JPQL query that retrieves
  // all BookType entities from the database. It executes the query and prints
  // the results to the console. The method is designed to be called from the
  // main method to demonstrate the use of JPQL queries in a JPA context.
  // It is a basic example of how to use JPA to perform queries in a relational
  // database using Hibernate as the JPA provider.
  @SuppressWarnings("unused")
  private static void writeJpqlQuery(EntityManagerFactory emf) {
    try (EntityManager em = emf.createEntityManager()) {
      em.getTransaction().begin();

      // Example JPQL query to find all books
      List<BookType> bookTypes = em.createQuery("SELECT bt FROM BookType bt", BookType.class).getResultList();
      for (BookType bookType : bookTypes) {
        System.out.println(bookType);
      }

      em.getTransaction().commit();
    }
  }

  /**
   * This method demonstrates how to write a typed JPQL query
   * to retrieve all BookType entities from the database.
   * It uses the TypedQuery interface to ensure type safety.
   *
   * @param emf
   */
  // This method uses the EntityManager to create a typed JPQL query that
  // retrieves
  // all BookType entities from the database. It uses the TypedQuery interface
  // to ensure type safety, allowing the results to be directly cast to the
  // BookType class. The method executes the query and prints the results to the
  // console. The method is designed to be called from the main method to
  // demonstrate the use of typed JPQL queries in a JPA context.
  // It is a basic example of how to use JPA to perform typed queries in a
  // relational database using Hibernate as the JPA provider.
  @SuppressWarnings("unused")
  private static void writeTypedQueryJpql(EntityManagerFactory emf) {
    try (EntityManager em = emf.createEntityManager()) {
      em.getTransaction().begin();

      // Example typed JPQL query to find all books
      TypedQuery<BookType> query = em.createQuery("SELECT bt FROM BookType bt", BookType.class);
      List<BookType> bookTypes = query.getResultList();
      for (BookType bookType : bookTypes) {
        System.out.println(bookType);
      }

      em.getTransaction().commit();
    }
  }

  /**
   * This method demonstrates how to use JPQL with a WHERE clause
   * to filter results based on specific criteria.
   * It retrieves BookType entities where the subCode matches a specific value
   * and the name contains a specific substring.
   *
   * @param emf
   */
  // This method uses the EntityManager to create a JPQL query that retrieves
  // BookType entities based on specific criteria using a WHERE clause.
  // It executes the query with parameters and prints the results to the console.
  // The method is designed to be called from the main method to demonstrate
  // the use of JPQL with a WHERE clause in a JPA context.
  // It is a basic example of how to use JPA to perform filtered queries in a
  // relational database using Hibernate as the JPA provider.
  @SuppressWarnings("unused")
  private static void writeJpqlWithWhere(EntityManagerFactory emf) {
    try (EntityManager em = emf.createEntityManager()) {
      em.getTransaction().begin();

      // Example typed JPQL query to find all books
      TypedQuery<BookType> query = em.createQuery(
          "SELECT bt FROM BookType bt where bt.subCode = :subCode and bt.name like :name",
          BookType.class);
      query.setParameter("subCode", "SC001");
      query.setParameter("name", "%Fiction%");

      List<BookType> bookTypes = query.getResultList();
      for (BookType bookType : bookTypes) {
        System.out.println(bookType);
      }

      em.getTransaction().commit();
    }
  }

  /**
   * This method demonstrates how to use JPQL to perform joins
   * between entities and retrieve specific fields into a DTO.
   * It includes both INNER JOIN and LEFT JOIN examples.
   *
   * @param emf
   */
  // This method uses the EntityManager to create a JPQL query that joins
  // the Book and Author entities, selecting specific fields into a DTO
  // called BooksAndAuthors.
  // The INNER JOIN retrieves books with their associated authors,
  // while the LEFT JOIN retrieves all books, including those without
  // associated authors.
  // The method is designed to be called from the main method to demonstrate
  // the use of JPQL joins in a JPA context.
  // It is a basic example of how to use JPA to perform joins between
  // entities and retrieve specific fields into a DTO in a relational database
  // using Hibernate as the JPA provider.
  @SuppressWarnings("unused")
  private static void writeJpqlJoin(EntityManagerFactory emf) {
    try (EntityManager em = emf.createEntityManager()) {
      em.getTransaction().begin();

      String jpql = """
            SELECT NEW com.mycompany.app.dto.BooksAndAuthors(book, author, address)
            FROM Book book
            INNER JOIN book.author author
          """;

      // Example typed JPQL query joining Book and Author entities
      // and selecting specific fields into a DTO
      // The query uses an INNER JOIN to retrieve books with their associated authors.
      // The BooksAndAuthors DTO is used to encapsulate the book, author, and address
      // information.
      // The query is executed using a TypedQuery to ensure type safety.
      // The result is a list of BooksAndAuthors objects, each containing the book,
      // author, and address information.
      TypedQuery<BooksAndAuthors> query = em.createQuery(jpql, BooksAndAuthors.class);
      List<BooksAndAuthors> books = query.getResultList();

      // Print the results of the query
      // The results are printed to the console, showing the book name, author name,
      // and address information for each book-author pair.
      // The output will display the book name, author name, and address for each
      // book-author pair retrieved from the database.
      System.out.println("Books and Authors (Using INNER Join):");
      for (BooksAndAuthors book : books) {
        System.out.println(book.book().getName() + " " + book.author().getName() + " " + book.address());
      }

      jpql = """
            SELECT NEW com.mycompany.app.dto.BooksAndAuthors(book, author, address)
            FROM Book book
            LEFT JOIN book.author author
          """;

      // Example typed JPQL query joining Book and Author entities
      // and selecting specific fields into a DTO
      // The query uses a LEFT JOIN to retrieve all books, including those without
      // associated authors.
      // The BooksAndAuthors DTO is used to encapsulate the book, author, and address
      // information.
      // The query is executed using a TypedQuery to ensure type safety.
      // The result is a list of BooksAndAuthors objects, each containing the book,
      // author, and address information.
      // The LEFT JOIN ensures that all books are included in the result, even if they
      // do not have an associated author.
      // The result is a list of BooksAndAuthors objects, each containing the book,
      // author, and address information.onsole, showing the book name, author name,
      query = null;
      query = em.createQuery(jpql, BooksAndAuthors.class);
      books = null;
      books = query.getResultList();

      // Print the results of the query
      // The results are printed to the console, showing the book name, author name,
      // and address information for each book-author pair.
      // The output will display the book name, author name, and address for each
      // book-author pair retrieved from the database, including books without
      // associated authors.
      System.out.println("Books and Authors (Using LEFT Join):");
      for (BooksAndAuthors book : books) {
        System.out.println(book.book().getName() + " " + (book.author() == null ? null : book.author().getName()) + " "
            + (book.address() == null ? null
                : book.address().getStreet()
                    + " " + book.address().getCity() + " " + book.address().getPostalCode()));
      }

      em.getTransaction().commit();
    }
  }

  /**
   * This method demonstrates how to use a named query
   * to retrieve all BookType entities from the database.
   * Named queries are defined in the entity class using the @NamedQuery
   * annotation.
   *
   * @param emf
   */
  // This method uses the EntityManager to create a named query that retrieves
  // all BookType entities from the database. Named queries are defined in the
  // entity class using the @NamedQuery annotation, allowing for reusable and
  // type-safe queries. The method executes the named query and prints the results
  // to the console. The method is designed to be called from the main method to
  // demonstrate the use of named queries in a JPA context.
  // It is a basic example of how to use JPA to perform named queries in a
  // relational database using Hibernate as the JPA provider.
  @SuppressWarnings("unused")
  private static void writeJpqlWithNamedQuery(EntityManagerFactory emf) {
    try (EntityManager em = emf.createEntityManager()) {
      em.getTransaction().begin();

      // Example of using a named query to find all BookType entities
      TypedQuery<BookType> query = em.createNamedQuery("BookType.findAll", BookType.class);
      List<BookType> bookTypes = query.getResultList();
      for (BookType bookType : bookTypes) {
        System.out.println(bookType);
      }

      // Example of using a named query with parameters to find BookType entities
      // with a specific subCode and name pattern
      query = em.createNamedQuery("BookType.findBySubcodeAndName", BookType.class);

      query.setParameter("subCode", "SC002");
      query.setParameter("name", "%Fiction%");

      bookTypes = query.getResultList();
      for (BookType bookType : bookTypes) {
        System.out.println(bookType);
      }
      em.getTransaction().commit();
    }
  }

  /**
   * This method demonstrates how to use aggregate functions
   * in JPQL to perform calculations on entity attributes.
   * It includes examples of COUNT, SUM, MIN, MAX, and AVG functions.
   * 
   * @param emf The EntityManagerFactory used to create EntityManager instances.
   */
  // This method uses the EntityManager to create JPQL queries that utilize
  // aggregate functions such as COUNT, SUM, MIN, MAX, and AVG to perform
  // calculations on entity attributes. It executes the queries and prints the
  // results to the console. The method is designed to be called from the main
  // method to demonstrate the use of aggregate functions in a JPA context.
  // It is a basic example of how to use JPA to perform aggregate calculations in a
  // relational database using Hibernate as the JPA provider.
  @SuppressWarnings("unused")
  private static void aggregateFunctions(EntityManagerFactory emf) {
    try (EntityManager em = emf.createEntityManager()) {
      em.getTransaction().begin();

      // -- Count function example as presented in the lesson 07_04 --
      // This query counts the number of books written by an author with the name
      // "Allen".
      // It uses the COUNT function to return the total number of books that match the
      // criteria.
      // The query is parameterized to allow for dynamic input of the author's name.
      // The result will be a single value representing the count of books.
      // The query is executed using the EntityManager's createQuery method, and the
      // result is printed to the console.
      String sql = "SELECT COUNT(b) FROM Book b WHERE b.author.name = :name";
      Query query = em.createQuery(sql);
      query.setParameter("name", "Allen");
      Long numberOfBooks = (Long) query.getSingleResult();
      System.out.println("Number of books for Author: " + numberOfBooks);
      query = null;

      // -- Count function example using named query --
      // This uses a named query, Book.findCountByAuthor, located
      // in the Book entity. The named query is exactly the same as
      // the JPQL above.
      // This example is provided to reinforce lesson 07_03 and to
      // provide more experience using named queries.
      query = em.createNamedQuery("Book.findCountByAuthor");
      query.setParameter("name", "Allen");
      numberOfBooks = (Long) query.getSingleResult();
      System.out.println("Number of books for Author (using named query): " + numberOfBooks);
      query = null;

      // -- SUM function use as presented in lesson 07_04 --
      // This query calculates the total price of books written by an author with
      // the name "Jane".
      // It uses the SUM function to return the total price of all books that match
      // the criteria.
      // The query is parameterized to allow for dynamic input of the author's name.
      // The result will be a single value representing the total price of books.
      // The query is executed using the EntityManager's createQuery method, and the
      // result is printed to the console.
      sql = "SELECT SUM(b.price) FROM Book b where b.author.name = :name";
      TypedQuery<Double> query2 = em.createQuery(sql, Double.class);
      query2.setParameter("name", "Jane");
      Double totalPrice = query2.getSingleResult();
      System.out.println("Total proce of book for Author: " + totalPrice);
      query2 = null;

      // -- SUM function example using named query --
      // This uses a named query, Book.findTotalPriceByAuthor, located
      // in the Book entity. The named query is exactly the same as
      // the JPQL above.
      // This example is provided to reinforce lesson 07_03 and to
      // provide more experience using named queries.
      query2 = em.createNamedQuery("Book.findTotalPriceByAuthor", Double.class);
      query2.setParameter("name", "Jane");
      totalPrice = query2.getSingleResult();
      System.out.println("Total price of books for Author (using named query): " + totalPrice);

      // -- MIN function use as presented in lesson 07_04 --
      // This query retrieves the minimum rating of a book written by an author
      // with the name "Book1".
      // It uses the MIN function to return the lowest rating of all books that match
      // the criteria.
      // The query is parameterized to allow for dynamic input of the book name.
      // The result will be a single value representing the minimum rating.
      // The query is executed using the EntityManager's createQuery method, and the
      // result is printed to the console.
      sql = "SELECT MIN(r.rating) FROM Review r WHERE r.book.name = :name";
      TypedQuery<Integer> query3 = em.createQuery(sql, Integer.class);
      query3.setParameter("name", "Book1");
      Integer rating = query3.getSingleResult();
      System.out.println("Min rating of Book for Author: " + rating);

      // - MIN function example using named query --
      // This uses a named query, Review.findMinRatingByBookName, located
      // in the Review entity. The named query is exactly the same as
      // the JPQL above.
      // This example is provided to reinforce lesson 07_03 and to
      // provide more experience using named queries.
      query3 = em.createNamedQuery("Review.findMinRatingByBookName", Integer.class);
      query3.setParameter("name", "Book1");
      rating = query3.getSingleResult();
      System.out.println("Min rating of Book for Author (using named query): " + rating);
      
      // -- MAX function use as presented in lesson 07_04 --
      // This query retrieves the maximum rating of a book written by an author
      // with the name "Book1".
      // It uses the MAX function to return the highest rating of all books that match
      // the criteria.
      // The query is parameterized to allow for dynamic input of the book name.
      // The result will be a single value representing the maximum rating.
      // The query is executed using the EntityManager's createQuery method, and the
      // result is printed to the console.
      sql = "SELECT MAX(r.rating) FROM Review r WHERE r.book.name = :name";
      query3 = em.createQuery(sql, Integer.class);
      query3.setParameter("name", "Book1");
      rating = query3.getSingleResult();
      System.out.println("Max rating of Book for Author: " + rating);

      // -- MAX function example using named query --
      // This uses a named query, Review.findMaxRatingByBookName, located
      // in the Review entity. The named query is exactly the same as
      // the JPQL above.
      // This example is provided to reinforce lesson 07_03 and to
      // provide more experience using named queries.
      query3 = em.createNamedQuery("Review.findMaxRatingByBookName", Integer.class);
      query3.setParameter("name", "Book1");
      rating = query3.getSingleResult();
      System.out.println("Max rating of Book for Author (using named query): " + rating);

      // -- AVG function use as presented in lesson 07_04 --
      // This query retrieves the average rating of a book written by an author
      // with the name "Book1".
      // It uses the AVG function to return the average rating of all books that match
      // the criteria.
      // The query is parameterized to allow for dynamic input of the book name.
      // The result will be a single value representing the average rating.
      // The query is executed using the EntityManager's createQuery method, and the
      // result is printed to the console.
      sql = "SELECT AVG(r.rating) FROM Review r WHERE r.book.name = :name";
      query2 = em.createQuery(sql, Double.class);
      query2.setParameter("name", "Book1");
      Double avgRating = query2.getSingleResult();
      System.out.println("Average rating of Book for Author: " + avgRating);

      // -- AVG function example using named query --
      // This uses a named query, Review.findAvgRatingByBookName, located
      // in the Review entity. The named query is exactly the same as
      // the JPQL above.
      // This example is provided to reinforce lesson 07_03 and to
      // provide more experience using named queries.
      query2 = em.createNamedQuery("Review.findAvgRatingByBookName", Double.class);
      query2.setParameter("name", "Book1");
      avgRating = query2.getSingleResult();
      System.out.println("Average rating of Book for Author (using named query): " + avgRating);

      em.getTransaction().commit();
    }
  }

  /**
   * This method demonstrates how to use the ORDER BY clause
   * in JPQL to sort the results of a query.
   * It includes examples of ordering by author name in both ascending 
   * and descending order.
   * 
   * @param emf
   */
  // This method uses the EntityManager to create JPQL queries that utilize
  // the ORDER BY clause to sort the results of a query. It executes the queries
  // and prints the results to the console. The method is designed to be called
  // from the main method to demonstrate the use of the ORDER BY clause in a JPA
  // context. It is a basic example of how to use JPA to perform ordered queries
  // in a relational database using Hibernate as the JPA provider.
  @SuppressWarnings("unused")
  private static void orderByClause(EntityManagerFactory emf) {
    try (EntityManager em = emf.createEntityManager()) {
      em.getTransaction().begin();

      // Example of using ORDER BY clause in JPQL
      String jpql = """
          SELECT NEW com.mycompany.app.dto.BooksAndAuthors(b, a, address) FROM
          Book b LEFT JOIN b.author a
          ORDER BY author.name
          """;
      
      TypedQuery<BooksAndAuthors> query = em.createQuery(jpql, BooksAndAuthors.class);
      List<BooksAndAuthors> result = query.getResultList();

      System.out.println("Books and Authors (Ordered by Author Name Ascending):");
      for (BooksAndAuthors r : result) {
        System.out.println(r.author() + " " + r.book());
      }

      jpql = """
          SELECT NEW com.mycompany.app.dto.BooksAndAuthors(b, a, address) FROM
          Book b LEFT JOIN b.author a
          ORDER BY author.name DESC
          """;

      query = em.createQuery(jpql, BooksAndAuthors.class);
      result = query.getResultList();

      System.out.println("Books and Authors (Ordered by Author Name Descending):");
      for (BooksAndAuthors r : result) {
        System.out.println(r.author() + " " + r.book());
      }      

      em.getTransaction().commit();
    }
  }

  /**
   * This method demonstrates how to use the GROUP BY clause
   * in JPQL to group results based on specific attributes.
   * It includes examples of grouping by book name and author name,
   * and calculating average ratings for each group.
   * 
   * @param emf
   */
  // This method uses the EntityManager to create JPQL queries that utilize
  // the GROUP BY clause to group results based on specific attributes.
  // It executes the queries and prints the results to the console. The method is
  // designed to be called from the main method to demonstrate the use of the GROUP
  // BY clause in a JPA context. It is a basic example of how to use JPA to perform
  // grouped queries in a relational database using Hibernate as the JPA provider.
  @SuppressWarnings("unused")
  private static void groupByClause(EntityManagerFactory emf) {
    try (EntityManager em = emf.createEntityManager()) {
      em.getTransaction().begin();

      // Example of using GROUP BY clause in JPQL
      String jpql = """
          SELECT r.book.name, AVG(r.rating) FROM
          Review r
          GROUP BY r.book.name
          """;

      // Create a typed query to retrieve average ratings by book name
      // This query groups the reviews by book name and calculates the average rating
      // for each book. The result is a list of Object arrays, where each array contains
      // the book name and its corresponding average rating.
      TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
      System.out.println("Average ratings by book name:");
      query.getResultList().forEach(o -> System.out.println("Average rating by book " + o[0] + " " + o[1]));
      
      jpql = """
          SELECT r.book.name, AVG(r.rating) FROM
          Review r
          GROUP BY r.book.author.name
          """;

      // Create a typed query to retrieve average ratings by author name
      // This query groups the reviews by author name and calculates the average rating
      // for each author. The result is a list of Object arrays, where each array contains
      // the author name and its corresponding average rating.
      query = em.createQuery(jpql, Object[].class);
      System.out.println("Average ratings by author name:");
      query.getResultList().forEach(o -> System.out.println("Average rating by author " + o[0] + " " + o[1]));

      em.getTransaction().commit();
    }
  }

  /**
   * This method demonstrates how to use the HAVING clause
   * in JPQL to filter results after aggregation.
   * It includes an example of filtering authors based on the average rating
   * of their books.
   * 
   * @param emf
   */
  // This method uses the EntityManager to create a JPQL query that utilizes
  // the HAVING clause to filter results after aggregation. It executes the query
  // and prints the results to the console. The method is designed to be called
  // from the main method to demonstrate the use of the HAVING clause in a JPA
  // context. It is a basic example of how to use JPA to perform filtered
  // aggregation queries in a relational database using Hibernate as the JPA
  // provider.
  @SuppressWarnings("unused")
  private static void havingClause(EntityManagerFactory emf) {
    try (EntityManager em = emf.createEntityManager()) {
      em.getTransaction().begin();

      // Example of using HAVING clause in JPQL
      String jpql = """
          SELECT r.book.author.name, AVG(r.rating) 
          FROM Review r
          GROUP BY r.book.author.name
          HAVING AVG(r.rating) > 3
          """;

      // Create a typed query to retrieve authors with average ratings greater than 3.0
      // This query groups the reviews by author name and filters the results to include
      // only those authors whose average rating is greater than 3.0. The result is
      // a list of author names.
      System.out.println("Authors with average rating greater than 3:");
      TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
      query.getResultList().forEach(o -> System.out.println("Average rating by author: " + o[0] + " " + o[1]));

      em.getTransaction().commit();
    }
  }

  /**
   * This method demonstrates how to use native SQL queries
   * to retrieve entities directly from the database.
   * It includes an example of retrieving all BookType entities
   * using a native SQL query.
   * 
   * @param emf
   */
  // This method uses the EntityManager to create a native SQL query that retrieves
  // entities directly from the database. It executes the query and prints the results
  // to the console. The method is designed to be called from the main method to
  // demonstrate the use of native SQL queries in a JPA context. It is a basic
  // example of how to use JPA to perform native SQL queries in a relational database
  // using Hibernate as the JPA provider.
  @SuppressWarnings({"unused", "unchecked"})
  private static void nativeQueries(EntityManagerFactory emf) {
    try (EntityManager em = emf.createEntityManager()) {
      em.getTransaction().begin();

      // Example of using a native SQL query to retrieve all BookType entities
      Query q = em.createNativeQuery("SELECT * FROM book", Book.class);
      q.getResultList().forEach(b -> System.out.println(b));

      em.getTransaction().commit();
    }
  }

  /**
   * This method demonstrates how to use the Criteria API
   * to create type-safe queries in JPA.
   * It includes an example of retrieving all BookType entities
   * using the Criteria API.
   *
   * @param emf The EntityManagerFactory used to create EntityManager instances.
   */
  // This method uses the Criteria API to create a type-safe query that retrieves
  // all BookType entities from the database. It executes the query and prints the
  // results to the console. The method is designed to be called from the main
  // method to demonstrate the use of the Criteria API in a JPA context.
  // It is a basic example of how to use JPA to perform type-safe queries in a
  // relational database using Hibernate as the JPA provider.
  //
  // Criteria Quries are used to create type-safe queries in JPA.
  // They provide a way to construct queries using a fluent API,
  // allowing for dynamic query construction and execution.
  // The Criteria API is typically used when you need to build complex queries
  // or when you want to avoid using string-based JPQL queries.
  @SuppressWarnings("unused")
  private static void criteriaQueries(EntityManagerFactory emf) {
    try (EntityManager em = emf.createEntityManager()) {
      em.getTransaction().begin();

      // --- Get list of all BookType entities using Criteria API ---

      // Example of using Criteria API to retrieve all BookType entities
      // The CriteriaBuilder is used to create CriteriaQuery objects,
      // which represent queries in a type-safe manner.
      // It provides methods to construct queries using a fluent API,
      // allowing for dynamic query construction and execution.
      // The CriteriaBuilder is typically obtained from the EntityManager,
      // and it is used to create CriteriaQuery objects that can be executed
      // to retrieve entities from the database.
      CriteriaBuilder b = em.getCriteriaBuilder();

      // Create a CriteriaQuery object for the BookType entity
      // The CriteriaQuery represents a query that can be executed to retrieve
      // entities from the database. It is a type-safe representation of a query,
      // allowing for dynamic query construction and execution.
      // The CriteriaQuery is created using the CriteriaBuilder, which provides methods
      // to construct queries in a type-safe manner.
      // The CriteriaQuery is parameterized with the type of entity to be retrieved,
      // in this case, BookType. This allows the query to be executed and return a
      // list of BookType entities from the database.
      // The CriteriaQuery is used to define the structure of the query, including
      // the selection of entities, the conditions for filtering, and the ordering of
      // results.
      CriteriaQuery<BookType> cq = b.createQuery(BookType.class);
      
      // Create a root for the BookType entity
      // The Root represents the entity type in the query and is used to define
      // the selection of entities and the conditions for filtering.
      // It is a type-safe representation of the entity type in the query,
      // allowing for dynamic query construction and execution.
      // The Root is created from the CriteriaQuery, and it represents the entity
      // type that will be queried. In this case, it represents the BookType entity.
      // The Root is used to define the selection of entities, the conditions for
      // filtering, and the ordering of results in the query.
      // The Root is typically used in conjunction with the CriteriaBuilder to
      // construct queries in a type-safe manner.
      Root<BookType> bookTypeRoot = cq.from(BookType.class);

      // Select the BookType entity from the root
      // The CriteriaQuery is used to define the structure of the query,
      // including the selection of entities, the conditions for filtering, and
      // the ordering of results.
      // The select method is called on the CriteriaQuery to specify that the
      // query should return BookType entities. This is done by passing the root
      // of the query, which represents the BookType entity, to the select method.
      // The select method is used to define the selection of entities in the query,
      // allowing for dynamic query construction and execution.
      cq.select(bookTypeRoot);

      // The CriteriaQuery is then executed to retrieve the list of BookType entities
      // from the database.
      TypedQuery<BookType> q1 = em.createQuery(cq);
      List<BookType> bookTypes = q1.getResultList();

      System.out.println("List of BookType entities:");
      // `System.out::println` is the same as typing `System.out.println(...)`
      // `::` is a method reference in Java, which allows you to refer to a method without executing it.
      // It is a shorthand for lambda expressions.
      // In this case, it is used to print each BookType object in the list.
      bookTypes.forEach(System.out::println);
      System.out.println("");

      // --- Get list of all BookType names using Criteria API ---
      CriteriaQuery<String> cq2 = b.createQuery(String.class);
      bookTypeRoot = cq2.from(BookType.class);

      // The `get()` method is used to specify the field to select from the entity.
      // In this case, it selects the "name" field from the BookType entity.
      cq2.select(bookTypeRoot.get("name"));

      TypedQuery<String> q2 = em.createQuery(cq2);
      List<String> bookTypeNames = q2.getResultList();

      System.out.println("List of BookType names:");
      bookTypeNames.forEach(System.out::println);;
      System.out.println("");

      // --- Get list of BookType names and codes using Criteria API ---
      CriteriaQuery<Object[]> cq3 = b.createQuery(Object[].class);
      bookTypeRoot = cq3.from(BookType.class);

      // Specify multiple fields to select from the entity.
      // In this case, it selects both the "name" and "code" fields from the BookType entity.
      // The `multiselect()` method is used to specify multiple fields to select from the entity.
      // It allows you to select multiple fields in a single query, returning
      // an array of objects for each result.
      cq3.multiselect(bookTypeRoot.get("name"), bookTypeRoot.get("code"));

      TypedQuery<Object[]> q3 = em.createQuery(cq3);
      List<Object[]> bookTypeNameandCode = q3.getResultList();

      System.out.println("List of BookType names and codes:");
      bookTypeNameandCode.forEach(r -> System.out.println(r[0] + " " + r[1]));
      System.out.println("");

      // --- Get list of Books with price greater than 1000, ordered by price descending using Criteria API ---
      cq3 = b.createQuery(Object[].class);
      Root<Book> bookRoot = cq3.from(Book.class);

      // The `get()` method is used to specify the field to select from the entity.
      // In this case, it selects the "name" field from the BookType entity.
      cq3.multiselect(bookRoot.get("id"), bookRoot.get("name"), bookRoot.get("price"))
          .where(b.gt(bookRoot.get("price"), 1000))
          .orderBy(b.desc(bookRoot.get("price")));

      TypedQuery<Object[]> q4 = em.createQuery(cq3);
      List<Object[]> books = q4.getResultList();

      System.out.println("List of Books with price greater than 1000, ordered by price descending:");
      books.forEach(r -> System.out.println(r[0] + " " + r[1] + " " + r[2]));
      System.out.println("");
      
      /*
       * To acheive a dynamic query, we can use the CriteriaBuilder to create
       * a CriteriaQuery object that represents the query structure.
       * The CriteriaBuilder provides methods to construct queries in a type-safe
       * manner, allowing for dynamic query construction and execution.
       * The CriteriaQuery is parameterized with the type of entity to be retrieved,
       * in this case, Object[]. This allows the query to be executed and return a
       * 
       * We can specify the `WHERE` and `ORDER BY` clauses using the CriteriaQuery
       * object, like so:
       * `cq3.where(b.gt(bookRoot.get("price"), 1000))` and
       * `cq3.orderBy(b.desc(bookRoot.get("price")))`.
       * 
       * Then we can wrap the `WHERE` clause in some sort of logical structure, using
       * an `IF...THEN...ELSE` statement
       * or even a switch statement, to dynamically add conditions based on user input
       * or other criteria.
       * 
       * The `GROUP BY` clause can also be added using the `groupBy()` method on the
       * CriteriaQuery object. And the
       * `HAVING` clause can be added using the `having()` method on the CriteriaQuery
       * object.
       * 
       * Check out the JPA Criteria API documentation for more details on how to use
       * these methods.
       * https://docs.oracle.com/javaee/7/api/javax/persistence/criteria/
       */

       em.getTransaction().commit();
    }
  }
}