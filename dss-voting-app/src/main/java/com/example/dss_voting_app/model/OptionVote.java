package com.example.dss_voting_app.model;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class OptionVote {
	
	private String option;
	private Long voteCount = 0L;
	
	

}
