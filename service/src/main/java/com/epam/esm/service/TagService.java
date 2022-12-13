package com.epam.esm.service;

import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.util.Page;

import java.util.List;

/**
 * The Tag service interface.
 * Specifies a set of methods to get and manage tags
 *
 * @author Maksim Rutkouski
 */
public interface TagService {

    /**
     * Find all list.
     *
     * @return the list
     */
    List<TagDto> findAllTags(Page page);

    /**
     * Find tag by id.
     *
     * @param id the id
     * @return the optional with a tag object if it exists, otherwise the empty optional
     */
    TagDto findTagById(Long id);

    /**
     * Save tag.
     *
     * @param tagDto the tag object
     * @return the created tag
     * @throws ServiceException the service exception if this tag already exists in the database
     */
    TagDto save(TagDto tagDto) throws ServiceException;

    /**
     * Delete the tag by id.
     *
     * @param id the id
     */
    void deleteTag(Long id);

    /**
     * boolean check if tag exists.
     *
     * @param tagDto the tag
     * @return the boolean
     */
    boolean isExists(TagDto tagDto);

    /**
     * long, find tag by name.
     *
     * @param name the name
     * @return the long
     * @throws ServiceException the service exception if this tag doesn't exist.
     */
    Long findByName(String name) throws ServiceException;
}
