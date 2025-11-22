package com.cave.vin.repository;

import com.cave.vin.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    List<Region> findByCountryId(Long countryId);

    java.util.Optional<Region> findByNameAndCountry(String name, com.cave.vin.domain.Country country);
}
