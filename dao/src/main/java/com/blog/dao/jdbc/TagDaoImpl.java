package com.blog.dao.jdbc;

import com.blog.Tag;
import com.blog.dao.TagDao;
import com.blog.dao.jdbc.mapper.TagRowMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

/**
 * This interface implementation {TagDao} allows operations to easily manage a database for an Tag object.
 * Use this class if you want to access the Tag database.
 *
 * @author Aliaksandr Yeutushenka
 * @see TagDao
 * @see TagRowMapper
 */
@Repository
public class TagDaoImpl implements TagDao {

    /**
     * This field used for logging events
     */
    private static final Logger LOGGER = LogManager.getLogger();

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

    /**
     * Allows to make a mapping for an {Tag} object from database.
     */
    private TagRowMapper tagRowMapper;
    /**
     * The JdbcTemplate which uses named parameters.
     */
    private NamedParameterJdbcTemplate jdbcTemplate;

    /**
     * Create a new TagDaoImpl for the given {@link TagRowMapper} and {@link NamedParameterJdbcTemplate }
     *
     * @param tagRowMapper the tag row mapper
     * @param jdbcTemplate the jdbc template
     */
    @Autowired
    public TagDaoImpl(TagRowMapper tagRowMapper, NamedParameterJdbcTemplate jdbcTemplate) {
        this.tagRowMapper = tagRowMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Tag> getAllTags() {
        LOGGER.debug("Get all tags from database.");
        return jdbcTemplate.query(getAllTagsSql, tagRowMapper);
    }

    public Tag getTagById(Long id) {
        LOGGER.debug("Get tag by id = [{}] from database.", id);
        SqlParameterSource parameterSource = new MapSqlParameterSource(ID, id);
        return jdbcTemplate.queryForObject(getTagByIdSql, parameterSource, tagRowMapper);
    }

    public List<Tag> getAllTagsByPostId(Long id) throws DataAccessException {
        LOGGER.debug("Get all tags by post id = [{}] from database.", id);
        SqlParameterSource parameterSource = new MapSqlParameterSource(ID, id);
        return jdbcTemplate.query(selectAllByPostId, parameterSource, (resultSet, i) -> new TagRowMapper().mapRow(resultSet, i));
    }

    public Long addTag(final Tag tag) throws DataAccessException {
        LOGGER.debug("Add new tag [{}] in database.", tag);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameterSource = getParameterSourceTag(tag);
        jdbcTemplate.update(addTagSql, parameterSource, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public boolean updateTag(final Tag tag) throws DataAccessException {
        LOGGER.debug("Update tag [{}] in database.", tag);

        MapSqlParameterSource parameterSource = getParameterSourceTag(tag);
        return jdbcTemplate.update(updateTagSql, parameterSource) == 1;
    }

    public boolean deleteTag(Long id) throws DataAccessException {
        LOGGER.debug("Delete tag by id = [{}] from database.", id);
        SqlParameterSource parameterSource = new MapSqlParameterSource(ID, id);
        return jdbcTemplate.update(deleteTagSql, parameterSource) == 1;

    }

    public boolean checkTagById(Long id) {
        LOGGER.debug("Check tag by id = [{}] from database.", id);
        SqlParameterSource parameterSource = new MapSqlParameterSource(ID, id);
        return jdbcTemplate.queryForObject(checkTagByIdSql, parameterSource, boolean.class);
    }

    public boolean checkTagByTitle(String title) {
        LOGGER.debug("Check tag by title = [{}] from database.", title);
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
