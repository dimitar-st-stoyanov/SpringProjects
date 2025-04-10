package com.example.dss_voting_app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.dss_voting_app.model.OptionVote;
import com.example.dss_voting_app.model.Poll;
import com.example.dss_voting_app.repository.PollRepository;

@Service
public class PollService {
	
	private final PollRepository pollRepository;
	
	
	public PollService(PollRepository pollRepository) {
		
		this.pollRepository = pollRepository;
	}


	public Poll createPoll(Poll poll) {
		poll.setId(null);
		return pollRepository.save(poll);
	}


	public List<Poll> getAllPolls() {
		
		return pollRepository.findAll();
	}


	public Optional<Poll> getPoll(Long id) {
		
		return pollRepository.findById(id);
	}


	public void vote(Long pollId, int optionIndex) {
		
		Poll currentPoll = pollRepository.findById(pollId)
				.orElseThrow(()-> new RuntimeException("Poll not found"));
		
		List<OptionVote> optionsList = currentPoll.getOptions();
		
		if(optionIndex<0 || optionIndex>=optionsList.size()) {
			throw new IllegalArgumentException("Invalid option index");
		}
		
		OptionVote selectedOption = optionsList.get(optionIndex);
		
		selectedOption.setVoteCount(selectedOption.getVoteCount()+1);
		
		pollRepository.save(currentPoll);
		
				
	}
	
	
	

}
