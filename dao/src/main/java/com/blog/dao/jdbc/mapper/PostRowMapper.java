package com.blog.dao.jdbc.mapper;

import com.blog.ResponsePostDto;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PostRowMapper implements RowMapper<ResponsePostDto> {

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
    public static final String AUTHOR_FIRST_NAME = "first_name";
    public static final String AUTHOR_LAST_NAME = "last_name";

    public ResponsePostDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResponsePostDto post = PostShortRowMapper.rowFields(rs);
        post.setText(rs.getString(TEXT));
        return post;
    }
}
