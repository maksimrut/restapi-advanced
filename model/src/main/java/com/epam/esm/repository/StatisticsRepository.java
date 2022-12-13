package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * The interface Statictics repository.
 *
 * @author Maksim Rutkouski
 */
@Repository
public interface StatisticsRepository {

    /**
     * Find most widely used tag of a user with the highest cost of all orders.
     *
     * @return the optional tag
     */
    Optional<Tag> findMostPopularTagOfUserWithHighestAllOrdersSum();
}
