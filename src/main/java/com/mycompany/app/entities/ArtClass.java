package com.mycompany.app.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "class")
public class ArtClass {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "class_id", nullable = false)
  private int id;
  
  @Column(name = "class_name")
  private String name;

  @Column(name = "day_of_week")
  private String dayOfWeek;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "teacher_id")
  private Teacher teacher;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "student_class", joinColumns = @JoinColumn(name = "class_id"), inverseJoinColumns = @JoinColumn(name = "student_id"))
  List<Student> students;

    public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDayOfWeek() {
    return dayOfWeek;
  }

  public void setDayOfWeek(String dayOfWeek) {
    this.dayOfWeek = dayOfWeek;
  }

  public Teacher getTeacher(){
    return this.teacher;
  }

  public void setTeacher(Teacher teacher){
    this.teacher = teacher;
  }

  /**
   * @return the students
   */
  public List<Student> getStudents() {
    return students;
  }

  /**
   * @param students the students to set
   */
  public void setStudents(List<Student> students) {
    this.students = students;
  }
}
