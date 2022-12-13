package com.epam.esm.controller;

import com.epam.esm.entity.Order;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * The User REST API controller.
 *
 * @author Maksim Rutkouski
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Get all users.
     *
     * @param pageNumber the page number for pagination
     * @param limit the one page records limit
     * @return all available users
     */
    @GetMapping
    public List<UserDto> findAll(@RequestParam(required = false, defaultValue = "1") int pageNumber,
                                 @RequestParam(required = false, defaultValue = "10") int limit) {
        Page page = new Page(pageNumber, limit);
        List<UserDto> usersDto = userService.findAllUsers(page);
        usersDto.forEach(this::addRelationship);
        return usersDto;
    }

    /**
     * Get a user by id.
     *
     * @param id the user id
     * @return found user, otherwise error response with 40401 status code
     */
    @GetMapping("/{id}")
    public UserDto findById(@PathVariable("id") Long id) {
        UserDto userDto = userService.findUserById(id);
        addRelationship(userDto);
        return userDto;
    }

    /**
     * Find user orders list.
     *
     * @param id         the user id
     * @param pageNumber the page number
     * @param limit      the limit records on the page
     * @return the list
     */
    @GetMapping("/{id}/orders")
    public List<Order> findUserOrders(@PathVariable("id") Long id,
                                      @RequestParam(required = false, defaultValue = "1") int pageNumber,
                                      @RequestParam(required = false, defaultValue = "10") int limit) {
        Page page = new Page(pageNumber, limit);
        return userService.findUserOrders(id, page);
    }

    private void addRelationship(UserDto userDto) {
        userDto.add(linkTo(methodOn(UserController.class).findById(userDto.getId())).withSelfRel());
    }
}
