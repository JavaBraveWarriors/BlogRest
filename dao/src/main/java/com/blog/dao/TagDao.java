package com.blog.dao;

import com.blog.Tag;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface TagDao {

    List<Tag> getAllTags();

    Tag getTagById(Long id);

    List<Tag> getAllTagsByPostId(Long id) throws DataAccessException;

    Long addTag(Tag tag) throws DataAccessException;

    boolean updateTag(Tag tag) throws DataAccessException;

    boolean deleteTag(Long id) throws DataAccessException;

    boolean checkTagById(Long id);

    boolean checkTagByTitle(String title);

}
