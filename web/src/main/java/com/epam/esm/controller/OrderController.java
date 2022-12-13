package com.epam.esm.controller;

import com.epam.esm.service.OrderService;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * The Order REST API controller.
 *
 * @author Maksim Rutkouski
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Get all orders.
     *
     * @param pageNumber the page number for pagination
     * @param limit the one page records limit
     * @return all available orders
     */
    @GetMapping
    public List<OrderDto> findAllOrders(@RequestParam(required = false, defaultValue = "1") int pageNumber,
                                        @RequestParam(required = false, defaultValue = "10") int limit) {
        Page page = new Page(pageNumber, limit);
        List<OrderDto> orderDtoList = orderService.findAll(page);
        orderDtoList.forEach(this::addRelationship);
        return orderDtoList;
    }

    /**
     * Get an order by id.
     *
     * @param id the order id
     * @return found user, otherwise error response with 40401 status code
     */
    @GetMapping("/{id}")
    public OrderDto findOrderById(@PathVariable("id") Long id) {
        OrderDto foundOrder = orderService.findById(id);
        addRelationship(foundOrder);
        return foundOrder;
    }

    /**
     * Create an order.
     *
     * @param orderDto the order json object
     * @return created order
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto save(@RequestBody @Valid OrderDto orderDto) {
       OrderDto savedOrder = orderService.save(orderDto);
       addRelationship(savedOrder);
       return savedOrder;
    }

    private void addRelationship(OrderDto orderDto) {
        orderDto.add(linkTo(methodOn(OrderController.class).findOrderById(orderDto.getId())).withSelfRel());
        orderDto.add(linkTo(methodOn(UserController.class).findById(orderDto.getUser().getId())).withRel("user"));
        orderDto.getCertificateListDto().forEach(this::addRelationshipCertificate);
    }

    private void addRelationshipCertificate(GiftCertificateDto giftCertificateDto) {
        giftCertificateDto.add(linkTo(methodOn(GiftCertificateController.class)
                                .findById(giftCertificateDto.getId())).withRel("certificate"));
    }
}
