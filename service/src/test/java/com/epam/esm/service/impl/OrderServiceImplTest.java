package com.epam.esm.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.GiftCertificateException;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.util.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserService userService;
    @Mock
    private GiftCertificateService certificateService;

    @Spy
    private ModelMapper modelMapper;

    private OrderDto orderDto;
    private Order order;
    private List<Order> orderList;
    private User user;
    private UserDto userDto;
    private GiftCertificate certificate;
    private GiftCertificateDto certificateDto;

    @BeforeEach
    void setUp() {
        orderList = new ArrayList<>();
        user = new User(5L, "userName");
        userDto = new UserDto(5L, "userName");
        List<GiftCertificate> giftCertificateList = new ArrayList<>();
        certificate = new GiftCertificate();
        certificate.setId(2L);
        certificate.setCreateDate(LocalDateTime.now());
        certificate.setLastUpdateDate(LocalDateTime.now());
        certificate.setDescription("qwe asd");
        certificate.setDuration(23);
        certificate.setPrice(BigDecimal.valueOf(2L));
        certificate.setName("certName");
        giftCertificateList.add(certificate);
        order = new Order(1L, BigDecimal.ONE, LocalDateTime.now(), user, giftCertificateList);
        orderList.add(order);
        certificateDto = new GiftCertificateDto();
        certificateDto.setId(2L);
        certificateDto.setCreateDate(LocalDateTime.now());
        certificateDto.setLastUpdateDate(LocalDateTime.now());
        certificateDto.setDescription("qwe asd");
        certificateDto.setDuration(23);
        certificateDto.setPrice(BigDecimal.valueOf(2L));
        certificateDto.setName("certName");
        List<GiftCertificateDto> giftCertificateDtoList = new ArrayList<>();
        giftCertificateDtoList.add(certificateDto);
        orderDto = new OrderDto(1L, BigDecimal.ONE, LocalDateTime.now(), user, giftCertificateDtoList);
    }

    @Test
    void findAllPositive() {
        Page page = new Page(1, 7);
        Mockito.when(orderRepository.findAll(page)).thenReturn(orderList);
        List<OrderDto> actual = orderService.findAll(page);
        assertEquals(1, actual.size());
    }

    @Test
    void findByIdPositive() {
        Mockito.when(orderRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(order));
        OrderDto foundOrder = orderService.findById(1L);
        assertEquals(BigDecimal.ONE, foundOrder.getOrderCost());
    }

    @Test
    void findByIdThrowsException() {
        Mockito.when(orderRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        assertThrows(GiftCertificateException.class, () -> orderService.findById(31L));
    }

    @Test
    void save() {
        Mockito.when(userService.findUserById(Mockito.anyLong())).thenReturn(userDto);
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(order);
        Mockito.when(certificateService.findCertificateDtoById(Mockito.anyLong())).thenReturn(certificateDto);
        OrderDto createdOrderDto = orderService.save(orderDto);
        assertEquals(user, createdOrderDto.getUser());
    }
}