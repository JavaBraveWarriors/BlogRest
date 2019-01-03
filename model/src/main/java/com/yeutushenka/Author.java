package com.yeutushenka;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Author {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Size(max = 100, message = "mail should be less than 100 characters.")
    @Email
    private String mail;

    @NotNull
    @Size(max = 100, message = "login should be less than 100 characters.")
    private String login;

    @NotNull
    @Size(max = 255, message = "password should be less than 255 characters.")
    private String password;

    @NotNull
    @Size(max = 45, message = "firstName should be less than 255 characters.")
    private String firstName;

    @NotNull
    @Size(max = 45, message = "lastName should be less than 255 characters.")
    private String lastName;


    private LocalDate registrationTime;

    private String phone;

    private String description;

    public Author() {
    }

    public Author(Long id, String mail, String login, String password, String firstName, String lastName, String description, String phone) {
        this.id = id;
        this.mail = mail;
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.description = description;
        this.phone = phone;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(LocalDate registrationTime) {
        this.registrationTime = registrationTime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Author)) return false;
        Author author = (Author) o;
        return Objects.equals(getId(), author.getId()) &&
                Objects.equals(getMail(), author.getMail()) &&
                Objects.equals(getLogin(), author.getLogin()) &&
                Objects.equals(getPassword(), author.getPassword()) &&
                Objects.equals(getFirstName(), author.getFirstName()) &&
                Objects.equals(getLastName(), author.getLastName()) &&
                Objects.equals(getRegistrationTime(), author.getRegistrationTime()) &&
                Objects.equals(getPhone(), author.getPhone()) &&
                Objects.equals(getDescription(), author.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getMail(), getLogin(), getPassword(), getFirstName(), getLastName(), getRegistrationTime(), getPhone(), getDescription());
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", mail='" + mail + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", registrationTime='" + registrationTime + '\'' +
                ", phone='" + phone + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
