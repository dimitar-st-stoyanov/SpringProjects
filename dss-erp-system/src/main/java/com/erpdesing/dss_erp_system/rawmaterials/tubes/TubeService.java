package com.erpdesing.dss_erp_system.rawmaterials.tubes;


import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class TubeService {

	private final TubeRepository tubeRepository;
	
	public TubeService(TubeRepository tubeRepository) {
		this.tubeRepository = tubeRepository;
	}
	
	public List<Tube>getAllTubes(){
		return (List<Tube>) tubeRepository.findAll();
	}
	
	public Tube addTube(Tube tube) {
		return tubeRepository.save(tube);
	}
	
	public boolean deleteTube(int id) {
		Optional<Tube> tube = tubeRepository.findById(id);
		if(tube.isPresent()) {
			tubeRepository.deleteById(id);
			return true;
		}else {
			return false;
		}
	}
}
