package com.blog.model;

import javax.persistence.Entity;
import java.util.List;

@Entity
public class CommentListWrapper {
    List<Comment> commentsPage;
    Long countPages;
    Long countCommentsInPost;

    public List<Comment> getCommentsPage() {
        return commentsPage;
    }

    public void setCommentsPage(List<Comment> commentsPage) {
        this.commentsPage = commentsPage;
    }

    public Long getCountPages() {
        return countPages;
    }

    public void setCountPages(Long countPages) {
        this.countPages = countPages;
    }

    public Long getCountCommentsInPost() {
        return countCommentsInPost;
    }

    public void setCountCommentsInPost(Long countCommentsInPost) {
        this.countCommentsInPost = countCommentsInPost;
    }
}
