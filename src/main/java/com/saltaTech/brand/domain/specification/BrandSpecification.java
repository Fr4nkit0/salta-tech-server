package com.saltaTech.brand.domain.specification;

import com.saltaTech.brand.domain.dto.request.BrandSearchCriteria;
import com.saltaTech.brand.domain.persistence.Brand;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

public class BrandSpecification implements Specification<Brand> {
    private final BrandSearchCriteria searchCriteria;

    public BrandSpecification(BrandSearchCriteria searchCriteria){
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<Brand> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        var predicates=new ArrayList<Predicate>();
        if (StringUtils.hasText(searchCriteria.name())){
            final var nameLike = criteriaBuilder.like(
                    root.get("name"),
                    "%".concat(searchCriteria.name()).concat("%"));
            predicates.add(nameLike);
        }
        final var enabledPredicate = criteriaBuilder.isTrue(root.get("enabled"));
        predicates.add(enabledPredicate);
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
