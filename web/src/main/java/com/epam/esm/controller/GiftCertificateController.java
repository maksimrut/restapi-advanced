package com.epam.esm.controller;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.CustomInternalException;
import com.epam.esm.exception.CustomNotFoundException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.util.CertificateQueryParameters;
import com.epam.esm.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * The Tag REST API controller.
 *
 * @author Maksim Rutkouski
 */
@RestController
@RequestMapping("/certificates")
public class GiftCertificateController {
    private final GiftCertificateService certificateService;

    @Autowired
    public GiftCertificateController(GiftCertificateService certificateService) {
        this.certificateService = certificateService;
    }

    /**
     * Get a certificate by id.
     *
     * @param id the certificate id
     * @return found certificate, otherwise error response with 40401 status code
     */
    @GetMapping("/{id}")
    public GiftCertificateDto findById(@PathVariable("id") Long id) {
        GiftCertificateDto foundCertificate = certificateService.findCertificateDtoById(id);
        addRelationship(foundCertificate);
        return foundCertificate;
    }

    /**
     * Create a certificate.
     *
     * @param certificateDto the certificate json object
     * @return created certificate
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificateDto create(@RequestBody @Valid GiftCertificateDto certificateDto) {
        try {
            GiftCertificateDto savedCertificate = certificateService.create(certificateDto);
            addRelationship(savedCertificate);
            return savedCertificate;
        } catch (ServiceException e) {
            throw new CustomInternalException("The certificate can not be created at the moment");
        }
    }

    /**
     * Delete a certificate.
     *
     * @param id the certificate id
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        certificateService.deleteById(id);
    }


    /**
     * Update gift certificate.
     *
     * @param id     the id
     * @param source the source
     * @return the updated gift certificate
     */
    @PutMapping("/{id}")
    public GiftCertificateDto update(@PathVariable("id") Long id, @RequestBody GiftCertificateDto source) {
        try {
            GiftCertificateDto updatedCertificate = certificateService.update(id, source);
            addRelationship(updatedCertificate);
            return updatedCertificate;
        } catch (ServiceException e) {
            throw new CustomNotFoundException(id);
        }
    }

    /**
     * Get all certificates by parameters.
     *
     * @param tagNames request parameter for searching by several tags
     * @param partName the part of name/description of a certificate
     * @param sortByName allow certificate sorting by a Name
     * @param sortByDate allow certificate sorting by a Date
     */
    @GetMapping
    public List<GiftCertificateDto> findAllByParams(
            @RequestParam(value = "tag", required = false) String[] tagNames,
            @RequestParam(value = "partName", required = false) String partName,
            @RequestParam(value = "sortByName", required = false) String sortByName,
            @RequestParam(value = "sortByDate",required = false) String sortByDate,
            @RequestParam(required = false, defaultValue = "1") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int limit) {
        CertificateQueryParameters queryParameters =
                new CertificateQueryParameters(tagNames, partName, sortByName, sortByDate);
        Page page = new Page(pageNumber, limit);
        List<GiftCertificateDto> foundCertificates = certificateService.findAllByParams(queryParameters, page);
        foundCertificates.forEach(this::addRelationship);
        return foundCertificates;
    }

    /**
     * Delete all tags from the chosen certificate
     *
     * @param id the certificate id
     */
    @DeleteMapping("/{id}/tags")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearTags(@PathVariable("id") Long id) {
        certificateService.clearTags(id);
    }

    private void addRelationship(GiftCertificateDto giftCertificateDto) {
        giftCertificateDto.add(linkTo(methodOn(GiftCertificateController.class).findById(giftCertificateDto.getId())).withSelfRel());
        giftCertificateDto.getTags().forEach(this::addTagRelationship);
    }

    private void addTagRelationship(Tag tag) {
        TagDto tagDto = new TagDto(tag.getId(), tag.getName());
        tagDto.add(linkTo(methodOn(TagController.class).findById(tagDto.getId())).withRel("tag"));
    }
}
