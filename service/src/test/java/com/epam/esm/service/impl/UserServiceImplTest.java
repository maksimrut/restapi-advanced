package com.epam.esm.service.impl;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.GiftCertificateException;
import com.epam.esm.repository.UserRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    @Spy
    private ModelMapper modelMapper;

    private List<User> userList;
    private User user1;

    @BeforeEach
    void setUp() {
        user1 = new User(1L, "userName1");
        User user2 = new User(2L, "userName2");
        userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
    }

    @Test
    void findAllUsers() {
        Page page = new Page(1, 7);
        Mockito.when(userRepository.findAll(page)).thenReturn(userList);
        List<UserDto> userDtoList = userService.findAllUsers(page);
        assertEquals(2, userDtoList.size());
    }

    @Test
    void findUserById() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        UserDto foundUser = userService.findUserById(1L);
        assertEquals("userName1", foundUser.getName());
    }

    @Test
    void findUserByIdThrowsException() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        assertThrows(GiftCertificateException.class, () -> userService.findUserById(1L));
    }

    @Test
    void findUserOrders() {
        Page page = new Page(1, 7);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(userRepository.findUserOrders(Mockito.any(User.class), Mockito.any(Page.class))).thenReturn(new ArrayList<>());
        List<Order> orderList = userService.findUserOrders(1L, page);
        assertEquals(0, orderList.size());
        Mockito.verify(userRepository, Mockito.times(1))
                .findUserOrders(Mockito.any(User.class), Mockito.any(Page.class));
    }
}