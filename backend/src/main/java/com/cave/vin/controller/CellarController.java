package com.cave.vin.controller;

import com.cave.vin.domain.Cellar;
import com.cave.vin.service.CellarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cellars")
@CrossOrigin(origins = "http://localhost:4200")
public class CellarController {

    private final CellarService cellarService;

    public CellarController(CellarService cellarService) {
        this.cellarService = cellarService;
    }

    // TODO: Remove userId param once Auth is implemented
    @GetMapping
    public ResponseEntity<List<Cellar>> getUserCellars(@RequestParam Long userId) {
        return ResponseEntity.ok(cellarService.getCellarsForUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cellar> getCellar(@PathVariable Long id) {
        return ResponseEntity.ok(cellarService.getCellarById(id));
    }

    @PostMapping
    public ResponseEntity<Cellar> createCellar(@RequestParam Long userId, @RequestBody Cellar cellar) {
        return ResponseEntity.ok(cellarService.createCellar(userId, cellar));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCellar(@PathVariable Long id) {
        cellarService.deleteCellar(id);
        return ResponseEntity.noContent().build();
    }
}
