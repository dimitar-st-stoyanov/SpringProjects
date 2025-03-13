package com.erpdesing.dss_erp_system.rawmaterials.bars;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class BarsController {
private final BarsRepository barsRepository;
	
	public BarsController(BarsRepository barsRepository) {
		this.barsRepository = barsRepository;
	}
	
	@GetMapping("/bars")
	public Iterable<Bars> findAllBars(){
		return this.barsRepository.findAll();
	}
	
	@PostMapping("/bars")
	public Bars addBar(@RequestBody Bars bar) {
		return this.barsRepository.save(bar);
	}
}
