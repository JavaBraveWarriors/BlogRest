package com.blog.dao.jdbc;

import com.blog.dao.ViewDao;
import com.blog.dao.jdbc.mapper.ViewRowMapper;
import com.blog.model.View;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.blog.dao.jdbc.mapper.ViewRowMapper.*;

/**
 * This interface implementation {ViewDao} allows operations to easily manage a database for an View object.
 * Use this class if you want to access the View database.
 *
 * @author Aliaksandr Yeutushenka
 * @see ViewDao
 * @see ViewRowMapper
 */
@Repository
public class ViewDaoImpl implements ViewDao {

    private static final Logger LOGGER = LogManager.getLogger();

    @Value("${view.selectListViewOfPost}")
    private String getListViewOfPostSql;

    @Value("${view.insertView}")
    private String addViewSql;

    @Value("${view.selectListViewOfUser}")
    private String getListViewOfUserSql;

    @Value("${view.delete}")
    private String deleteViewSql;

    @Value("${view.checkView}")
    private String checkViewByPostIdAndUserIdSql;

    private ViewRowMapper viewRowMapper;

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public ViewDaoImpl(ViewRowMapper viewRowMapper, NamedParameterJdbcTemplate jdbcTemplate) {
        this.viewRowMapper = viewRowMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean addView(View view) {
        LOGGER.debug("Add new view [{}] to database.", view);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameterSource = new MapSqlParameterSource(AUTHOR_ID, view.getUserId());
        parameterSource.addValue(POST_ID, view.getPostId());
        jdbcTemplate.update(addViewSql, parameterSource, keyHolder, new String[]{ID});
        return keyHolder.getKey().longValue() > 0L;
    }

    public boolean deleteView(Long viewId) {
        LOGGER.debug("Delete view by id = [{}] in database.", viewId);
        MapSqlParameterSource parameterSource = new MapSqlParameterSource(ID, viewId);
        return jdbcTemplate.update(deleteViewSql, parameterSource) == 1;
    }

    public List<View> getListViewsOfPost(Long initial, Long size, Long postId) {
        LOGGER.debug("Get list of view with initial = [{}], size = [{}] and post id = [{}] in database.",
                initial, size, postId);
        MapSqlParameterSource parameterSource = getViewParameterSource(initial, size);
        parameterSource.addValue(POST_ID, postId);
        return queryForListViews(getListViewOfPostSql, parameterSource);
    }

    public List<View> getListViewsOfUser(Long initial, Long size, Long userId) {
        LOGGER.debug("Get list of view with initial = [{}], size = [{}] and user id = [{}] in database.",
                initial, size, userId);
        MapSqlParameterSource parameterSource = getViewParameterSource(initial, size);
        parameterSource.addValue(AUTHOR_ID, userId);
        return queryForListViews(getListViewOfUserSql, parameterSource);
    }

    public boolean checkViewByPostIdAndUserId(Long postId, Long userId) {
        LOGGER.debug("Check view post id = [{}] and user id = [{}] in database.", postId, userId);
        MapSqlParameterSource parameterSource = new MapSqlParameterSource(POST_ID, postId);
        parameterSource.addValue(AUTHOR_ID, userId);
        return jdbcTemplate.queryForObject(checkViewByPostIdAndUserIdSql, parameterSource, boolean.class);
    }

    private MapSqlParameterSource getViewParameterSource(Long initial, Long size) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource(INITIAL, initial - 1L);
        parameterSource.addValue(QUANTITY, size);
        return parameterSource;
    }

    private List<View> queryForListViews(String sql, MapSqlParameterSource parameterSource) {
        return jdbcTemplate.query(
                sql,
                parameterSource,
                (resultSet, i) -> viewRowMapper.mapRow(resultSet, i));
    }
}