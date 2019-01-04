package com.blog.dao.jdbc.mapper;

import com.blog.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.blog.dao.jdbc.TagDaoImpl.*;

@Component
public class TagRowMapper implements RowMapper<Tag> {


    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Tag(
                rs.getLong(ID),
                rs.getString(TITLE),
                rs.getString(PATH_IMAGE)
        );
    }
}
