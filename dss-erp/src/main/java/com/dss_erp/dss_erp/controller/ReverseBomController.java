package com.dss_erp.dss_erp.controller;

import com.dss_erp.dss_erp.models.BomComponentType;
import com.dss_erp.dss_erp.payload.ProductDTO;
import com.dss_erp.dss_erp.service.ReverseBomService;
import lombok.RequiredArgsConstructor;
import org.hibernate.type.ComponentType;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reverse-bom")
@RequiredArgsConstructor
public class ReverseBomController {

    private final ReverseBomService reverseBomService;
    private final ModelMapper modelMapper;

    @GetMapping("/where-used")
    public List<ProductDTO> whereUsed(
            @RequestParam Long componentId,
            @RequestParam BomComponentType componentType
    ) {

        return reverseBomService.whereUsed(componentId, componentType)
                .stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
    }
}

