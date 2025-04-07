package com.example.dss_voting_app.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dss_voting_app.model.Poll;
import com.example.dss_voting_app.service.PollService;

@RestController
@RequestMapping("/api/polls")
public class PollController {
	
	private final PollService pollService;
	
	
	public PollController(PollService pollService) {
		super();
		this.pollService = pollService;
	}



	@PostMapping
	public Poll createPoll (@RequestBody Poll poll) {
		return pollService.createPoll(poll);
	}
	
	@GetMapping
	public List<Poll> getAllPolls(){
		return pollService.getAllPolls();
	}
	
	@GetMapping("/{id}")
	public Optional<Poll> getPoll(@PathVariable Long id){
		return pollService.getPoll(id);
		
	}

}
