package com.epam.esm.repository.impl;

import com.epam.esm.config.TestConfig;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.util.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {TestConfig.class})
@ActiveProfiles("test")
@Transactional
class OrderRepositoryImplTest {

    private User user;

    @Autowired
    private OrderRepositoryImpl orderRepository;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(2L);
        user.setName("user2");
    }

    @Test
    void findAll() {
        Page page = new Page(1, 10);
        List<Order> orders = orderRepository.findAll(page);
        int expected = 3;
        int actual = orders.size();
        assertEquals(expected, actual);
    }

    @Test
    void findById() {
        Optional<Order> optionalOrder = orderRepository.findById(1L);
        assertNotNull(optionalOrder.get());
    }

    @Test
    void save() {
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setOrderCost(BigDecimal.valueOf(200));
        order.setUser(user);
        Order createdOrder = orderRepository.save(order);
        assertNotNull(createdOrder.getId());
    }

    @Test
    void deleteById() {
        assertThrows(UnsupportedOperationException.class, () -> orderRepository.deleteById(3L));
    }

    @Test
    void update() {
        Optional<Order> order = orderRepository.findById(1L);
        order.get().setOrderCost(BigDecimal.valueOf(1000));
        orderRepository.update(order.get());
        BigDecimal expected = BigDecimal.valueOf(1000);
        assertEquals(expected, order.get().getOrderCost());
    }

    @Test
    void findByUserId() {
        Page page = new Page(1, 10);
        List<Order> orders = orderRepository.findByUserId(2L, page);
        int expected = 2;
        int actual = orders.size();
        assertEquals(expected, actual);
    }
}
