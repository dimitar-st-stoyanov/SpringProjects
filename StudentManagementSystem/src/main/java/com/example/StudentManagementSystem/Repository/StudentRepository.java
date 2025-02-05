package com.example.StudentManagementSystem.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.StudentManagementSystem.Model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

}
