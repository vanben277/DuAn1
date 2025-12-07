package com.example.baitap1be.repository.specification;

import com.example.baitap1be.entity.Store;
import com.example.baitap1be.entity.User;
import com.example.baitap1be.enums.Role;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<User> hasStoreCode(String storeCode) {
        return (root, query, criteriaBuilder) -> {
            if (storeCode == null || storeCode.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<User, Store> storeUserJoin = root.join("store", JoinType.LEFT);
            return criteriaBuilder.equal(storeUserJoin.get("storeCode"), storeCode);
        };
    }

    public static Specification<User> fullNameContains(String fullName) {
        return (root, query, criteriaBuilder) -> {
            if (fullName == null || fullName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), "%" + fullName.toLowerCase() + "%");
        };
    }

    public static Specification<User> hasRole(Role role) {
        return (root, query, criteriaBuilder) -> {
            if (role == null) {
                return criteriaBuilder.or(
                        criteriaBuilder.equal(root.get("role"), Role.STAFF),
                        criteriaBuilder.equal(root.get("role"), Role.MANAGER)
                );
            }
            if (role == Role.ADMIN) {
                return criteriaBuilder.disjunction();
            }

            return criteriaBuilder.equal(root.get("role"), role);
        };
    }


    public static Specification<User> isActive() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isActive"), Boolean.TRUE);
    }
}
