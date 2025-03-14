package com.erpdesing.dss_erp_system.rawmaterials.rectagulattubes;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class RectTubeService {
	private final RectTubeRepository rectTubeRepository;

    public RectTubeService(RectTubeRepository rectTubeRepository) {
        this.rectTubeRepository = rectTubeRepository;
    }

    public List<RectTube> getAllRectTubes() {
        return (List<RectTube>) rectTubeRepository.findAll();
    }

    public RectTube addRectTube(RectTube rectTube) {
        return rectTubeRepository.save(rectTube);
    }
    
    public boolean deleteRectTube(int id) {
        Optional<RectTube> rectTube = rectTubeRepository.findById(id);
        if (rectTube.isPresent()) {
            rectTubeRepository.deleteById(id); 
            return true;
        } else {
            return false;
        }
    }
}
