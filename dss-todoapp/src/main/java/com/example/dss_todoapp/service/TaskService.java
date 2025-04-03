package com.example.dss_todoapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.dss_todoapp.model.Task;
import com.example.dss_todoapp.repository.TaskRepository;

@Service
public class TaskService {

	private final TaskRepository taskRepository;
	
	public TaskService(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}
	
	public List<Task> getAllTasks() {
		
		return taskRepository.findAll();
	}

	public void createTask(String title) {
		Task task = new Task();
		task.setTitle(title);
		task.setCompleted(false);
		taskRepository.save(task);
		
	}

	public void deleteTask(Long id) {
		taskRepository.deleteById(id);
		
	}

	public void toggleTask(Long id) {
		Task task =  taskRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Task id"));
		
		task.setCompleted(!task.isCompleted());
		taskRepository.save(task);
	}

}
