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

    public static Specification<Bottle> hasText(String query) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (!StringUtils.hasText(query)) {
                return criteriaBuilder.conjunction();
            }
            String likePattern = "%" + query.toLowerCase() + "%";

            Predicate wineNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("wine").get("name")),
                    likePattern);
            Predicate appellationPredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("wine").get("appellation")),
                    likePattern);
            Predicate producerNamePredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("wine").get("producer").get("name")),
                    likePattern);

            return criteriaBuilder.or(wineNamePredicate, appellationPredicate, producerNamePredicate);
        };
    }

    public static Specification<Bottle> hasVintage(Integer vintage) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (vintage == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("wine").get("vintage"), vintage);
        };
    }

    public static Specification<Bottle> hasColor(WineColor color) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (color == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("wine").get("color"), color);
        };
    }

    public static Specification<Bottle> hasRegion(String regionName) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (!StringUtils.hasText(regionName)) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(criteriaBuilder.lower(root.get("wine").get("region").get("name")),
                    regionName.toLowerCase());
        };
    }

    public static Specification<Bottle> hasAppellation(String appellation) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (!StringUtils.hasText(appellation)) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(criteriaBuilder.lower(root.get("wine").get("appellation")),
                    appellation.toLowerCase());
        };
    }

    public static Specification<Bottle> hasUserEmail(String email) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (!StringUtils.hasText(email)) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("cellar").get("user").get("email"), email);
        };
    }

    public static Specification<Bottle> search(String query, Integer vintage, WineColor color, String region,
            String appellation, String userEmail) {
        Specification<Bottle> spec = Specification.where(null);
        if (StringUtils.hasText(userEmail)) {
            spec = spec.and(hasUserEmail(userEmail));
        }
        if (StringUtils.hasText(query)) {
            spec = spec.and(hasText(query));
        }
        if (vintage != null) {
            spec = spec.and(hasVintage(vintage));
        }
        if (color != null) {
            spec = spec.and(hasColor(color));
        }
        if (StringUtils.hasText(region)) {
            spec = spec.and(hasRegion(region));
        }
        if (StringUtils.hasText(appellation)) {
            spec = spec.and(hasAppellation(appellation));
        }
        return spec;
    }
}
