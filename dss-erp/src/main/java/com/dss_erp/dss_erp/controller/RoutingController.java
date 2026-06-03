package com.dss_erp.dss_erp.controller;

import com.dss_erp.dss_erp.models.Product;
import com.dss_erp.dss_erp.models.Routing;
import com.dss_erp.dss_erp.payload.BomRequest;
import com.dss_erp.dss_erp.payload.RoutingDTO;
import com.dss_erp.dss_erp.payload.RoutingResponse;
import com.dss_erp.dss_erp.repositories.ProductRepository;
import com.dss_erp.dss_erp.service.RoutingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/products/{productId}/routing")
@RequiredArgsConstructor
public class RoutingController {

    private final RoutingService routingService;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;

    @PostMapping("/create")
    public ResponseEntity<RoutingDTO> createRouting(@PathVariable Long productId,
                                            @RequestBody BomRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        RoutingDTO created = routingService.createRouting(product, request.getNote());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    @PostMapping("/clone/{routingId}")
    public ResponseEntity<RoutingDTO> cloneRouting(@PathVariable Long routingId,
                                            @RequestParam(required = false) String note) {
        Routing newRouting = routingService.cloneRouting(routingId, note);
        RoutingDTO dto = modelMapper.map(newRouting, RoutingDTO.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PostMapping("/release/{routingId}")
    public ResponseEntity<RoutingDTO> release(@PathVariable Long routingId) {
        RoutingDTO released = routingService.releaseRouting(routingId);
        return ResponseEntity.ok(released);
    }

    @GetMapping("/latest")
    public ResponseEntity<RoutingDTO> getLatestRouting(@PathVariable Long productId) {
        Routing routing = routingService.getLatestRouting(productId);
        return ResponseEntity.ok(modelMapper.map(routing, RoutingDTO.class));
    }

    @GetMapping("/all")
    public ResponseEntity<RoutingResponse> getAll(Pageable pageable) {
        RoutingResponse response = routingService.getAll(pageable);
        return ResponseEntity.ok(response);
    }

}