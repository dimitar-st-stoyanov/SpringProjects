package com.erpdesing.dss_erp_system.rawmaterials.sheets;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class SheetsService {

    private final SheetsRepository sheetsRepository;

    public SheetsService(SheetsRepository sheetsRepository) {
        this.sheetsRepository = sheetsRepository;
    }

    public List<Sheets> getAllSheets() {
        return (List<Sheets>) sheetsRepository.findAll();
    }

    public Sheets addSheet(Sheets sheet) {
        return sheetsRepository.save(sheet);
    }
    
    public boolean deleteSheet(int id) {
        Optional<Sheets> sheet = sheetsRepository.findById(id);
        if (sheet.isPresent()) {
            sheetsRepository.deleteById(id); 
            return true;
        } else {
            return false;
        }
    }
}
