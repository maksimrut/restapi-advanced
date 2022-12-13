package com.epam.esm.controller;

import com.epam.esm.exception.ResourceDuplicateException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/tags")
public class TagController {
    private final TagService service;

    @Autowired
    public TagController(TagService service) {
        this.service = service;
    }

    /**
     * Get all tags.
     *
     * @param pageNumber the page number for pagination
     * @param limit the one page records limit
     * @return all available tags
     */
    @GetMapping
    public List<TagDto> findAll(@RequestParam(required = false, defaultValue = "1") int pageNumber,
                                @RequestParam(required = false, defaultValue = "10") int limit) {
        Page page = new Page(pageNumber, limit);
        List<TagDto> tagsDto = service.findAllTags(page);
        tagsDto.forEach(this::addRelationship);
        return tagsDto;
    }

    /**
     * Get a tag by id.
     *
     * @param id the tag id
     * @return found tag, otherwise error response with 40401 status code
     */
    @GetMapping("/{id}")
    public TagDto findById(@PathVariable("id") Long id) {
        TagDto tagDto = service.findTagById(id);
        addRelationship(tagDto);
        return tagDto;
    }

    /**
     * Create a tag.
     *
     * @param tagDto the tag json object
     * @return created tag
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagDto create(@RequestBody @Valid TagDto tagDto) {
        try {
            TagDto savedTag = service.save(tagDto);
            addRelationship(savedTag);
            return savedTag;
        } catch (ServiceException e) {
            throw new ResourceDuplicateException("name", tagDto.getName());
        }
    }

    /**
     * Delete a tag.
     *
     * @param id the tag id
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.deleteTag(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private void addRelationship(TagDto tagDto) {
        tagDto.add(linkTo(methodOn(TagController.class).findById(tagDto.getId())).withSelfRel());
        tagDto.add(linkTo(methodOn(TagController.class).delete(tagDto.getId())).withRel("delete"));
    }
}
