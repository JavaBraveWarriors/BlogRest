package com.blog.dao.jdbc.mapper;

import com.blog.model.Author;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * The Author row mapper.
 */
@Component
public class AuthorRowMapper implements RowMapper<Author> {

    /**
     * The constant ID.
     */
    public static final String ID = "id";
    /**
     * The constant MAIL.
     */
    public static final String MAIL = "mail";
    /**
     * The constant LOGIN.
     */
    public static final String LOGIN = "login";
    /**
     * The constant PASSWORD.
     */
    public static final String PASSWORD = "password";
    /**
     * The constant FIRST_NAME.
     */
    public static final String FIRST_NAME = "first_name";
    /**
     * The constant LAST_NAME.
     */
    public static final String LAST_NAME = "last_name";
    /**
     * The constant REGISTRATION_DATE.
     */
    public static final String REGISTRATION_DATE = "registration_date";
    /**
     * The constant PHONE.
     */
    public static final String PHONE = "phone";
    /**
     * The constant DESCRIPTION.
     */
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
