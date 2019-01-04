package com.blog.dao.jdbc;

import com.blog.Tag;
import com.blog.dao.TagDao;
import com.blog.dao.jdbc.mapper.TagRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TagDaoImpl implements TagDao {

    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String PATH_IMAGE = "path_image";

    @Value("${tag.select}")
    String getAllTagsSql;

    @Value("${tag.insert}")
    String addTagSql;

    @Value("${tag.update}")
    String updateTagSql;

    @Value("${tag.delete}")
    String deleteTagSql;

    @Value("${tag.selectById}")
    String getTagByIdSql;

    @Value("${tag.checkTagById}")
    String checkTagByIdSql;

    @Value("${tag.checkTagByTitle}")
    String checkTagByTitleSql;

    private TagRowMapper tagRowMapper;
    private NamedParameterJdbcTemplate jdbcTemplate;

    public TagDaoImpl() {
    }

    public List<Tag> getAllTags() {
        return jdbcTemplate.query(getAllTagsSql, tagRowMapper);
    }

    public Tag getTagById(Long id) {
        SqlParameterSource parameterSource = new MapSqlParameterSource(ID, id);
        return jdbcTemplate.queryForObject(getTagByIdSql, parameterSource, tagRowMapper);
    }

    @Override
    public Long addTag(Tag tag) throws DataAccessException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameterSource = getParameterSourceTag(tag);

        jdbcTemplate.update(addTagSql, parameterSource, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public int updateTag(Tag tag) throws DataAccessException {
        MapSqlParameterSource parameterSource = getParameterSourceTag(tag);
        return jdbcTemplate.update(updateTagSql, parameterSource);
    }

    @Override
    public int deleteTag(Long id) throws DataAccessException {
        SqlParameterSource parameterSource = new MapSqlParameterSource(ID, id);
        return jdbcTemplate.update(deleteTagSql, parameterSource);

    }

    public boolean checkTagById(Long id) {
        SqlParameterSource parameterSource = new MapSqlParameterSource(ID, id);
        return jdbcTemplate.queryForObject(checkTagByIdSql, parameterSource, boolean.class);
    }

    @Override
    public boolean checkTagByTitle(String title) {
        SqlParameterSource parameterSource = new MapSqlParameterSource(TITLE, title);
        return jdbcTemplate.queryForObject(checkTagByTitleSql, parameterSource, boolean.class);
    }

    private MapSqlParameterSource getParameterSourceTag(Tag tag) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(ID, tag.getId());
        parameterSource.addValue(TITLE, tag.getTitle());
        parameterSource.addValue(PATH_IMAGE, tag.getPathImage());
        return parameterSource;
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
