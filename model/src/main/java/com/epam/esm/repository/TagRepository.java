package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Tag repository.
 *
 * @author Maksim Rutkouski
 */
@Repository
public interface TagRepository extends BaseRepository<Tag> {

    /**
     * Find tag by name optional.
     *
     * @param name the name
     * @return the optional
     */
    Optional<Tag> findByName(String name);

    /**
     * Find all tags by gift certificate id.
     *
     * @param id the id
     * @return the list
     */
    List<Tag> findAllByGiftCertificateId(Long id);
}
