package com.blog.model;

import java.util.List;

public class PostListWrapper {
    List<ResponsePostDto> posts;
    Long countPages;
    Long countPosts;

    public List<ResponsePostDto> getPosts() {
        return posts;
    }

    public void setPosts(List<ResponsePostDto> posts) {
        this.posts = posts;
    }

    public Long getCountPages() {
        return countPages;
    }

    public void setCountPages(Long countPages) {
        this.countPages = countPages;
    }

    public Long getCountPosts() {
        return countPosts;
    }

    public void setCountPosts(Long countPosts) {
        this.countPosts = countPosts;
    }
}
