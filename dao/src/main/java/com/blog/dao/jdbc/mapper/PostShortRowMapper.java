package com.blog.dao.jdbc.mapper;

import com.blog.ResponsePostDto;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import static com.blog.dao.jdbc.mapper.PostRowMapper.*;

@Component
public class PostShortRowMapper implements RowMapper<ResponsePostDto> {
    @Override
    public ResponsePostDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rowFields(rs);
    }

    public static ResponsePostDto rowFields(ResultSet rs) throws SQLException {
        ResponsePostDto post = new ResponsePostDto();
        post.setId(rs.getLong(ID));
        post.setTitle(rs.getString(TITLE));
        post.setDescription(rs.getString(DESCRIPTION));
        post.setPathImage(rs.getString(PATH_IMAGE));
        post.setCommentsCount(rs.getLong(COMMENTS_COUNT));
        post.setAuthorId(rs.getLong(AUTHOR_ID));
        post.setAuthorName(rs.getString(AUTHOR_FIRST_NAME));
        post.setAuthorLastName(rs.getString(AUTHOR_LAST_NAME));
        post.setViewsCount(rs.getLong(VIEWS_COUNT));
        Timestamp date = rs.getTimestamp(CREATED_DATE);
        if (date != null) {
            post.setTimeOfCreation(date.toLocalDateTime());
        }
        return post;
    }
}
