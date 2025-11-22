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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BottleService {

    private final BottleRepository bottleRepository;
    private final RackRepository rackRepository;
    private final WineRepository wineRepository;

    public BottleService(BottleRepository bottleRepository, RackRepository rackRepository,
            WineRepository wineRepository) {
        this.bottleRepository = bottleRepository;
        this.rackRepository = rackRepository;
        this.wineRepository = wineRepository;
    }

    public List<Bottle> getBottlesInRack(Long rackId) {
        return bottleRepository.findByRackId(rackId);
    }

    public List<Bottle> getAllBottles() {
        return bottleRepository.findAll();
    }

    public List<Bottle> searchBottles(String query, Integer vintage, WineColor color) {
        return bottleRepository.findAll(BottleSpecification.search(query, vintage, color));
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
