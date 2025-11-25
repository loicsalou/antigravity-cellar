package com.cave.vin.controller;

import com.cave.vin.domain.Cellar;
import com.cave.vin.service.CellarService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cellars")
public class CellarController {

    private final CellarService cellarService;

    public CellarController(CellarService cellarService) {
        this.cellarService = cellarService;
    }

    @GetMapping
    public ResponseEntity<List<Cellar>> getUserCellars(@AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");
        return ResponseEntity.ok(cellarService.getCellarsForUser(email));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cellar> getCellar(@PathVariable Long id) {
        return ResponseEntity.ok(cellarService.getCellarById(id));
    }

    @PostMapping
    public ResponseEntity<Cellar> createCellar(@AuthenticationPrincipal OAuth2User principal,
            @RequestBody Cellar cellar) {
        String email = principal.getAttribute("email");
        return ResponseEntity.ok(cellarService.createCellar(email, cellar));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCellar(@PathVariable Long id) {
        cellarService.deleteCellar(id);
        return ResponseEntity.noContent().build();
    }
}
