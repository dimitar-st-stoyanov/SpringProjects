package com.erpdesing.dss_erp_system.rawmaterials.tubes;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tubes")
@CrossOrigin("*")
public class TubeController {
	private final TubeService tubeService;
	
	public TubeController(TubeService tubeService) {
		this.tubeService = tubeService;
	}
	
	@GetMapping
	
	public ResponseEntity <List<Tube>> findAllTubes(){
		return ResponseEntity.ok(tubeService.getAllTubes());
	}
	
	@PostMapping
	public ResponseEntity<Tube> addTube(@RequestBody Tube tube){
		Tube savedTube = tubeService.addTube(tube);
		return ResponseEntity.status(201).body(savedTube);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTube(@PathVariable int id){
		boolean isDeleted = tubeService.deleteTube(id);
		if(isDeleted) {
			return ResponseEntity.noContent().build();
		}else {
			return ResponseEntity.notFound().build();
		}
	}
}
