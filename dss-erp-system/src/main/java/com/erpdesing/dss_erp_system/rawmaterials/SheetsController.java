package com.erpdesing.dss_erp_system.rawmaterials;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SheetsController {
	private final SheetsRepository sheetRepository;
	
	public SheetsController(SheetsRepository sheetRepository) {
		this.sheetRepository = sheetRepository;
	}
	
	@GetMapping("/sheets")
	  public Iterable<Sheets> findAllEmployees() {
	    return this.sheetRepository.findAll();
	  }

	  @PostMapping("/Sheets")
	  public Sheets addOneEmployee(@RequestBody Sheets sheet) {
	    return this.sheetRepository.save(sheet);
	  }
}
