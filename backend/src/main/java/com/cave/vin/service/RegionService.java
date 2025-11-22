package com.cave.vin.service;

import com.cave.vin.domain.Region;
import com.cave.vin.repository.RegionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class RegionService {
    private final RegionRepository regionRepository;

    public RegionService(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    public List<Region> getAllRegions() {
        return regionRepository.findAll();
    }

    public List<Region> getRegionsByCountry(Long countryId) {
        return regionRepository.findByCountryId(countryId);
    }

    public Region createRegion(Region region) {
        return regionRepository.save(region);
    }
}

@RestController
@RequestMapping("/api/regions")
@CrossOrigin(origins = "http://localhost:4200")
class RegionController {
    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @GetMapping
    public List<Region> getAllRegions(@RequestParam(required = false) Long countryId) {
        if (countryId != null) {
            return regionService.getRegionsByCountry(countryId);
        }
        return regionService.getAllRegions();
    }

    @PostMapping
    public Region createRegion(@RequestBody Region region) {
        return regionService.createRegion(region);
    }
}
