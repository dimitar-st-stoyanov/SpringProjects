package com.example.dss_voting_app.service;

import org.springframework.stereotype.Service;

import com.example.dss_voting_app.model.Poll;
import com.example.dss_voting_app.repository.PollRepository;

@Service
public class PollService {
	
	private final PollRepository pollRepository;
	
	
	public PollService(PollRepository pollRepository) {
		
		this.pollRepository = pollRepository;
	}


	public Poll createPoll(Poll poll) {
		return pollRepository.save(poll);
	}
	

}
