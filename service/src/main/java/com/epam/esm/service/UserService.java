package com.epam.esm.service;

import com.epam.esm.entity.Order;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.util.Page;

import java.util.List;

/**
 * The User service interface.
 * Specifies a set of methods for user managing
 *
 * @author Maksim Rutkouski
 */

public interface UserService {

    /**
     * Find all list.
     *
     * @return the list
     */
    List<UserDto> findAllUsers(Page page);

    /**
     * Find tag by id.
     *
     * @param id the id
     * @return the optional user if it exists, otherwise the empty optional
     */
    UserDto findUserById(Long id);

    /**
     * Find user's order list.
     *
     * @param userId the user id
     * @param page the page
     * @return the list of orders
     */
    List<Order> findUserOrders(Long userId, Page page);
}
