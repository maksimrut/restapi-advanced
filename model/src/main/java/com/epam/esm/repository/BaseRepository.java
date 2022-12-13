package com.epam.esm.repository;

import com.epam.esm.util.Page;

import java.util.List;
import java.util.Optional;

/**
 * The interface Base repository.
 *
 * @param <E> the type parameter
 *
 * @author Maksim Rutkouski
 */
public interface BaseRepository<E> {

    /**
     * Find all entities.
     *
     * @return the list
     */
    List<E> findAll(Page page);

    /**
     * Find entity by id optional.
     *
     * @param id the id
     * @return the optional
     */
    Optional<E> findById(Long id);

    /**
     * Create entity.
     *
     * @param entity the entity
     * @return the e
     */
    E save(E entity);

    /**
     * Delete entity by id.
     *
     * @param id the id
     */
    void deleteById(Long id);
}
