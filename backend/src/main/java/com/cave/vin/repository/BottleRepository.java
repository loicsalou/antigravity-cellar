package com.cave.vin.repository;

import com.cave.vin.domain.Bottle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BottleRepository extends JpaRepository<Bottle, Long>, JpaSpecificationExecutor<Bottle> {
    List<Bottle> findTop3ByOrderByPriceDesc();

    List<Bottle> findTop3ByWineVintageGreaterThanOrderByWineVintageAsc(int vintage);

    List<Bottle> findByRackId(Long rackId);

    List<Bottle> findByWineId(Long wineId);

    List<Bottle> findByCellar_User_Email(String email);

    List<Bottle> findTop3ByCellar_User_EmailOrderByPriceDesc(String email);

    List<Bottle> findTop3ByCellar_User_EmailAndWineVintageGreaterThanOrderByWineVintageAsc(String email, int vintage);
}
