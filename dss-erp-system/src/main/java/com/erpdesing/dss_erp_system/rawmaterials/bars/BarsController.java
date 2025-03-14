package com.erpdesing.dss_erp_system.rawmaterials.bars;

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

import com.erpdesing.dss_erp_system.rawmaterials.sheets.Sheets;


@RestController
@RequestMapping("/bars")
@CrossOrigin("*")
public class BarsController {
private final BarsService barsService;
	
	public BarsController(BarsService barsService) {
		this.barsService = barsService;
	}
	
	@GetMapping
    public ResponseEntity<List<Bars>> findAllBars() {
        return ResponseEntity.ok(barsService.getAllBars());
    }
	
	@PostMapping
	 public ResponseEntity<Bars> addBar(@RequestBody Bars bar) {
        Bars savedBar = barsService.addBar(bar);
        return ResponseEntity.status(201).body(savedBar);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBar(@PathVariable int id) {
        boolean isDeleted = barsService.deleteBar(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build(); // 204 No Content if deleted
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found if sheet not found
        }
    }
}
