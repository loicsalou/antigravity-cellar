package com.cave.vin.controller;

import com.cave.vin.domain.Bottle;
import com.cave.vin.domain.WineColor;
import com.cave.vin.service.BottleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bottles")
@CrossOrigin(origins = "http://localhost:4200")
public class BottleController {

    private final BottleService bottleService;

    public BottleController(BottleService bottleService) {
        this.bottleService = bottleService;
    }

    @GetMapping
    public ResponseEntity<List<Bottle>> getBottles(
            @RequestParam(required = false) Long rackId,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Integer vintage,
            @RequestParam(required = false) WineColor color) {

        if (rackId != null) {
            return ResponseEntity.ok(bottleService.getBottlesInRack(rackId));
        }

        if (query != null || vintage != null || color != null) {
            return ResponseEntity.ok(bottleService.searchBottles(query, vintage, color));
        }

        return ResponseEntity.ok(bottleService.getAllBottles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bottle> getBottle(@PathVariable Long id) {
        return ResponseEntity.ok(bottleService.getBottleById(id));
    }

    @PostMapping
    public ResponseEntity<Bottle> addBottle(
            @RequestBody Bottle bottle,
            @RequestParam Long rackId,
            @RequestParam Long wineId) {
        return ResponseEntity.ok(bottleService.addBottle(bottle, rackId, wineId));
    }

    @PutMapping("/{id}/move")
    public ResponseEntity<Bottle> moveBottle(
            @PathVariable Long id,
            @RequestParam(required = false) Long newRackId,
            @RequestParam(required = false) Double x,
            @RequestParam(required = false) Double y) {
        return ResponseEntity.ok(bottleService.moveBottle(id, newRackId, x, y));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBottle(@PathVariable Long id) {
        bottleService.deleteBottle(id);
        return ResponseEntity.noContent().build();
    }
}
