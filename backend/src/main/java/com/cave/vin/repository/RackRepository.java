package com.cave.vin.repository;

import com.cave.vin.domain.Rack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RackRepository extends JpaRepository<Rack, Long> {
    List<Rack> findByCellarId(Long cellarId);
}
