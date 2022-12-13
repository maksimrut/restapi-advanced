package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.util.CertificateQueryParameters;
import com.epam.esm.util.Page;

import java.util.List;

/**
 * The interface Gift certificate service.
 *
 * @author Maksim Rutkouski
 */
public interface GiftCertificateService {

    /**
     * Find certificate by id.
     *
     * @param id the id
     * @return the optional with a certificate object if it exists, otherwise the empty optional
     */
    GiftCertificate findById(Long id);

    /**
     * Find certificate by id.
     *
     * @param id the id
     * @return the optional with a certificate object if it exists, otherwise the empty optional
     */
    GiftCertificateDto findCertificateDtoById(Long id);

    /**
     * Create or save gift certificate.
     *
     * @param certificateDto the certificate
     * @return the created certificate
     * @throws ServiceException the service exception
     */
    GiftCertificateDto create(GiftCertificateDto certificateDto) throws ServiceException;

    /**
     * Delete certificate by id.
     *
     * @param id the id
     */
    void deleteById(Long id);

    /**
     * Update gift certificate.
     *
     * @param id          the id
     * @param certificateDto the certificate
     * @return the gift certificate
     * @throws ServiceException the service exception if the certificate with id doesn't exist.
     */
    GiftCertificateDto update(Long id, GiftCertificateDto certificateDto) throws ServiceException;

    /**
     * Find all certificate by query params.
     *
     * @param queryParameters the query parameters
     * @param page chosen page
     * @return the list of found certificates
     */
    List<GiftCertificateDto> findAllByParams(CertificateQueryParameters queryParameters, Page page);

    /**
     * Clear tags.
     *
     * @param giftCertificateId the gift certificate id
     */
    void clearTags(Long giftCertificateId);
}
