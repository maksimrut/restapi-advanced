package com.epam.esm.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.GiftCertificateException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.impl.GiftCertificateRepositoryImpl;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.util.CertificateQueryParameters;
import com.epam.esm.util.Page;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateRepository certificateRepository;
    private final ModelMapper modelMapper;
    private final TagService tagService;

    private static final int NON_EXISTENT_ENTITY = 40410;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepositoryImpl certificateRepository, TagService tagService, ModelMapper modelMapper) {
        this.certificateRepository = certificateRepository;
        this.tagService = tagService;
        this.modelMapper = modelMapper;
        this.modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        this.modelMapper.typeMap(GiftCertificate.class, GiftCertificate.class).addMappings(mapper -> {
           mapper.skip(GiftCertificate::setCreateDate);
           mapper.skip(GiftCertificate::setLastUpdateDate);
           mapper.skip(GiftCertificate::setTags);
        });
    }

    @Override
    public GiftCertificate findById(Long id) {
        return certificateRepository.findById(id)
                .orElseThrow(() -> new GiftCertificateException(NON_EXISTENT_ENTITY));
    }

    @Override
    public GiftCertificateDto findCertificateDtoById(Long id) {
        Optional<GiftCertificate> certificateOptional = certificateRepository.findById(id);
        return certificateOptional.map(giftCertificate -> modelMapper.map(giftCertificate, GiftCertificateDto.class))
                .orElseThrow(() -> new GiftCertificateException(NON_EXISTENT_ENTITY));
    }
    
    @Transactional
    @Override
    public GiftCertificateDto create(GiftCertificateDto certificateDto) throws ServiceException {
        GiftCertificate certificate = modelMapper.map(certificateDto, GiftCertificate.class);
        LocalDateTime currentTime = LocalDateTime.now();
        certificate.setCreateDate(currentTime);
        certificate.setLastUpdateDate(currentTime);
        GiftCertificate createdCertificate = certificateRepository.save(certificate);
        if (certificateDto.getTags() != null) {
            addTags(createdCertificate.getId(), certificateDto.getTags());
            createdCertificate.getTags().addAll(certificateDto.getTags());
        }
        return modelMapper.map(createdCertificate, GiftCertificateDto.class);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        clearTags(id);
        certificateRepository.deleteById(id);
    }

    @Transactional
    @Override
    public GiftCertificateDto update(Long id, GiftCertificateDto certificateDto) throws ServiceException {
        GiftCertificate certificate = certificateRepository.findById(id)
                .orElseThrow(ServiceException::new);
        modelMapper.map(certificateDto, certificate);
        certificate.setLastUpdateDate(LocalDateTime.now().withNano(0));
        certificateRepository.clearTags(id);
        if (certificateDto.getTags() != null) {
            addTags(id, certificateDto.getTags());
            certificate.getTags().addAll(certificateDto.getTags());
        }
        GiftCertificate updatedCertificate = certificateRepository.update(certificate);
        return modelMapper.map(updatedCertificate, GiftCertificateDto.class);
    }

    @Override
    public List<GiftCertificateDto> findAllByParams(CertificateQueryParameters queryParameters, Page page) {
        List<GiftCertificate> certificates = certificateRepository.findAllByParams(queryParameters, page);
        return certificates.stream()
                .map(giftCertificate -> modelMapper.map(giftCertificate, GiftCertificateDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void clearTags(Long giftCertificateId) {
        certificateRepository.clearTags(giftCertificateId);
    }

    private void addTags(Long giftCertificateId, Set<Tag> tags) throws ServiceException {
        for (Tag tag : tags) {
            TagDto tagDto = modelMapper.map(tag, TagDto.class);
            if (!tagService.isExists(tagDto)){
                tagService.save(tagDto);
            }
            Long tagId = tagService.findByName(tagDto.getName());
            certificateRepository.addTag(giftCertificateId, tagId);
        }
    }
}
