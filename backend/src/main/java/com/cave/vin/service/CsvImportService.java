package com.cave.vin.service;

import com.cave.vin.domain.*;
import com.cave.vin.repository.*;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CsvImportService {

    private final BottleRepository bottleRepository;
    private final WineRepository wineRepository;
    private final ProducerRepository producerRepository;
    private final RegionRepository regionRepository;
    private final CountryRepository countryRepository;
    private final CellarRepository cellarRepository;

    public CsvImportService(BottleRepository bottleRepository, WineRepository wineRepository,
            ProducerRepository producerRepository, RegionRepository regionRepository,
            CountryRepository countryRepository, CellarRepository cellarRepository) {
        this.bottleRepository = bottleRepository;
        this.wineRepository = wineRepository;
        this.producerRepository = producerRepository;
        this.regionRepository = regionRepository;
        this.countryRepository = countryRepository;
        this.cellarRepository = cellarRepository;
    }

    @Transactional
    public void importCsv(MultipartFile file, User user, String cellarName) throws IOException, CsvException {
        // Find or Create Cellar
        Cellar cellar = cellarRepository.findByNameAndUser(cellarName, user)
                .orElseGet(() -> {
                    Cellar c = new Cellar();
                    c.setName(cellarName);
                    c.setUser(user);
                    return cellarRepository.save(c);
                });

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            // Configure CSV reader with tab separator as seen in the file
            CSVReader csvReader = new CSVReaderBuilder(reader)
                    .withCSVParser(new CSVParserBuilder().withSeparator('\t').build())
                    .withSkipLines(1) // Skip header
                    .build();

            List<String[]> records = csvReader.readAll();

            for (String[] record : records) {
                processRecord(record, cellar);
            }
        }
    }

    private void processRecord(String[] record, Cellar cellar) {
        // Mapping based on the CSV structure:
        // 0: nomCru (Producer - Wine Name)
        // 1: country_label
        // 2: subregion_label
        // 3: area_label (Appellation)
        // 4: label (Color)
        // 5: millesime
        // 6: volume (e.g. "75 cl")
        // 7: date_achat
        // 8: prix
        // 10: quantite_courante

        if (record.length < 11)
            return; // Skip invalid lines

        String nomCru = record[0];
        String countryName = record[1];
        String regionName = record[2];
        String appellation = record[3]; // area_label -> Appellation
        String colorLabel = record[4];
        String vintageStr = record[5];
        String volumeStr = record[6];
        String dateAchatStr = record[7];
        String priceStr = record[8];
        String quantityStr = record[10];

        int quantity = parseInteger(quantityStr, 0);
        if (quantity <= 0)
            return; // Don't import if quantity is 0

        // 1. Find or Create Country
        Country country = findOrCreateCountry(countryName);

        // 2. Find or Create Region
        Region region = findOrCreateRegion(regionName, country);

        // 3. Parse Producer and Wine Name
        String producerName;
        String wineName;
        if (nomCru.contains(" - ")) {
            String[] parts = nomCru.split(" - ", 2);
            producerName = parts[0].trim();
            wineName = parts[1].trim();
        } else {
            producerName = "Inconnu";
            wineName = nomCru.trim();
        }

        // 4. Find or Create Producer
        Producer producer = findOrCreateProducer(producerName);

        // 5. Find or Create Wine
        Wine wine = findOrCreateWine(wineName, producer, region, colorLabel, vintageStr, appellation);

        // 6. Create Bottles
        createBottles(wine, quantity, volumeStr, dateAchatStr, priceStr, cellar);
    }

    private Country findOrCreateCountry(String name) {
        if (name == null || name.isBlank())
            return null;
        return countryRepository.findByName(name)
                .orElseGet(() -> {
                    Country c = new Country();
                    c.setName(name);
                    return countryRepository.save(c);
                });
    }

    private Region findOrCreateRegion(String name, Country country) {
        if (name == null || name.isBlank())
            return null;
        return regionRepository.findByNameAndCountry(name, country)
                .orElseGet(() -> {
                    Region r = new Region();
                    r.setName(name);
                    r.setCountry(country);
                    return regionRepository.save(r);
                });
    }

    private Producer findOrCreateProducer(String name) {
        if (name == null || name.isBlank())
            return null;
        return producerRepository.findByName(name)
                .orElseGet(() -> {
                    Producer p = new Producer();
                    p.setName(name);
                    return producerRepository.save(p);
                });
    }

    private Wine findOrCreateWine(String name, Producer producer, Region region, String colorLabel, String vintageStr,
            String appellation) {
        Integer vintage = parseInteger(vintageStr, null);
        // Simple check to avoid duplicates: Name + Vintage + Producer
        Optional<Wine> existingWine = wineRepository.findByNameAndProducerAndVintage(name, producer, vintage);

        if (existingWine.isPresent()) {
            return existingWine.get();
        }

        Wine wine = new Wine();
        wine.setName(name);
        wine.setProducer(producer);
        wine.setRegion(region);
        wine.setVintage(vintage);
        wine.setColor(mapColor(colorLabel));
        wine.setAppellation(appellation);

        return wineRepository.save(wine);
    }

    private void createBottles(Wine wine, int quantity, String volumeStr, String dateAchatStr, String priceStr,
            Cellar cellar) {
        Integer volume = parseVolume(volumeStr);
        LocalDate purchaseDate = parseDate(dateAchatStr);
        BigDecimal price = parsePrice(priceStr);

        for (int i = 0; i < quantity; i++) {
            Bottle bottle = new Bottle();
            bottle.setWine(wine);
            bottle.setVolume(volume);
            bottle.setPurchaseDate(purchaseDate);
            bottle.setPrice(price);
            bottle.setCellar(cellar);
            // Rack is null by default
            bottleRepository.save(bottle);
        }
    }

    private WineColor mapColor(String label) {
        if (label == null)
            return WineColor.RED;
        String lower = label.toLowerCase();
        if (lower.contains("blanc")) {
            if (lower.contains("effervescent"))
                return WineColor.SPARKLING;
            return WineColor.WHITE;
        }
        if (lower.contains("rosé") || lower.contains("rose")) {
            if (lower.contains("effervescent"))
                return WineColor.SPARKLING;
            return WineColor.ROSE;
        }
        if (lower.contains("rouge"))
            return WineColor.RED;
        if (lower.contains("jaune"))
            return WineColor.YELLOW;
        if (lower.contains("dessert") || lower.contains("liquoreux"))
            return WineColor.DESSERT;

        return WineColor.RED; // Default
    }

    private Integer parseVolume(String volumeStr) {
        if (volumeStr == null)
            return 750;
        try {
            String v = volumeStr.toLowerCase().replace("cl", "").trim();
            double val = Double.parseDouble(v);
            if (volumeStr.toLowerCase().contains("cl")) {
                return (int) (val * 10); // cl to ml
            }
            return (int) val; // assume ml if not specified? or cl? CSV says "75 cl"
        } catch (NumberFormatException e) {
            return 750; // Default standard bottle
        }
    }

    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank())
            return null;
        try {
            return LocalDate.parse(dateStr); // ISO format yyyy-MM-dd in CSV
        } catch (Exception e) {
            return null;
        }
    }

    private BigDecimal parsePrice(String priceStr) {
        if (priceStr == null || priceStr.isBlank())
            return BigDecimal.ZERO;
        try {
            return new BigDecimal(priceStr.replace(",", "."));
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    private Integer parseInteger(String str, Integer defaultValue) {
        if (str == null)
            return defaultValue;
        try {
            // Handle cases like "-0" or "2008"
            return Integer.parseInt(str.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
