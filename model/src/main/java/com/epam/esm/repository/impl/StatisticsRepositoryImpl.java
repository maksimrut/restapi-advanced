package com.epam.esm.repository.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.StatisticsRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class StatisticsRepositoryImpl implements StatisticsRepository {

    private static final String FIND_MOST_POPULAR_TAG_OF_USER_WITH_HIGHEST_ALL_ORDERS_SUM =
            "SELECT t.id, t.name FROM tags t " +
            "JOIN tags_certificates tc ON t.id=tc.tag_id " +
            "JOIN orders_certificates oc ON tc.gift_certificate_id=oc.gift_certificate_id " +
            "JOIN orders o ON oc.order_id=o.id AND oc.order_id=(" +
                    "SELECT u.id FROM users u " +
                    "JOIN orders ON u.id=orders.user_id " +
                    "GROUP BY u.id " +
                    "ORDER BY sum(orders.orderCost) DESC LIMIT 1) " +
            "GROUP BY t.id " +
            "ORDER BY count(t.id) DESC LIMIT 1";

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Optional<Tag> findMostPopularTagOfUserWithHighestAllOrdersSum() {
        return entityManager.createNativeQuery(FIND_MOST_POPULAR_TAG_OF_USER_WITH_HIGHEST_ALL_ORDERS_SUM, Tag.class)
                .getResultList()
                .stream().findAny();
    }
}
