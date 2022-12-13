package com.epam.esm.service.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.GiftCertificateException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDto;
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
public class TagServiceImpl implements TagService {

    private final ModelMapper modelMapper;
    private final TagRepository tagRepository;

    @Override
    public List<TagDto> findAllTags(Page page) {
        List<Tag> tags = tagRepository.findAll(page);
        return tags.stream()
                .map(tag -> modelMapper.map(tag, TagDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public TagDto findTagById(Long id) {
        Optional<Tag> optionalTag = tagRepository.findById(id);
        return optionalTag.map(tag -> modelMapper.map(tag, TagDto.class))
                .orElseThrow(() -> new GiftCertificateException(NON_EXISTENT_ENTITY));
    }

    @Transactional
    @Override
    public TagDto save(TagDto tagDto) throws ServiceException {
        if (isExists(tagDto)) {
           throw new ServiceException(String.format("The tag with name %s already exists", tagDto.getName()));
        }
        Tag savedTag = tagRepository.save(modelMapper.map(tagDto, Tag.class));
        return modelMapper.map(savedTag, TagDto.class);
    }

    @Transactional
    @Override
    public void deleteTag(Long id) {
        findTagById(id);
        tagRepository.deleteById(id);
    }

    @Override
    public boolean isExists(TagDto tagDto) {
        return tagRepository.findByName(tagDto.getName()).isPresent();
    }

    @Override
    public Long findByName(String name) throws ServiceException {
        Optional<Tag> tagOptional = tagRepository.findByName(name);
        if (tagOptional.isEmpty()) {
            throw new ServiceException(String.format("There is no tag with name %s", name));
        }
        return tagOptional.get().getId();
    }
}
