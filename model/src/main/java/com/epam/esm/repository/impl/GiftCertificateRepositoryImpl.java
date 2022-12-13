package com.epam.esm.repository.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.util.CertificateQueryCreator;
import com.epam.esm.util.CertificateQueryParameters;
import com.epam.esm.util.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {

    private static final String FIND_ALL =
            "SELECT gc FROM GiftCertificate gc";
    private static final String SQL_ADD_TAG_TO_CERTIFICATE =
            "INSERT INTO tags_certificates (gift_certificate_id, tag_id) VALUES (:gift_certificate_id, :tag_id)";
    private static final String SQL_CLEAR_TAGS =
            "DELETE FROM tags_certificates WHERE gift_certificate_id = :gift_certificate_id";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<GiftCertificate> findAll(Page page) {
        return entityManager.createQuery(FIND_ALL, GiftCertificate.class)
                .setFirstResult((page.getNumber() - 1) * page.getLimit())
                .setMaxResults(page.getLimit())
                .getResultList();
    }

    @Override
    public Optional<GiftCertificate> findById(Long id) {
        return Optional.ofNullable(entityManager.find(GiftCertificate.class, id));
    }

    @Override
    public GiftCertificate save(GiftCertificate certificate) {
        entityManager.persist(certificate);
        return certificate;
    }

    @Override
    public void deleteById(Long id) {
        GiftCertificate giftCertificate = entityManager.find(GiftCertificate.class, id);
        entityManager.remove(giftCertificate);
    }

    @Override
    public GiftCertificate update(GiftCertificate certificate) {
        return entityManager.merge(certificate);
    }

    @Override
    public void addTag(Long giftCertificateId, Long tagId) {
        entityManager.createNativeQuery(SQL_ADD_TAG_TO_CERTIFICATE)
                .setParameter("gift_certificate_id", giftCertificateId)
                .setParameter("tag_id", tagId)
                .executeUpdate();
    }

    @Override
    public void clearTags(Long giftCertificateId) {
        entityManager.createNativeQuery(SQL_CLEAR_TAGS)
                .setParameter("gift_certificate_id", giftCertificateId)
                .executeUpdate();
    }

    @Override
    public List<GiftCertificate> findAllByParams(CertificateQueryParameters queryParameters, Page page) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = CertificateQueryCreator.createQuery(queryParameters, criteriaBuilder);
        return entityManager.createQuery(criteriaQuery)
                .setFirstResult((page.getNumber() - 1) * page.getLimit())
                .setMaxResults(page.getLimit())
                .getResultList();
    }
}
