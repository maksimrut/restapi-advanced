package com.epam.esm.service;

import com.epam.esm.service.dto.TagDto;
import org.springframework.stereotype.Service;

/**
 * The interface Statictics repository.
 *
 * @author Maksim Rutkouski
 */
@Service
public interface StatisticsService {

    /**
     * Find most widely used tag of a user with the highest cost of all orders.
     *
     * @return the optional tag
     */
    TagDto findMostPopularTagOfUserWithHighestAllOrdersSum();
}
