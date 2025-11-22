package com.cave.vin.repository;

import com.cave.vin.domain.Bottle;
import com.cave.vin.domain.Producer;
import com.cave.vin.domain.Wine;
import com.cave.vin.domain.WineColor;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class BottleSpecification {

    public static Specification<Bottle> search(String query, Integer vintage, WineColor color) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<Bottle, Wine> wineJoin = root.join("wine");
            Join<Wine, Producer> producerJoin = wineJoin.join("producer");

            if (StringUtils.hasText(query)) {
                String likePattern = "%" + query.toLowerCase() + "%";
                Predicate wineNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(wineJoin.get("name")),
                        likePattern);
                Predicate appellationPredicate = criteriaBuilder
                        .like(criteriaBuilder.lower(wineJoin.get("appellation")), likePattern);
                Predicate producerNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(producerJoin.get("name")),
                        likePattern);

                predicates.add(criteriaBuilder.or(wineNamePredicate, appellationPredicate, producerNamePredicate));
            }

            if (vintage != null) {
                predicates.add(criteriaBuilder.equal(wineJoin.get("vintage"), vintage));
            }

            if (color != null) {
                predicates.add(criteriaBuilder.equal(wineJoin.get("color"), color));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
