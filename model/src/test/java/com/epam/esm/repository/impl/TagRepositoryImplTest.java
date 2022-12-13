package com.epam.esm.repository.impl;

import com.epam.esm.config.TestConfig;
import com.epam.esm.entity.Tag;
import com.epam.esm.util.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {TestConfig.class})
@ActiveProfiles("test")
@Transactional
class TagRepositoryImplTest {

    private Tag tag;

    @Autowired
    TagRepositoryImpl tagRepository;

    @BeforeEach
    void setUp() {
        tag = new Tag();
        tag.setName("CreatedTag");
    }

    @Test
    void findAll() {
        Page page = new Page(1, 10);
        List<Tag> tags = tagRepository.findAll(page);
        assertEquals(4, tags.size());
    }

    @Test
    void findById() {
        Optional<Tag> optionalTag = tagRepository.findById(1L);
        assertNotNull(optionalTag.get());
    }

    @Test
    void findByNameReturnsEmptyWithNonExistentTag() {
        Optional<Tag> actual = tagRepository.findByName("NonExistent");
        assertTrue(actual.isEmpty());
    }

    @Test
    void save() {
        Tag actual = tagRepository.save(tag);
        assertEquals("CreatedTag", actual.getName());
    }

    @Test
    void deleteById() {
        tagRepository.deleteById(2L);
        assertTrue(tagRepository.findById(2L).isEmpty());
    }

    @Test
    void findByName() {
        Optional<Tag> actual = tagRepository.findByName("second");
        assertTrue(actual.isPresent());
    }

    @Test
    void findAllByGiftCertificateId() {
        List<Tag> tags = tagRepository.findAllByGiftCertificateId(1L);
        assertEquals(2, tags.size());
    }
}