package com.blog.controller;

import com.blog.Tag;
import com.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagRestController {
    private TagService tagService;

    @Autowired
    public TagRestController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public List<Tag> getAllPosts() {
        return tagService.getAllTags();
    }

    @GetMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public Tag getTagById(@PathVariable(value = "id") Long id) {
        return tagService.getTagById(id);
    }

    @PostMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public Long addTag(@RequestBody Tag tag) {
        return tagService.addTag(tag);
    }

    @PutMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateTag(@RequestBody Tag tag) {
        tagService.updateTag(tag);
    }

    @DeleteMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteTag(@PathVariable(value = "id") Long id) {
        tagService.deleteTag(id);
    }


}
