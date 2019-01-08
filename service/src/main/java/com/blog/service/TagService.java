package com.blog.service;

import com.blog.Tag;

import java.util.List;

public interface TagService {



    List<Tag> getAllTags();

    Tag getTagById(Long id);

    List<Tag> getAllTagsByPostId(Long id);

    Long addTag(Tag tag);

    void updateTag(Tag tag);

    void deleteTag(Long id);

    void validateTagId(Long id);
}
