package com.epam.esm.repository.impl;

import com.epam.esm.config.TestConfig;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.util.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {TestConfig.class})
@ActiveProfiles("test")
@Transactional
class GiftCertificateRepositoryImplTest {

    @Autowired
    private GiftCertificateRepositoryImpl certificateRepository;

    @Test
    void findAll() {
        Page page = new Page(1, 10);
        List<GiftCertificate> certificates = certificateRepository.findAll(page);
        int expected = 3;
        int actual = certificates.size();
        assertEquals(expected, actual);
    }

    @Test
    void findById() {
        Optional<GiftCertificate> certificate = certificateRepository.findById(1L);
        assertNotNull(certificate.get());
    }

    @Test
    void create() {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("createdCertificate");
        certificate.setDescription("certificate description");
        certificate.setPrice(BigDecimal.valueOf(0.5));
        certificate.setDuration(15);
        certificate.setCreateDate(LocalDateTime.now());
        certificate.setLastUpdateDate(LocalDateTime.now().minusDays(2L));
        GiftCertificate certificateBD = certificateRepository.save(certificate);
        assertNotNull(certificateBD.getId());
    }

    @Test
    void deleteById() {
        certificateRepository.deleteById(3L);
        Optional<GiftCertificate> certificate = certificateRepository.findById(3L);
        Boolean expected = Boolean.TRUE;
        Boolean actual = certificate.isEmpty();
        assertEquals(expected, actual);
    }

    @Test
    void update() {
        Optional<GiftCertificate> certificate = certificateRepository.findById(1L);
        certificate.get().setDuration(100);
        certificateRepository.update(certificate.get());
        int expected = 100;
        int actual = certificateRepository.findById(1L).get().getDuration();
        assertEquals(expected, actual);
    }

    @Test
    void addTag() {
        certificateRepository.addTag(2L, 1L);
        int expected = 2;
        int actual = certificateRepository.findById(2L).get().getTags().size();
        assertEquals(expected, actual);
    }

    @Test
    void clearTags() {
        certificateRepository.clearTags(3L);
        int expected = 0;
        int actual = certificateRepository.findById(3L).get().getTags().size();
        assertEquals(expected, actual);
    }
}
