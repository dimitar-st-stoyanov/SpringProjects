package com.example.StudentManagementSystem.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.StudentManagementSystem.Model.Student;
import com.example.StudentManagementSystem.Service.StudentService;

@RestController
@RequestMapping ("/students")
public class StudentManagementSystemCotroller {
	 
	 private StudentService service;
	 
	 public StudentManagementSystemCotroller(StudentService service) {
		 this.service = service;
		 }
	 
	 @GetMapping
	 public List<Student> getAllStudents() {
		 return service.getAllStudents();
	 }
	 
	 @GetMapping("/{id}")
	 public Optional<Student> getStudentById(@PathVariable Long id) {
		 return service.getStudentById(id);
	 }
	 
	 @PostMapping
	 public Student addStudent(@RequestBody Student newStudent){
		 return service.addStudent(newStudent);
	 }
	 
	 @DeleteMapping("/{id}")
	 public void deteleStudent(@PathVariable Long id) {
		 service.deteleStudentById(id);
	 }
	 
	 
}
