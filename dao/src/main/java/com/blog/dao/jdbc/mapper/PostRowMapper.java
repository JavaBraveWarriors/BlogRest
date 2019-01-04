package com.blog.dao.jdbc.mapper;

import com.blog.Post;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PostRowMapper implements RowMapper<Post> {

    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String TEXT = "text";
    public static final String CREATED_DATE = "created_date";
    public static final String PATH_IMAGE = "path_image";
    public static final String AUTHOR_ID = "user_id";

    public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
        Post post = new Post(
                rs.getLong(ID),
                rs.getString(TITLE),
                rs.getString(DESCRIPTION),
                rs.getString(TEXT),
                rs.getString(PATH_IMAGE),
                rs.getLong(AUTHOR_ID)
        );
        Date date = rs.getDate(CREATED_DATE);
        if (date != null) {
            post.setDate(date.toLocalDate());
        }
        return post;
    }
}
