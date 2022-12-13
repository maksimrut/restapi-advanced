package com.epam.esm.repository.impl;

import com.epam.esm.entity.Order;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.util.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private static final String FIND_ALL = "SELECT o FROM Order o";
    private static final String FIND_ALL_BY_USER_ID =
            "SELECT * FROM orders WHERE orders.user_id = :userId";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Order> findAll(Page page) {
        return entityManager.createQuery(FIND_ALL, Order.class)
                .setFirstResult((page.getNumber() - 1) * page.getLimit())
                .setMaxResults(page.getLimit())
                .getResultList();
    }

    @Override
    public Optional<Order> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Order.class, id));
    }

    @Override
    public Order save(Order order) {
        entityManager.persist(order);
        return order;
    }

    @Override
    public void deleteById(Long id) {
        throw new UnsupportedOperationException("Order removing is unavailable for OrderRepositoryImpl class");
    }

    @Override
    public Order update(Order order) {
        return entityManager.merge(order);
    }

    @Override
    public List<Order> findByUserId(Long id, Page page) {
        return entityManager.createNativeQuery(FIND_ALL_BY_USER_ID)
                .setParameter("userId", id)
                .setFirstResult((page.getNumber() - 1) * page.getLimit())
                .setMaxResults(page.getLimit())
                .getResultList();
    }
}
