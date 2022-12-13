package com.epam.esm.service.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.GiftCertificateException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.impl.TagRepositoryImpl;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.util.Page;
import org.junit.jupiter.api.Assertions;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @InjectMocks
    private TagServiceImpl tagService;

    @Mock
    private TagRepositoryImpl tagRepository;

    @Spy
    private ModelMapper modelMapper;

    @Test
    void findByIdPositiveTest() {
        Tag tagFromRepository = new Tag();
        tagFromRepository.setId(1L);
        tagFromRepository.setName("first");
        Mockito.when(tagRepository.findById(1L)).thenReturn(Optional.of(tagFromRepository));
        TagDto tagFromService = tagService.findTagById(1L);
        assertEquals(tagFromRepository.getName(), tagFromService.getName());
        Mockito.verify(tagRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void findByIdNonExistentEntityThrowsException() {
        assertThrows(GiftCertificateException.class, () -> tagService.findTagById(7L));
    }

    @Test
    void findByIdNullEntityThrowsException() {
        assertThrows(GiftCertificateException.class, () -> tagService.findTagById(null));
    }

    @Test
    void createPositive() throws ServiceException {
        Tag createdTag = new Tag();
        createdTag.setId(1L);
        createdTag.setName("first");
        TagDto tagDto = new TagDto(1L, "first");
        Mockito.when(tagRepository.save(createdTag)).thenReturn(createdTag);
        TagDto tagFromService = tagService.save(tagDto);
        assertEquals(createdTag.getName(), tagFromService.getName());
    }

    @Test
    void createWhenNameExistsThrowsException() {
        TagDto tagDto = new TagDto(1L, "first");
        Tag createdTag = new Tag();
        createdTag.setId(1L);
        createdTag.setName("first");
        Mockito.when(tagRepository.findByName("first")).thenReturn(Optional.of(createdTag));
        assertThrows(ServiceException.class, () -> tagService.save(tagDto));
    }

    @Test
    void delete() {
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("first");
        Mockito.when(tagRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(tag));
        tagService.deleteTag(777L);
        Mockito.verify(tagRepository, Mockito.times(1)).deleteById(Mockito.anyLong());
    }

    @Test
    void findByNameNegative() {
        String name = "tag";
        Mockito.when(tagRepository.findByName(name)).thenReturn(Optional.empty());
        Assertions.assertThrows(ServiceException.class, () -> tagService.findByName(name));
        Mockito.verify(tagRepository, Mockito.times(1)).findByName(name);
    }

    @Test
    void findAll() {
        List<Tag> tags = new ArrayList<>();
        Tag tag1 = new Tag();
        tag1.setName("first");
        tag1.setId(1L);
        Tag tag2 = new Tag();
        tag2.setId(2L);
        tag2.setName("second");
        tags.add(tag1);
        tags.add(tag2);
        Page page = new Page(1, 5);
        Mockito.when(tagRepository.findAll(page)).thenReturn(tags);
        List<TagDto> tagsFromService = tagService.findAllTags(page);
        Assertions.assertEquals(2, tagsFromService.size());
    }
}
