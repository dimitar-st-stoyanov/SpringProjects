package com.example.simpleTaskTracker.model;

public class Task {
	private String name;
    private boolean completed;
    private int id;

    // Constructor
    public Task(int id, String name, boolean completed) {
    	this.setId(id);
    	this.setName(name);
        this.setCompleted(completed);
        
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
