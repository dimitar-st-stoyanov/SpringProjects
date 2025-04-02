package com.example.dss_todoapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dss_todoapp.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long>{

}
