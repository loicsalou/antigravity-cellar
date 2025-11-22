package com.cave.vin.repository;

import com.cave.vin.domain.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    java.util.Optional<Country> findByName(String name);
}
