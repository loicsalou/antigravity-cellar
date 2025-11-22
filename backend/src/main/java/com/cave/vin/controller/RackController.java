package com.cave.vin.controller;

import com.cave.vin.domain.Rack;
import com.cave.vin.service.RackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/racks")
@CrossOrigin(origins = "http://localhost:4200")
public class RackController {

    private final RackService rackService;

    public RackController(RackService rackService) {
        this.rackService = rackService;
    }

    @GetMapping
    public ResponseEntity<List<Rack>> getRacksByCellar(@RequestParam Long cellarId) {
        return ResponseEntity.ok(rackService.getRacksForCellar(cellarId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rack> getRack(@PathVariable Long id) {
        return ResponseEntity.ok(rackService.getRackById(id));
    }

    @PostMapping
    public ResponseEntity<Rack> createRack(@RequestParam Long cellarId, @RequestBody Rack rack) {
        return ResponseEntity.ok(rackService.createRack(cellarId, rack));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRack(@PathVariable Long id) {
        rackService.deleteRack(id);
        return ResponseEntity.noContent().build();
    }
}
