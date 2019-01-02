package com.yeutushenka.dao;

import com.yeutushenka.Post;
import com.yeutushenka.ResponseStatus;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface PostDao {

    List<Post> getAllPostsByAuthorId(Long userId) throws DataAccessException;

    List<Post> getAllPostsByAuthorIdAndTagId(Long userId, Long tagId) throws DataAccessException;

    List<Post> getAllPosts() throws DataAccessException;

    List<Post> getPostsByInitialIdAndQuantity(Long initial, Long quantity) throws DataAccessException;

    Post getPostById(Long id) throws DataAccessException;

    Long addPost(Post post) throws DataAccessException;

    ResponseStatus updatePost(Post post) throws DataAccessException;

    ResponseStatus deletePost(Long id) throws DataAccessException;

    boolean checkPostById(Long id);

    boolean checkPostByAuthorId(Long userId);

}
