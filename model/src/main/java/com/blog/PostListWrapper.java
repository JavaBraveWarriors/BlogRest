package com.blog;

import java.util.List;

public class PostListWrapper {
    List<PostForGet> posts;
    Long countPages;
    Long countPosts;

    public List<PostForGet> getPosts() {
        return posts;
    }

    public void setPosts(List<PostForGet> posts) {
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
