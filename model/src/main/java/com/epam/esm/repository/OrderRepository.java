package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import com.epam.esm.util.Page;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Order repository.
 *
 * @author Maksim Rutkouski
 */
@Repository
public interface OrderRepository extends BaseRepository<Order> {

    /**
     * Update order in database.
     *
     * @param order the order to update
     * @return the updated order
     */
    Order update(Order order);

    /**
     * Find order list by user id.
     *
     * @param id   the id
     * @param page the page containing information about pagination
     * @return the list of found orders
     */
    List<Order> findByUserId(Long id, Page page);
}
