package com.cave.vin.repository;

import com.cave.vin.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BottleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BottleRepository bottleRepository;

    @Test
    public void searchBottles_shouldReturnOnlyUserBottles_andFilterByAppellation() {
        // Given
        // User 1
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setName("User 1");
        entityManager.persist(user1);

        Cellar cellar1 = new Cellar();
        cellar1.setName("Cellar 1");
        cellar1.setUser(user1);
        entityManager.persist(cellar1);

        // User 2
        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setName("User 2");
        entityManager.persist(user2);

        Cellar cellar2 = new Cellar();
        cellar2.setName("Cellar 2");
        cellar2.setUser(user2);
        entityManager.persist(cellar2);

        // Common Wine Info
        Country country = new Country();
        country.setName("France");
        entityManager.persist(country);

        Region region = new Region();
        region.setName("Bordeaux");
        region.setCountry(country);
        entityManager.persist(region);

        Producer producer = new Producer();
        producer.setName("Chateau Margaux");
        entityManager.persist(producer);

        Wine wine = new Wine();
        wine.setName("Margaux");
        wine.setAppellation("Margaux");
        wine.setRegion(region);
        wine.setProducer(producer);
        wine.setColor(WineColor.RED);
        wine.setVintage(2015);
        entityManager.persist(wine);

        // Add 10 bottles for User 1
        for (int i = 0; i < 10; i++) {
            Bottle bottle = new Bottle();
            bottle.setWine(wine);
            bottle.setCellar(cellar1);
            entityManager.persist(bottle);
        }

        // Add 5 bottles for User 2 (same wine)
        for (int i = 0; i < 5; i++) {
            Bottle bottle = new Bottle();
            bottle.setWine(wine);
            bottle.setCellar(cellar2);
            entityManager.persist(bottle);
        }

        entityManager.flush();

        // When
        Specification<Bottle> spec = BottleSpecification.search(null, null, null, null, "Margaux", "user1@example.com");
        Page<Bottle> result = bottleRepository.findAll(spec, PageRequest.of(0, 20));

        // Then
        assertThat(result.getTotalElements()).isEqualTo(10);
        assertThat(result.getContent()).allMatch(b -> b.getCellar().getUser().getEmail().equals("user1@example.com"));
    }
}
