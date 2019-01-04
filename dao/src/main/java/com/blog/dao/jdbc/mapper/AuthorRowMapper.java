package com.blog.dao.jdbc.mapper;

import com.blog.Author;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.blog.dao.jdbc.AuthorDaoImpl.*;

@Component
public class AuthorRowMapper implements RowMapper<Author> {


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
        Date date = rs.getDate(REGISTRATION_DATE);
        if (date != null) {
            author.setRegistrationTime(date.toLocalDate());
        }
        return author;
    }
}
