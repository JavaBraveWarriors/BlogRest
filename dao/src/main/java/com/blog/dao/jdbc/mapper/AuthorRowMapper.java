package com.blog.dao.jdbc.mapper;

import com.blog.model.Author;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class AuthorRowMapper implements RowMapper<Author> {

    public static final String ID = "id";
    public static final String MAIL = "mail";
    public static final String LOGIN = "login";
    public static final String PASSWORD = "password";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String REGISTRATION_DATE = "registration_date";
    public static final String PHONE = "phone";
    public static final String DESCRIPTION = "description";

    public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
        Author author = new Author(
                rs.getLong(ID),
                rs.getString(MAIL),
                rs.getString(LOGIN),
                rs.getString(PASSWORD),
                rs.getString(FIRST_NAME),
                rs.getString(LAST_NAME),
                rs.getString(DESCRIPTION),
                rs.getString(PHONE)
        );
        Timestamp date = rs.getTimestamp(REGISTRATION_DATE);
        if (date != null) {
            author.setRegistrationTime(date.toLocalDateTime());
        }
        return author;
    }
}
