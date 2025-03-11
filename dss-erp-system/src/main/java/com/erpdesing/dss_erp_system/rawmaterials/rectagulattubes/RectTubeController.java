package com.erpdesing.dss_erp_system.rawmaterials.rectagulattubes;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RectTubeController {
	private final RectTubeRepository rectTubeRepository;
	
	public RectTubeController(RectTubeRepository rectTubeRepository) {
		this.rectTubeRepository = rectTubeRepository;
	}
	
	@GetMapping("/recttubes")
	public Iterable<RectTube> findAllRectTubes(){
		return this.rectTubeRepository.findAll();
	}
	
	@PostMapping("/recttubes")
	public RectTube addRectTube(@RequestBody RectTube rectTube) {
		return this.rectTubeRepository.save(rectTube);
	}
}
