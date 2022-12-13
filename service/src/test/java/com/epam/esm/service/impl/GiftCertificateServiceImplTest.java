package com.epam.esm.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.GiftCertificateException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.impl.GiftCertificateRepositoryImpl;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.util.CertificateQueryParameters;
import com.epam.esm.util.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GiftCertificateServiceImplTest {

    @Spy
    private ModelMapper modelMapper;

    @Mock
    private GiftCertificateRepositoryImpl certificateRepository;

    @InjectMocks
    private GiftCertificateServiceImpl certificateService;

    private List<GiftCertificate> giftCertificates;
    private GiftCertificate c1;
    private GiftCertificateDto c1Dto;

    @BeforeEach
    void setUp() {
        giftCertificates = new ArrayList<>();

        c1 = new GiftCertificate();
        c1.setId(1L);
        c1.setName("certificate1");
        c1.setDescription("description of c1");
        c1.setPrice(BigDecimal.valueOf(1.5));
        c1.setDuration(30);
        c1.setCreateDate(LocalDateTime.now());
        c1.setLastUpdateDate(LocalDateTime.now());

        GiftCertificate c2 = new GiftCertificate();
        c2.setId(2L);
        c2.setName("certificate2");
        c2.setDescription("description of c2");
        c2.setPrice(BigDecimal.valueOf(10));
        c2.setDuration(5);
        c2.setCreateDate(LocalDateTime.now().minusDays(5L));
        c2.setLastUpdateDate(LocalDateTime.now());

        GiftCertificate c3 = new GiftCertificate();
        c3.setId(3L);
        c3.setName("certificate3");
        c3.setDescription("description of c3");
        c3.setPrice(BigDecimal.valueOf(1.5));
        c3.setDuration(30);
        c3.setCreateDate(LocalDateTime.now());

        c1Dto = new GiftCertificateDto();
        c1Dto.setId(1L);
        c1Dto.setName("certificate1");
        c1Dto.setDescription("description of c1");
        c1Dto.setPrice(BigDecimal.valueOf(1.5));
        c1Dto.setDuration(30);
        c1Dto.setCreateDate(LocalDateTime.now());
        c1Dto.setLastUpdateDate(LocalDateTime.now());

        giftCertificates.add(c1);
        giftCertificates.add(c2);
        giftCertificates.add(c3);
    }

    @Test
    void findByIdPositive() {
        Mockito.when(certificateRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(c1));
        GiftCertificate certificate = certificateService.findById(1L);
        String expected = "certificate1";
        String actual = certificate.getName();
        assertEquals(expected, actual);
    }

    @Test
    void findByIdNonExistentEntityThrowsException() {
        assertThrows(GiftCertificateException.class, () -> certificateService.findById(5L));
    }

    @Test
    void findCertificateDtoByIdPositive() {
        Mockito.when(certificateRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(c1));
        GiftCertificateDto certificateDto = certificateService.findCertificateDtoById(1L);
        String expected = "certificate1";
        String actual = certificateDto.getName();
        assertEquals(expected, actual);
    }

    @Test
    void createPositiveWithoutTags() throws ServiceException {
        Mockito.when(certificateRepository.save(Mockito.any(GiftCertificate.class))).thenReturn(c1);
        GiftCertificateDto actual = certificateService.create(c1Dto);
        actual.setCreateDate(c1Dto.getCreateDate());
        actual.setLastUpdateDate(c1Dto.getLastUpdateDate());
        Mockito.verify(certificateRepository, Mockito.times(1)).save(Mockito.any(GiftCertificate.class));
        assertEquals(c1Dto, actual);
    }

    @Test
    void deleteById() {
        certificateService.deleteById(777L);
        Mockito.verify(certificateRepository, Mockito.times(1)).deleteById(777L);
    }

    @Test
    void updateThrowsException() {
        assertThrows(ServiceException.class,
                () -> certificateService.update(0L,new GiftCertificateDto()));
    }

    @Test
    void updatePositiveWithoutTags() throws ServiceException {
        Mockito.when(certificateRepository.findById(1L)).thenReturn(Optional.of(c1));
        Mockito.when(certificateRepository.update(c1)).thenReturn(c1);
        GiftCertificate actual = modelMapper.map(certificateService.update(1L, c1Dto), GiftCertificate.class);
        assertEquals(c1, actual);
    }

    @Test
    void findAllByParamsPositive() {
        Page page = new Page(1, 7);
        CertificateQueryParameters queryParameters = new CertificateQueryParameters(new String[]{}, "cert", "DESC", "ASC");
        Mockito.when(certificateRepository.findAllByParams(queryParameters, page)).thenReturn(giftCertificates);
        List<GiftCertificateDto> actual = certificateService.findAllByParams(queryParameters, page);
        assertEquals(giftCertificates.size(), actual.size());
    }
}
