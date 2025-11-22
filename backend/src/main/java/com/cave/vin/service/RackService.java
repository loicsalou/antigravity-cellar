package com.cave.vin.service;

import com.cave.vin.domain.Cellar;
import com.cave.vin.domain.Rack;
import com.cave.vin.repository.CellarRepository;
import com.cave.vin.repository.RackRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RackService {

    private final RackRepository rackRepository;
    private final CellarRepository cellarRepository;

    public RackService(RackRepository rackRepository, CellarRepository cellarRepository) {
        this.rackRepository = rackRepository;
        this.cellarRepository = cellarRepository;
    }

    public List<Rack> getRacksForCellar(Long cellarId) {
        return rackRepository.findByCellarId(cellarId);
    }

    public Rack getRackById(Long id) {
        return rackRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rack not found with id: " + id));
    }

    @Transactional
    public Rack createRack(Long cellarId, Rack rack) {
        Cellar cellar = cellarRepository.findById(cellarId)
                .orElseThrow(() -> new EntityNotFoundException("Cellar not found with id: " + cellarId));
        rack.setCellar(cellar);
        return rackRepository.save(rack);
    }

    @Transactional
    public void deleteRack(Long id) {
        rackRepository.deleteById(id);
    }
}
