package com.example.dss_todoapp.service;

import java.util.List;

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

	public void addTask(String title) {
		Task task = new Task();
		task.setTitle(title);
		task.setCompleted(false);
		taskRepository.save(task);
		
	}

}
