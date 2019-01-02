package com.yeutushenka.service;

import com.yeutushenka.Post;

import java.util.List;

public interface PostService {
    List<Post> getAllPostsByAuthorId(Long userId);

    List<Post> getAllPostsByAuthorIdAndTagId(Long userId, Long tagId);

    List<Post> getAllPosts();

    List<Post> getPostsByInitialIdAndQuantity(Long initial, Long quantity);

    Post getPostById(Long id);

    Long addPost(Post post);

    void updatePost(Post post);

    void deletePost(Long id);
}
