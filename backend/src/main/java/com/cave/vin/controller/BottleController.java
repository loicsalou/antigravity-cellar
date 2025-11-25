package com.cave.vin.controller;

import com.cave.vin.domain.Bottle;
import com.cave.vin.domain.WineColor;
import com.cave.vin.service.BottleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bottles")
public class BottleController {

    private final BottleService bottleService;

    public BottleController(BottleService bottleService) {
        this.bottleService = bottleService;
    }

    @GetMapping
    public ResponseEntity<Page<Bottle>> searchBottles(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Integer vintage,
            @RequestParam(required = false) WineColor color,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String appellation,
            Pageable pageable,
            @org.springframework.security.core.annotation.AuthenticationPrincipal org.springframework.security.oauth2.core.user.OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        String email = principal.getAttribute("email");
        return ResponseEntity
                .ok(bottleService.searchBottles(query, vintage, color, region, appellation, pageable, email));
    }

    @GetMapping("/rack/{rackId}")
    public ResponseEntity<List<Bottle>> getBottlesInRack(@PathVariable Long rackId) {
        return ResponseEntity.ok(bottleService.getBottlesInRack(rackId));
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

    @PostMapping("/batch")
    public ResponseEntity<List<Bottle>> addBottleBatch(
            @RequestBody com.cave.vin.dto.AddBottleBatchRequest request,
            @org.springframework.security.core.annotation.AuthenticationPrincipal org.springframework.security.oauth2.core.user.OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        String email = principal.getAttribute("email");
        return ResponseEntity.ok(bottleService.addBottleBatch(request, email));
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
