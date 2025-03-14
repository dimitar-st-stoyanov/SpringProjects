package com.erpdesing.dss_erp_system.rawmaterials.rectagulattubes;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recttubes")
@CrossOrigin("*")
public class RectTubeController {
	private final RectTubeService rectTubeService;
	
	public RectTubeController(RectTubeService rectTubeService) {
		this.rectTubeService = rectTubeService;
	}
	
	@GetMapping
	public ResponseEntity<List<RectTube>> findAllRectTubes(){
		return ResponseEntity.ok(rectTubeService.getAllRectTubes());
	}
	
	@PostMapping
	public ResponseEntity<RectTube> addRectTube(@RequestBody RectTube rectTube){
		RectTube savedRectTube = rectTubeService.addRectTube(rectTube);
		return ResponseEntity.status(201).body(savedRectTube);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteRectTube(@PathVariable int id) {
        boolean isDeleted = rectTubeService.deleteRectTube(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build(); // 204 No Content if deleted
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found if sheet not found
        }
    }
}
