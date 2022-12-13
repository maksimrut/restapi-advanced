package com.epam.esm.service.impl;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.GiftCertificateException;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.util.Page;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.epam.esm.exception.ErrorCode.NON_EXISTENT_ENTITY;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<UserDto> findAllUsers(Page page) {
        List<User> users = userRepository.findAll(page);
        return users.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.map(user -> modelMapper.map(user, UserDto.class))
                .orElseThrow(() -> new GiftCertificateException(NON_EXISTENT_ENTITY));
    }

    @Transactional
    @Override
    public List<Order> findUserOrders(Long userId, Page page) {
        UserDto userDto = findUserById(userId);
        return userRepository.findUserOrders(modelMapper.map(userDto, User.class), page);
    }
}
