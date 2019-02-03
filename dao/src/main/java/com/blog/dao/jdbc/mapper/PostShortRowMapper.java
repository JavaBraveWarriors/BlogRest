package com.blog.dao.jdbc.mapper;

import com.blog.Post;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import static com.blog.dao.jdbc.mapper.PostRowMapper.*;

public class PostShortRowMapper implements RowMapper<Post> {
    @Override
    public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rowFields(rs);
    }

    public static Post rowFields(ResultSet rs) throws SQLException {
        Post post = new Post();
        post.setId(rs.getLong(ID));
        post.setTitle(rs.getString(TITLE));
        post.setDescription(rs.getString(DESCRIPTION));
        post.setPathImage(rs.getString(PATH_IMAGE));
        post.setAuthorId(rs.getLong(AUTHOR_ID));
        Timestamp date = rs.getTimestamp(CREATED_DATE);
        if (date != null) {
            post.setTimeOfCreation(date.toLocalDateTime());
        }
        return post;
    }
}
