package vn.project.Service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import vn.project.Entity.Brands;
import vn.project.Entity.Products;

public class ProductSpecification {

    public static Specification<Products> withBrandIds(List<Integer> brandIds) {
        if (brandIds == null || brandIds.isEmpty()) {
            return null;
        }
        return (root, query, criteriaBuilder) -> root.get("brandid").in(brandIds);
    }

    public static Specification<Products> withPriceRange(Long minPrice, Long maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice != null && maxPrice != null) {
                return criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
            }
            if (minPrice != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
            }
            if (maxPrice != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
            }
            return null;
        };
    }
}
