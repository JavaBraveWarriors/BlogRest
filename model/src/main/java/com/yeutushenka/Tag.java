package com.yeutushenka;


import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "TAG")
public class Tag {


    @Id
    @Column(name = "ID")
    @GeneratedValue
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "PATH_IMAGE")
    private String pathImage;

    public Tag(String title) {
        this.title = title;
    }

    public Tag(String title, String pathImage) {
        this.title = title;
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

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;
        Tag tag = (Tag) o;
        return Objects.equals(getId(), tag.getId()) &&
                Objects.equals(getTitle(), tag.getTitle()) &&
                Objects.equals(getPathImage(), tag.getPathImage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getPathImage());
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", pathImage='" + pathImage + '\'' +
                '}';
    }
}
