package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.util.Page;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The interface User repository.
 *
 * @author Maksim Rutkouski
 */
@Repository
public interface UserRepository {

    /**
     * Find all users.
     *
     * @param page the page
     * @return the list
     */
    List<User> findAll(Page page);

    /**
     * Find user by id optional.
     *
     * @param id the id
     * @return the optional
     */
    Optional<User> findById(Long id);


    /**
     * Find list of all orders of a certain user.
     *
     * @param user the user
     * @param page the page
     * @return the list
     */
    List<Order> findUserOrders(User user, Page page);
}
