package com.example.simpleTaskTracker.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.simpleTaskTracker.model.Task;


@RestController
@RequestMapping("/api/tasks")
public class SimpleTaskController {
	 
    private List<Task> tasks = new ArrayList<>();

    
    @PostMapping
    public Task addTask(@RequestParam("id") int id, @RequestParam("name") String name, @RequestParam("completed") boolean completed) {
        Task newTask = new Task(id, name, completed);
        tasks.add(newTask);
        return newTask;
    }

    @DeleteMapping 
    public void deleteTask(@RequestParam("id") int id) {
    	int pos = 0;
    	
    	for(int i=0; i<tasks.size();i++) {
    		if(tasks.get(i).getId()==id) {
    			pos = i;
    			break;
    		}
    	}
    	
    	tasks.remove(pos);
    }
    
    @PutMapping
    public ResponseEntity<Task> completeTask(@RequestParam("id") int id) {
    	for (Task task : tasks) {
            if (task.getId() == id) {
                task.setCompleted(true);
                return ResponseEntity.ok(task);
            }
        }
        return ResponseEntity.notFound().build();
    
    	
    }
   
    @GetMapping
    public List<Task> getAllTasks() {
        return tasks;
    }
}

