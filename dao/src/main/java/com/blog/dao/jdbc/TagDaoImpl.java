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

import static com.blog.dao.jdbc.mapper.TagRowMapper.*;

@Repository
public class TagDaoImpl implements TagDao {


    @Value("${tag.select}")
    private String getAllTagsSql;

    @Value("${tag.selectAllByPostId}")
    private String selectAllByPostId;

    @Value("${tag.insert}")
    private String addTagSql;

    @Value("${tag.update}")
    private String updateTagSql;

    @Value("${tag.delete}")
    private String deleteTagSql;

    @Value("${tag.selectById}")
    private String getTagByIdSql;

    @Value("${tag.checkTagById}")
    private String checkTagByIdSql;

    @Value("${tag.checkTagByTitle}")
    private String checkTagByTitleSql;

    private TagRowMapper tagRowMapper;
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public TagDaoImpl(TagRowMapper tagRowMapper, NamedParameterJdbcTemplate jdbcTemplate) {
        this.tagRowMapper = tagRowMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Tag> getAllTags() {
        return jdbcTemplate.query(getAllTagsSql, tagRowMapper);
    }

    public Tag getTagById(Long id) {
        SqlParameterSource parameterSource = new MapSqlParameterSource(ID, id);
        return jdbcTemplate.queryForObject(getTagByIdSql, parameterSource, tagRowMapper);
    }

    public List<Tag> getAllTagsByPostId(Long id) throws DataAccessException {
        SqlParameterSource parameterSource = new MapSqlParameterSource(ID, id);
        return jdbcTemplate.query(selectAllByPostId, parameterSource, (resultSet, i) -> new TagRowMapper().mapRow(resultSet, i));
    }

    public Long addTag(Tag tag) throws DataAccessException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameterSource = getParameterSourceTag(tag);

        jdbcTemplate.update(addTagSql, parameterSource, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public boolean updateTag(Tag tag) throws DataAccessException {
        MapSqlParameterSource parameterSource = getParameterSourceTag(tag);
        return jdbcTemplate.update(updateTagSql, parameterSource) == 1;
    }

    public boolean deleteTag(Long id) throws DataAccessException {
        SqlParameterSource parameterSource = new MapSqlParameterSource(ID, id);
        return jdbcTemplate.update(deleteTagSql, parameterSource) == 1;

    }

    public boolean checkTagById(Long id) {
        SqlParameterSource parameterSource = new MapSqlParameterSource(ID, id);
        return jdbcTemplate.queryForObject(checkTagByIdSql, parameterSource, boolean.class);
    }

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

}
