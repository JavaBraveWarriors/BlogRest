package com.blog;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class Comment {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Size(max = 600, message = "Text should be less than 600 characters.")
    private String text;

    @NotNull
    private Long authorId;

    @NotNull
    private Long postId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss±hh")
    private LocalDateTime timeOfCreation;

    public Comment() {
    }

    public Comment(@NotNull @Size(max = 600, message = "Text should be less than 600 characters.") String text, @NotNull Long authorId, @NotNull Long postId) {
        this.text = text;
        this.authorId = authorId;
        this.postId = postId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public LocalDateTime getTimeOfCreation() {
        return timeOfCreation;
    }

    public void setTimeOfCreation(LocalDateTime timeOfCreation) {
        this.timeOfCreation = timeOfCreation;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", authorId=" + authorId +
                ", postId=" + postId +
                ", timeOfCreation=" + timeOfCreation +
                '}';
    }
}
