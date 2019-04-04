package com.blog.controller;

import com.blog.model.Tag;
import com.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;

/**
 * The Tag rest controller provides an interface to interact with our rest-API service to Tag model.
 *
 * @author Aliaksandr Yeutushenka
 * @see TagService
 */
@RestController
@RequestMapping("/tags")
public class TagRestController {
    private TagService tagService;

    /**
     * Instantiates a new Tag rest controller.
     *
     * @param tagService the tag service
     */
    @Autowired
    public TagRestController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * Gets the list of objects of the all tags.
     *
     * @return {List<Tag>} is a list of all tags.
     */
    @GetMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public List<Tag> getAllTags() {
        return tagService.getAllTags();
    }

    /**
     * Gets a {Tag} object where id is equal to argument parameter.
     *
     * @param id {Long} value the ID of the tag you want to get.
     * @return {Tag} is a object which has this ID.
     */
    @GetMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public Tag getTagById(@PathVariable(value = "id") Long id) {
        return tagService.getTagById(id);
    }

    /**
     * Add new tag.
     *
     * @param tag               {Tag} to be added.
     * @param validationResults the validation results of tag object.
     * @return {Long} is the value that is the id of the new tag.
     */
    @PostMapping("")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Long addTag(@Valid @RequestBody Tag tag, BindingResult validationResults) {
        if (validationResults.hasErrors()) {
            throw new ValidationException(validationResults.getFieldErrors().toString());
        } else {
            return tagService.addTag(tag);
        }
    }

    /**
     * Update tag.
     *
     * @param tag               {Tag} to be updated.
     * @param validationResults the validation results of tag object.
     */
    @PutMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateTag(@Valid @RequestBody Tag tag, BindingResult validationResults) {
        if (validationResults.hasErrors()) {
            throw new ValidationException(validationResults.getFieldErrors().toString());
        } else {
            tagService.updateTag(tag);
        }
    }

    /**
     * Delete tag using tag ID.
     *
     * @param id is {Long} value which identifies the tag ID.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteTag(@PathVariable(value = "id") Long id) {
        tagService.deleteTag(id);
    }
}