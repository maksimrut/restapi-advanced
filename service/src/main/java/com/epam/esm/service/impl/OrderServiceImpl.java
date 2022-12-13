package com.epam.esm.service.impl;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.GiftCertificateException;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.util.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.epam.esm.exception.ErrorCode.NON_EXISTENT_ENTITY;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    
    private final OrderRepository orderRepository;
    private final GiftCertificateService certificateService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Override
    public List<OrderDto> findAll(Page page) {
        List<Order> orders = orderRepository.findAll(page);
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto findById(Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        return optionalOrder.map(order -> modelMapper.map(order, OrderDto.class))
                .orElseThrow(() -> new GiftCertificateException(NON_EXISTENT_ENTITY));
    }

    @Override
    @Transactional
    public OrderDto save(OrderDto orderDto) {
        Order order = modelMapper.map(orderDto, Order.class);
        UserDto userDto = userService.findUserById(orderDto.getUser().getId());
        List<GiftCertificateDto> foundCertificates = orderDto.getCertificateListDto()
                .stream()
                .map(giftCertificateDto -> certificateService.findCertificateDtoById(giftCertificateDto.getId()))
                .collect(Collectors.toList());
        orderDto.setUser(modelMapper.map(userDto, User.class));
        orderDto.setCertificateListDto(foundCertificates);
        order.setOrderCost(foundCertificates.stream()
                .map(GiftCertificateDto :: getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        Order savedOrder = orderRepository.save(order);
        return modelMapper.map(savedOrder, OrderDto.class);
    }
}
