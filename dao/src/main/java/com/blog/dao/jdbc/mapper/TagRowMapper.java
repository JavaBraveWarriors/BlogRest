package com.blog.dao.jdbc.mapper;

import com.blog.model.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TagRowMapper implements RowMapper<Tag> {
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String PATH_IMAGE = "path_image";

    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Tag(
                rs.getLong(ID),
                rs.getString(TITLE),
                rs.getString(PATH_IMAGE)
        );
    }
}
