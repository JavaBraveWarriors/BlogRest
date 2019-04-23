package com.blog.dao.jdbc.mapper;

import com.blog.model.View;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The View row mapper.
 */
@Component
public class ViewRowMapper implements RowMapper<View> {
    /**
     * The constant ID.
     */
    public static final String ID = "view_id";
    /**
     * The constant AUTHOR_ID.
     */
    public static final String AUTHOR_ID = "author_id";
    /**
     * The constant POST_ID.
     */
    public static final String POST_ID = "post_id";
    /**
     * The constant INITIAL.
     */
    public static final String INITIAL = "initial";
    /**
     * The constant QUANTITY.
     */
    public static final String QUANTITY = "quantity";

    public View mapRow(ResultSet rs, int rowNum) throws SQLException {
        View view = new View();
        view.setId(rs.getLong(ID));
        view.setPostId(rs.getLong(POST_ID));
        view.setUserId(rs.getLong(AUTHOR_ID));
        return view;
    }
}