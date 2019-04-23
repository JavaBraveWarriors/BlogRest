package com.blog.dao.jdbc.mapper;

import com.blog.dto.TagDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The Tag dto row mapper.
 */
@Component
public class TagDtoRowMapper implements RowMapper<TagDto> {
    /**
     * The constant POST_ID.
     */
    public static final String POST_ID = "post_id";

    private TagRowMapper tagRowMapper;

    /**
     * Instantiates a new Tag dto row mapper.
     *
     * @param tagRowMapper the tag row mapper
     */
    @Autowired
    public TagDtoRowMapper(TagRowMapper tagRowMapper) {
        this.tagRowMapper = tagRowMapper;
    }

    public TagDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new TagDto(rs.getLong(POST_ID), tagRowMapper.mapRow(rs, rowNum));
    }
}