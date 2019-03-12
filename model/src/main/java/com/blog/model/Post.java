package com.blog.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
public class Post {

    @Id
    @GeneratedValue
    protected Long id;

    @NotNull
    @Size(max = 200, message = "Title should be less than 200 characters.")
    protected String title;

    @NotNull
    @Size(max = 600, message = "Description should be less than 600 characters.")
    protected String description;

    @NotNull
    protected String text;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss±hh")
    protected LocalDateTime timeOfCreation;

    protected String pathImage;

    @NotNull
    protected Long authorId;

    public Post() {
    }

    public Post(Long id, String title, String description, String text, String pathImage, Long authorId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.text = text;
        this.pathImage = pathImage;
        this.authorId = authorId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getTimeOfCreation() {
        return timeOfCreation;
    }

    public void setTimeOfCreation(LocalDateTime timeOfCreation) {
        this.timeOfCreation = timeOfCreation;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

}
