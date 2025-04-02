package com.example.dss_todoapp.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.dss_todoapp.model.Task;
import com.example.dss_todoapp.service.TaskService;

@Controller

public class TaskController {

	private final TaskService service;
	
	public TaskController(TaskService service) {
		this.service = service;
	}


	@GetMapping
	public String showAllTasks(Model model) {
		List<Task> tasks=service.getAllTasks();
		model.addAttribute("tasks", tasks);
		return "tasks";
	}
	
	@PostMapping
	public String addNewTask(@RequestParam String title) {
		service.addTask(title);
		return "redirect:/";
		
	}
	
}
