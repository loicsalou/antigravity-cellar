package com.cave.vin.service;

import com.cave.vin.domain.Wine;
import com.cave.vin.repository.WineRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WineService {

    private final WineRepository wineRepository;

    public WineService(WineRepository wineRepository) {
        this.wineRepository = wineRepository;
    }

    public List<Wine> getAllWines() {
        var list = wineRepository.findAll();
        System.out.println("Nombre de vins retournés : " + list.size());
        return list;
    }

    public Wine getWineById(Long id) {
        return wineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Wine not found with id: " + id));
    }

    @Transactional
    public Wine createWine(Wine wine) {
        // TODO: Check if wine already exists?
        return wineRepository.save(wine);
    }

    // TODO: Implement advanced search with Specifications
}
