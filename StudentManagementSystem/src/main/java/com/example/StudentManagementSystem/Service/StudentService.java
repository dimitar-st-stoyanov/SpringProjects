package com.example.StudentManagementSystem.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.StudentManagementSystem.Model.Student;
import com.example.StudentManagementSystem.Repository.StudentRepository;

@Service
public class StudentService {
	
	private StudentRepository repository;
	
	public StudentService(StudentRepository repository) {
		this.repository = repository;
		
	}
	
	public List<Student> getAllStudents() {
		return repository.findAll();
	}
	
	public Optional<Student> getStudentById(Long id) {
		return repository.findById(id);
	}
	
	public Student addStudent(Student student) {
		return repository.save(student);
	}
	
	public void deteleStudentById(long id) {
		repository.deleteById(id);
	}
}
