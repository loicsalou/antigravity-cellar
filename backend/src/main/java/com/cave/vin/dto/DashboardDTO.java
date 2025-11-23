package com.cave.vin.dto;

import com.cave.vin.domain.Bottle;
import com.cave.vin.domain.WineColor;

import java.util.List;
import java.util.Map;

public class DashboardDTO {
    private long totalBottles;
    private Map<String, Long> bottlesByRegion;
    private Map<String, Map<String, Long>> regionAppellationStats;
    private Map<WineColor, Long> bottlesByColor;
    private List<Bottle> mostExpensiveBottles;
    private List<Bottle> oldestBottles;

    public DashboardDTO(long totalBottles, Map<String, Long> bottlesByRegion,
            Map<String, Map<String, Long>> regionAppellationStats,
            Map<WineColor, Long> bottlesByColor, List<Bottle> mostExpensiveBottles, List<Bottle> oldestBottles) {
        this.totalBottles = totalBottles;
        this.bottlesByRegion = bottlesByRegion;
        this.regionAppellationStats = regionAppellationStats;
        this.bottlesByColor = bottlesByColor;
        this.mostExpensiveBottles = mostExpensiveBottles;
        this.oldestBottles = oldestBottles;
    }

    public long getTotalBottles() {
        return totalBottles;
    }

    public void setTotalBottles(long totalBottles) {
        this.totalBottles = totalBottles;
    }

    public Map<String, Long> getBottlesByRegion() {
        return bottlesByRegion;
    }

    public void setBottlesByRegion(Map<String, Long> bottlesByRegion) {
        this.bottlesByRegion = bottlesByRegion;
    }

    public Map<String, Map<String, Long>> getRegionAppellationStats() {
        return regionAppellationStats;
    }

    public void setRegionAppellationStats(Map<String, Map<String, Long>> regionAppellationStats) {
        this.regionAppellationStats = regionAppellationStats;
    }

    public Map<WineColor, Long> getBottlesByColor() {
        return bottlesByColor;
    }

    public void setBottlesByColor(Map<WineColor, Long> bottlesByColor) {
        this.bottlesByColor = bottlesByColor;
    }

    public List<Bottle> getMostExpensiveBottles() {
        return mostExpensiveBottles;
    }

    public void setMostExpensiveBottles(List<Bottle> mostExpensiveBottles) {
        this.mostExpensiveBottles = mostExpensiveBottles;
    }

    public List<Bottle> getOldestBottles() {
        return oldestBottles;
    }

    public void setOldestBottles(List<Bottle> oldestBottles) {
        this.oldestBottles = oldestBottles;
    }
}
