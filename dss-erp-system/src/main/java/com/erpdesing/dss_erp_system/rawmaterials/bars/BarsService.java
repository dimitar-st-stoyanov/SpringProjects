package com.erpdesing.dss_erp_system.rawmaterials.bars;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.erpdesing.dss_erp_system.rawmaterials.sheets.Sheets;
import com.erpdesing.dss_erp_system.rawmaterials.sheets.SheetsRepository;

@Service
public class BarsService {
	  private final BarsRepository barsRepository;

	    public BarsService(BarsRepository barsRepository) {
	        this.barsRepository = barsRepository;
	    }

	    public List<Bars> getAllBars() {
	        return (List<Bars>) barsRepository.findAll();
	    }

	    public Bars addBar(Bars bar) {
	        return barsRepository.save(bar);
	    }
	    
	    public boolean deleteBar(int id) {
	        Optional<Bars> bar = barsRepository.findById(id);
	        if (bar.isPresent()) {
	            barsRepository.deleteById(id); 
	            return true;
	        } else {
	            return false;
	        }
	    }
}
