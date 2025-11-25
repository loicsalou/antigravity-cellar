package com.cave.vin.service;

import com.cave.vin.domain.Bottle;
import com.cave.vin.domain.Rack;
import com.cave.vin.domain.Wine;
import com.cave.vin.domain.WineColor;
import com.cave.vin.repository.BottleRepository;
import com.cave.vin.repository.BottleSpecification;
import com.cave.vin.repository.RackRepository;
import com.cave.vin.repository.WineRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BottleService {

    private final BottleRepository bottleRepository;
    private final RackRepository rackRepository;
    private final WineRepository wineRepository;
    private final com.cave.vin.repository.CellarRepository cellarRepository;

    public BottleService(BottleRepository bottleRepository, RackRepository rackRepository,
            WineRepository wineRepository, com.cave.vin.repository.CellarRepository cellarRepository) {
        this.bottleRepository = bottleRepository;
        this.rackRepository = rackRepository;
        this.wineRepository = wineRepository;
        this.cellarRepository = cellarRepository;
    }

    public List<Bottle> getBottlesInRack(Long rackId) {
        return bottleRepository.findByRackId(rackId);
    }

    public List<Bottle> getAllBottles() {
        return bottleRepository.findAll();
    }

    public Page<Bottle> searchBottles(String query, Integer vintage, WineColor color, String region, String appellation,
            Pageable pageable, String userEmail) {
        Specification<Bottle> spec = BottleSpecification.search(query, vintage, color, region, appellation, userEmail);
        return bottleRepository.findAll(spec, pageable);
    }

    public Bottle getBottleById(Long id) {
        return bottleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bottle not found with id: " + id));
    }

    @Transactional
    public Bottle addBottle(Bottle bottle, Long rackId, Long wineId) {
        Rack rack = rackRepository.findById(rackId)
                .orElseThrow(() -> new EntityNotFoundException("Rack not found with id: " + rackId));
        Wine wine = wineRepository.findById(wineId)
                .orElseThrow(() -> new EntityNotFoundException("Wine not found with id: " + wineId));

        bottle.setRack(rack);
        bottle.setWine(wine);
        return bottleRepository.save(bottle);
    }

    @Transactional
    public List<Bottle> addBottleBatch(com.cave.vin.dto.AddBottleBatchRequest request, String userEmail) {
        // Validate quantity
        if (request.getQuantity() == null || request.getQuantity() < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }

        // Find wine
        Wine wine = wineRepository.findById(request.getWineId())
                .orElseThrow(() -> new EntityNotFoundException("Wine not found with id: " + request.getWineId()));

        // Find cellar and verify it belongs to the user
        com.cave.vin.domain.Cellar cellar = cellarRepository.findById(request.getCellarId())
                .orElseThrow(() -> new EntityNotFoundException("Cellar not found with id: " + request.getCellarId()));

        if (!cellar.getUser().getEmail().equals(userEmail)) {
            throw new IllegalArgumentException("Cellar does not belong to the current user");
        }

        // Find rack if provided
        Rack rack = null;
        if (request.getRackId() != null) {
            rack = rackRepository.findById(request.getRackId())
                    .orElseThrow(() -> new EntityNotFoundException("Rack not found with id: " + request.getRackId()));
        }

        // Create bottles
        List<Bottle> bottles = new ArrayList<>();
        for (int i = 0; i < request.getQuantity(); i++) {
            Bottle bottle = new Bottle();
            bottle.setWine(wine);
            bottle.setCellar(cellar);
            bottle.setRack(rack);
            bottle.setPrice(request.getPrice());
            bottle.setPurchaseDate(request.getPurchaseDate());
            bottle.setVolume(request.getVolume());
            bottles.add(bottleRepository.save(bottle));
        }

        return bottles;
    }

    @Transactional
    public Bottle moveBottle(Long bottleId, Long newRackId, Double x, Double y) {
        Bottle bottle = getBottleById(bottleId);
        if (newRackId != null) {
            Rack newRack = rackRepository.findById(newRackId)
                    .orElseThrow(() -> new EntityNotFoundException("Rack not found with id: " + newRackId));
            bottle.setRack(newRack);
        }
        if (x != null)
            bottle.setPositionX(x);
        if (y != null)
            bottle.setPositionY(y);

        return bottleRepository.save(bottle);
    }

    @Transactional
    public void deleteBottle(Long id) {
        bottleRepository.deleteById(id);
    }
}
