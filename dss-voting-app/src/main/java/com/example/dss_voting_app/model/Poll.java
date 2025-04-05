package com.example.dss_voting_app.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Poll {

	@Id
	@GeneratedValue (strategy=GenerationType.IDENTITY)
	private Long id;
	private String question;
	
	@ElementCollection
	private List<String> options = new ArrayList<>();
	
	@ElementCollection
	private List<String> votes = new ArrayList<>();
	
	
	
	
	
}
