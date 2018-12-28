package com.yeutushenka.dao;

import com.yeutushenka.Post;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface PostDao {

    List<Post> getAllPostsByUserId(Long userId) throws DataAccessException;

    List<Post> getAllPostsByTagId(Long userId, Long tagId) throws DataAccessException;

    List<Post> getAllPosts() throws DataAccessException;

    //List<Post> getPostsByInitialIdAndQuantity(Long id, int quantity) throws DataAccessException;

    Post getPostById(Long id) throws DataAccessException;

    Long addPost(Post post) throws DataAccessException;

    int updatePost(Post post) throws DataAccessException;

    int deletePost(Long id) throws DataAccessException;

    boolean checkPostById(Long id);

    boolean checkPostByUserId(Long userId);

}
