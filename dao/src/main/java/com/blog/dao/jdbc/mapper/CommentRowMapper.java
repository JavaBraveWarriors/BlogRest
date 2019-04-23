package com.blog.dao.jdbc.mapper;

import com.blog.model.Comment;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * The Comment row mapper.
 */
@Component
public class CommentRowMapper implements RowMapper<Comment> {

    /**
     * The constant ID.
     */
    public static final String ID = "comment_id";
    /**
     * The constant TEXT.
     */
    public static final String TEXT = "comment_text";
    /**
     * The constant CREATED_DATE.
     */
    public static final String CREATED_DATE = "comment_created_date";
    /**
     * The constant POST_ID.
     */
    public static final String POST_ID = "comment_post_id";
    /**
     * The constant AUTHOR_ID.
     */
    public static final String AUTHOR_ID = "comment_author_id";
    /**
     * The constant INITIAL.
     */
    public static final String INITIAL = "initial";
    /**
     * The constant QUANTITY.
     */
    public static final String QUANTITY = "quantity";
    /**
     * The constant AUTHOR_FIRST_NAME.
     */
    public static final String AUTHOR_FIRST_NAME = "first_name";
    /**
     * The constant AUTHOR_LAST_NAME.
     */
    public static final String AUTHOR_LAST_NAME = "last_name";

    public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
        Comment comment = new Comment();
        comment.setId(rs.getLong(ID));
        comment.setText(rs.getString(TEXT));
        comment.setAuthorFirstName(rs.getString(AUTHOR_FIRST_NAME));
        comment.setAuthorLastName(rs.getString(AUTHOR_LAST_NAME));
        comment.setAuthorId(rs.getLong(AUTHOR_ID));
        comment.setPostId(rs.getLong(POST_ID));
        comment.setId(rs.getLong(ID));
        Timestamp date = rs.getTimestamp(CREATED_DATE);
        if (date != null) {
            comment.setTimeOfCreation(date.toLocalDateTime());
        }
        return comment;
    }
}