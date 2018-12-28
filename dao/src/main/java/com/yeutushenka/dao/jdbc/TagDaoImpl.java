package com.yeutushenka.dao.jdbc;

import com.yeutushenka.Tag;
import com.yeutushenka.dao.TagDao;
import com.yeutushenka.dao.jdbc.mapper.TagRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TagDaoImpl implements TagDao {


    @Value("${tag.select}")
    String getAllTagsSql;

    @Value("${tag.selectById}")
    String getTagByIdSql;

    @Value("${tag.checkTagById}")
    String checkTagByIdSql;

    private TagRowMapper tagRowMapper;
    private NamedParameterJdbcTemplate jdbcTemplate;

    public TagDaoImpl() {
    }

    public List<Tag> getAllTags() {
        return jdbcTemplate.query(getAllTagsSql, tagRowMapper);
    }

    public Tag getTagById(Long id) {
        SqlParameterSource parameterSource = new MapSqlParameterSource("id", id);
        return jdbcTemplate.queryForObject(getTagByIdSql, parameterSource, tagRowMapper);
    }

    public boolean checkTagById(Long id) {
        SqlParameterSource parameterSource = new MapSqlParameterSource("id", id);
        return jdbcTemplate.queryForObject(checkTagByIdSql, parameterSource, boolean.class);
    }

    @Autowired
    public void setTagRowMapper(TagRowMapper tagRowMapper) {
        this.tagRowMapper = tagRowMapper;
    }

    @Autowired
    public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
