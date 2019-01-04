package com.blog.dao;

import com.blog.Tag;

import java.util.List;

public interface TagDao {

    List<Tag> getAllTags();

    Tag getTagById(Long id);

    boolean checkTagById(Long id);
}
