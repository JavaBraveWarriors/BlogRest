package com.blog.dao.jdbc.mapper;

import com.blog.dto.TagDto;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TagDtoRowMapper implements RowMapper<TagDto> {
    public static final String POST_ID = "post_id";

    public TagDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new TagDto(rs.getLong(POST_ID), TagRowMapper.rowFields(rs));
    }
}