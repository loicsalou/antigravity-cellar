package com.cave.vin.service;

import com.cave.vin.domain.Bottle;
import com.cave.vin.domain.WineColor;
import com.cave.vin.dto.DashboardDTO;
import com.cave.vin.repository.BottleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

        private final BottleRepository bottleRepository;

        public DashboardService(BottleRepository bottleRepository) {
                this.bottleRepository = bottleRepository;
        }

        public DashboardDTO getDashboardStats() {
                List<Bottle> allBottles = bottleRepository.findAll();

                long totalBottles = allBottles.size();

                Map<String, Long> bottlesByRegion = allBottles.stream()
                                .collect(Collectors.groupingBy(b -> b.getWine().getRegion().getName(),
                                                Collectors.counting()));

                Map<String, Map<String, Long>> regionAppellationStats = allBottles.stream()
                                .filter(b -> b.getWine().getAppellation() != null)
                                .collect(Collectors.groupingBy(
                                                b -> b.getWine().getRegion().getName(),
                                                Collectors.groupingBy(b -> b.getWine().getAppellation(),
                                                                Collectors.counting())));

                Map<WineColor, Long> bottlesByColor = allBottles.stream()
                                .collect(Collectors.groupingBy(b -> b.getWine().getColor(), Collectors.counting()));

                List<Bottle> mostExpensive = bottleRepository.findTop3ByOrderByPriceDesc();
                List<Bottle> oldest = bottleRepository.findTop3ByWineVintageGreaterThanOrderByWineVintageAsc(0);

                return new DashboardDTO(totalBottles, bottlesByRegion, regionAppellationStats, bottlesByColor,
                                mostExpensive, oldest);
        }
}
