package com.blog;

import javax.persistence.Entity;

@Entity
public class PostForAdd extends Post {

    private Long[] tags;

    public PostForAdd() {
    }

    public PostForAdd(Long id, String title, String description, String text, String pathImage, Long authorId) {
        super(id, title, description, text, pathImage, authorId);
    }

    public Long[] getTags() {
        return tags;
    }

    public void setTags(Long[] tags) {
        this.tags = tags;
    }

}
