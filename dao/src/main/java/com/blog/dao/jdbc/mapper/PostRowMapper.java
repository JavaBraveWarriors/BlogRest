package com.blog.dao.jdbc.mapper;

import com.blog.model.ResponsePostDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The Post row mapper.
 */
@Component
public class PostRowMapper implements RowMapper<ResponsePostDto> {

    /**
     * The constant ID.
     */
    public static final String ID = "id";
    /**
     * The constant TITLE.
     */
    public static final String TITLE = "title";
    /**
     * The constant DESCRIPTION.
     */
    public static final String DESCRIPTION = "description";
    /**
     * The constant TEXT.
     */
    public static final String TEXT = "text";
    /**
     * The constant CREATED_DATE.
     */
    public static final String CREATED_DATE = "created_date";
    /**
     * The constant PATH_IMAGE.
     */
    public static final String PATH_IMAGE = "path_image";
    /**
     * The constant AUTHOR_ID.
     */
    public static final String AUTHOR_ID = "author_id";
    /**
     * The constant INITIAL.
     */
    public static final String INITIAL = "initial";
    /**
     * The constant QUANTITY.
     */
    public static final String QUANTITY = "quantity";
    /**
     * The constant TAG_ID.
     */
    public static final String TAG_ID = "tag_id";
    /**
     * The constant COMMENTS_COUNT.
     */
    public static final String COMMENTS_COUNT = "comments_count";
    /**
     * The constant VIEWS_COUNT.
     */
    public static final String VIEWS_COUNT = "views_count";
    /**
     * The constant AUTHOR_FIRST_NAME.
     */
    public static final String AUTHOR_FIRST_NAME = "first_name";
    /**
     * The constant AUTHOR_LAST_NAME.
     */
    public static final String AUTHOR_LAST_NAME = "last_name";

    private PostShortRowMapper postShortRowMapper;

    /**
     * Instantiates a new Post row mapper.
     *
     * @param postShortRowMapper the post short row mapper
     */
    @Autowired
    public PostRowMapper(PostShortRowMapper postShortRowMapper) {
        this.postShortRowMapper = postShortRowMapper;
    }

    public ResponsePostDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResponsePostDto post = postShortRowMapper.mapRow(rs, rowNum);
        post.setText(rs.getString(TEXT));
        return post;
    }
}
