package com.example.dss_voting_app.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Vote {
	private Long pollId;
	private int optionIndex;
	
	

}
