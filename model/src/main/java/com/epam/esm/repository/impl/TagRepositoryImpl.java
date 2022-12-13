package com.epam.esm.repository.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.util.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class TagRepositoryImpl implements TagRepository{

    private static final String FIND_ALL = "SELECT t FROM Tag t";
    private static final String FIND_BY_NAME = "SELECT t FROM Tag t WHERE t.name = :name";
    private static final String FIND_BY_CERTIFICATE_ID = "SELECT t FROM GiftCertificate gc " +
            "JOIN gc.tags t WHERE gc.id = :id";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Tag> findAll(Page page) {
        return entityManager.createQuery(FIND_ALL, Tag.class)
                .setFirstResult((page.getNumber() - 1) * page.getLimit())
                .setMaxResults(page.getLimit())
                .getResultList();
    }

    @Override
    public Optional<Tag> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Tag.class, id));
    }

    @Override
    public Tag save(Tag tag) {
        entityManager.persist(tag);
        return tag;
    }

    @Override
    public void deleteById(Long id) {
        Tag tag = entityManager.find(Tag.class, id);
        entityManager.remove(tag);
    }

    @Override
    public Optional<Tag> findByName(String name) {
        return entityManager.createQuery(FIND_BY_NAME, Tag.class)
                .setParameter("name", name)
                .getResultList()
                .stream().findAny();
    }

    @Override
    public List<Tag> findAllByGiftCertificateId(Long id) {
        return entityManager.createQuery(FIND_BY_CERTIFICATE_ID, Tag.class)
                .setParameter("id", id)
                .getResultList();
    }
}
