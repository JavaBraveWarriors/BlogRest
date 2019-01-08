package com.blog.dao;

import com.blog.Post;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface PostDao {

    List<Post> getAllPostsByAuthorId(Long userId) throws DataAccessException;

    List<Post> getAllPosts() throws DataAccessException;

    List<Post> getPostsByInitialIdAndQuantity(Long initial, Long quantity) throws DataAccessException;

    List<Post> getAllPostsByTagId(Long tagId) throws DataAccessException;

    Post getPostById(Long id) throws DataAccessException;

    Long addPost(Post post) throws DataAccessException;

    boolean addTagToPost(Long id, Long tagId) throws DataAccessException;

    boolean updatePost(Post post) throws DataAccessException;

    boolean deletePost(Long id) throws DataAccessException;

    boolean deleteTagInPost(Long id, Long tagId) throws DataAccessException;

    boolean checkPostById(Long id);

    boolean checkTagInPostById(Long id, Long tagId);

    boolean checkPostByAuthorId(Long userId);

}
