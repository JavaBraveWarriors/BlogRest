package com.blog;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
public class Post {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Size(max = 200, message = "Title should be less than 200 characters.")
    private String title;

    @NotNull
    @Size(max = 600, message = "Description should be less than 600 characters.")
    private String description;

    @NotNull
    private String text;

    private LocalDate date;

    private String pathImage;

    @NotNull
    private Long authorId;

    @NotNull
    private List<Long> tagsId;

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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public List<Long> getTagsId() {
        return tagsId;
    }

    public void setTagsId(List<Tag> tags) {
        tags = tags;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post)) return false;
        Post post = (Post) o;
        return Objects.equals(getId(), post.getId()) &&
                Objects.equals(getTitle(), post.getTitle()) &&
                Objects.equals(getDescription(), post.getDescription()) &&
                Objects.equals(getText(), post.getText()) &&
                Objects.equals(getDate(), post.getDate()) &&
                Objects.equals(getPathImage(), post.getPathImage()) &&
                Objects.equals(getAuthorId(), post.getAuthorId()) &&
                Objects.equals(getTagsId(), post.getTagsId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getDescription(), getText(), getDate(), getPathImage(), getAuthorId(), getTagsId());
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", text='" + text + '\'' +
                ", date=" + date +
                ", pathImage='" + pathImage + '\'' +
                ", authorId=" + authorId +
                ", tagsId=" + tagsId +
                '}';
    }
}
