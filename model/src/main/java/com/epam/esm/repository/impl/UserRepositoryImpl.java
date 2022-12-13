package com.epam.esm.repository.impl;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.util.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private static final String USER = "user";
    private static final String FIND_ALL = "SELECT u FROM User u";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> findAll(Page page) {
        return entityManager.createQuery(FIND_ALL, User.class)
                .setFirstResult((page.getNumber() - 1) * page.getLimit())
                .setMaxResults(page.getLimit())
                .getResultList();
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public List<Order> findUserOrders(User user, Page page) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> query = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);
        query.select(root);
        query.where(criteriaBuilder.equal(root.get(USER), user));

        return entityManager.createQuery(query)
                .setFirstResult((page.getNumber() - 1) * page.getLimit())
                .setMaxResults(page.getLimit())
                .getResultList();
    }
}
