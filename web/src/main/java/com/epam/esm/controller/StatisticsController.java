package com.epam.esm.controller;

import com.epam.esm.service.StatisticsService;
import com.epam.esm.service.dto.TagDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * The Statistics REST API controller.
 *
 * @author Maksim Rutkouski
 */

@RequestMapping("/statistics")
@RestController
public class StatisticsController {

    private final StatisticsService statisticsService;

    @Autowired
    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/tags/popular/highest-all-orders-cost-user")
    public TagDto findMostPopularTagOfUserWithHighestAllOrdersSum() {
        TagDto foundTag = statisticsService.findMostPopularTagOfUserWithHighestAllOrdersSum();
        addRelationship(foundTag);
        return foundTag;
    }

    private void addRelationship(TagDto tagDto) {
        tagDto.add(linkTo(methodOn(TagController.class).findById(tagDto.getId())).withSelfRel());
    }
}
