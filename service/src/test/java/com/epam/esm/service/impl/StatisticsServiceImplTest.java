package com.epam.esm.service.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.GiftCertificateException;
import com.epam.esm.repository.StatisticsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceImplTest {

    @InjectMocks
    private StatisticsServiceImpl statisticsService;

    @Mock
    private StatisticsRepository statisticsRepository;

    @Test
    void findMostPopularTagOfUserWithHighestAllOrdersSum() {
        Tag tag = new Tag();
        tag.setName("tag");
        tag.setId(1L);
        Mockito.when(statisticsRepository.findMostPopularTagOfUserWithHighestAllOrdersSum())
                .thenReturn(Optional.of(tag));
        Tag foundTag = statisticsRepository.findMostPopularTagOfUserWithHighestAllOrdersSum().get();
        assertEquals("tag", foundTag.getName());
    }

    @Test
    void findMostPopularTagOfUserWithHighestAllOrdersSumThrowsException() {
        Mockito.when(statisticsRepository.findMostPopularTagOfUserWithHighestAllOrdersSum())
                .thenReturn(Optional.empty());
        GiftCertificateException thrown = assertThrows(GiftCertificateException.class, () -> statisticsService.findMostPopularTagOfUserWithHighestAllOrdersSum());
        assertEquals(40410, thrown.getErrorCode());
    }
}