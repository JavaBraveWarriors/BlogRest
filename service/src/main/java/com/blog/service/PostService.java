package com.blog.service;

import com.blog.Post;

import java.util.List;

public interface PostService {
    List<Post> getAllPostsByAuthorId(Long userId);

    List<Post> getAllPosts();

    List<Post> getPostsByInitialIdAndQuantity(Long initial, Long quantity);

    List<Post> getAllPostsByTagId(Long tagId);

    Post getPostById(Long id);

    Long addPost(Post post);

    void addTagToPost(Long postId, Long tagId);


    void updatePost(Post post);

    void deletePost(Long id);
}
