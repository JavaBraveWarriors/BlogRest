package com.blog.dao.jdbc.mapper;

import com.blog.Comment;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class CommentRowMapper implements RowMapper<Comment> {

    public static final String ID = "comment_id";
    public static final String TEXT = "comment_text";
    public static final String CREATED_DATE = "comment_created_date";
    public static final String POST_ID = "comment_post_id";
    public static final String AUTHOR_ID = "comment_author_id";
    public static final String INITIAL = "initial";
    public static final String QUANTITY = "quantity";

    @Override
    public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
        Comment comment = new Comment();
        comment.setId(rs.getLong(ID));
        comment.setText(rs.getString(TEXT));
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
