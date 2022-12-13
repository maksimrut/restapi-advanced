package com.epam.esm.service;

import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.util.Page;

import java.util.List;

/**
 * The interface Order service.
 *
 * @author Maksim Rutkouski
 */
public interface OrderService {

    /**
     * Find all orders
     *
     * @param page the chosen page
     * @return the list of orders
     */
    List<OrderDto> findAll(Page page);

    /**
     * Find by id optional.
     *
     * @param id the id
     * @return the optional
     */
    OrderDto findById(Long id);

    /**
     * Save or create an order.
     *
     * @param orderDto the order to save
     * @return the created or saved order
     */
    OrderDto save(OrderDto orderDto);
}
