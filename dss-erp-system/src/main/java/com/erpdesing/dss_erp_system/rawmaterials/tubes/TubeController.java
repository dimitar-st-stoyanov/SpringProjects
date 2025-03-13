package com.erpdesing.dss_erp_system.rawmaterials.tubes;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TubeController {
	private final TubeRepository tubeRepository;
	
	public TubeController(TubeRepository tubeRepository) {
		this.tubeRepository = tubeRepository;
	}
	
	@GetMapping("/tubes")
	public Iterable<Tube> findAllTubes(){
		return this.tubeRepository.findAll();
	}
	
	@PostMapping("/tubes")
	public Tube addTube(@RequestBody Tube tube) {
		return this.tubeRepository.save(tube);
	}
}
