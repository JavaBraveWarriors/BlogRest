package com.blog.dto;

import com.blog.model.Tag;

import javax.persistence.Entity;

@Entity
public class TagDto {

    private Long postId;

    private Tag tag;

    public TagDto(Long postId, Tag tag) {
        this.postId = postId;
        this.tag = tag;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}