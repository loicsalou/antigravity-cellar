package com.cave.vin.controller;

import com.cave.vin.domain.Wine;
import com.cave.vin.service.WineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wines")
public class WineController {

    private final WineService wineService;

    public WineController(WineService wineService) {
        this.wineService = wineService;
    }

    @GetMapping
    public ResponseEntity<List<Wine>> getAllWines() {
        return ResponseEntity.ok(wineService.getAllWines());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Wine> getWine(@PathVariable Long id) {
        return ResponseEntity.ok(wineService.getWineById(id));
    }

    @PostMapping
    public ResponseEntity<Wine> createWine(@RequestBody Wine wine) {
        return ResponseEntity.ok(wineService.createWine(wine));
    }
}
