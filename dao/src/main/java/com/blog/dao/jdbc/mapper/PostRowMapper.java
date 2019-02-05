package com.blog.dao.jdbc.mapper;

import com.blog.Post;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

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
    public static final String AUTHOR_ID = "author_id";
    public static final String INITIAL = "initial";
    public static final String QUANTITY = "quantity";
    public static final String TAG_ID = "tag_id";
    public static final String COMMENTS_COUNT = "comments_count";

    public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
        Post post = PostShortRowMapper.rowFields(rs);
        post.setText(rs.getString(TEXT));
        return post;
    }
}
