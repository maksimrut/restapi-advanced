package com.epam.esm.service.impl;

import com.epam.esm.exception.GiftCertificateException;
import com.epam.esm.repository.StatisticsRepository;
import com.epam.esm.service.StatisticsService;
import com.epam.esm.service.dto.TagDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.epam.esm.exception.ErrorCode.NON_EXISTENT_ENTITY;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final StatisticsRepository statisticsRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public StatisticsServiceImpl(StatisticsRepository statisticsRepository, ModelMapper modelMapper) {
        this.statisticsRepository = statisticsRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public TagDto findMostPopularTagOfUserWithHighestAllOrdersSum() {
        return statisticsRepository.findMostPopularTagOfUserWithHighestAllOrdersSum()
                .map(optionalTag -> modelMapper.map(optionalTag, TagDto.class))
                .orElseThrow(() -> new GiftCertificateException(NON_EXISTENT_ENTITY));
    }
}
