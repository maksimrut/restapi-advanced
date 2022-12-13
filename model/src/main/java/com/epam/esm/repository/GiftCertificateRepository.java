package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.util.CertificateQueryParameters;
import com.epam.esm.util.Page;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Gift certificate repository.
 *
 * @author Maksim Rutkouski
 */
@Repository
public interface GiftCertificateRepository extends BaseRepository<GiftCertificate> {

    /**
     * Update gift certificate.
     *
     * @param giftCertificate the gift certificate
     * @return the gift certificate
     */
    GiftCertificate update(GiftCertificate giftCertificate);

    /**
     * Add tag to the certificate.
     *
     * @param giftCertificateId the gift certificate id
     * @param tagId             the tag id
     */
    void addTag(Long giftCertificateId, Long tagId);

    /**
     * Clear tags.
     *
     * @param giftCertificateId the gift certificate id
     */
    void clearTags(Long giftCertificateId);

    /**
     * Find all certificates by params list with query.
     *
     * @param queryParameters the query parameters
     * @return the list
     */
    List<GiftCertificate> findAllByParams(CertificateQueryParameters queryParameters, Page page);
}
