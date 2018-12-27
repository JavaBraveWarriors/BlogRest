package com.yeutushenka.dao;

import com.yeutushenka.Post;
import com.yeutushenka.Tag;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface PostDao {

    List<Post> getAllPostsByUserIdAndTags(Long userId, List<Tag> tags) throws DataAccessException;

    List<Post> getAllPostsByUserId(Long userId) throws DataAccessException;

    List<Post> getAllPosts() throws DataAccessException;

    /*List<Post> getPostsByInitialIdAndQuantity(Long id, int quantity) throws DataAccessException;

    List<Post> getPostsByInitialIdAndQuantityAndTags(Long id, int quantity, List<Tag> tags) throws DataAccessException;

    List<Post> getPostsByInitialIdAndQuantityAndUserId(Long id, int quantity, Long userId) throws DataAccessException;*/

    Post getPostById(Long id) throws DataAccessException;

    Long addPost(Post post) throws DataAccessException;

    int updatePost(Post post) throws DataAccessException;

    int deletePost(Long id) throws DataAccessException;

    boolean checkPostById(Long id);

    boolean checkPostByUserId(Long userId);

}
