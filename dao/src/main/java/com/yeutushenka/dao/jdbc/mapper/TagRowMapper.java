package com.yeutushenka.dao.jdbc.mapper;

import com.yeutushenka.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TagRowMapper implements RowMapper<Tag> {
    private static final String ID = "id";
    private static final String TAG = "tag";
    private static final String PATH_IMAGE = "path_image";

    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Tag(
                rs.getLong(ID),
                rs.getString(TAG),
                rs.getString(PATH_IMAGE)
        );
    }
}
