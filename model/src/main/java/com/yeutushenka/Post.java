package com.yeutushenka;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "TAG")
public class Post {

    @Id
    @Column(name = "ID")
    @GeneratedValue
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "TEXT")
    private String text;

    @Column(name = "CREATED_TIME")
    private LocalDate date;

    @Column(name = "PATH_IMAGE")
    private String pathImage;

    public Post(String title, String description, String text, LocalDate date) {
        this.title = title;
        this.description = description;
        this.text = text;
        this.date = date;
    }

    public Post(String title, String description, String text, LocalDate date, String pathImage) {
        this.title = title;
        this.description = description;
        this.text = text;
        this.date = date;
        this.pathImage = pathImage;
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
                Objects.equals(getPathImage(), post.getPathImage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getDescription(), getText(), getDate(), getPathImage());
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
                '}';
    }
}
