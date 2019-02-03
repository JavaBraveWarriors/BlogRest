package com.blog.dao.jdbc;

import com.blog.Post;
import com.blog.dao.PostDao;
import com.blog.dao.jdbc.mapper.PostRowMapper;
import com.blog.dao.jdbc.mapper.PostShortRowMapper;
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

import static com.blog.dao.jdbc.mapper.PostRowMapper.*;

/**
 * This interface implementation {PostDao} allows operations to easily manage a database for an Post object.
 * Use this class if you want to access the Post database.
 *
 * @author Aliaksandr Yeutushenka
 * @see PostDao
 * @see PostRowMapper
 * @see Post
 */
@Repository
public class PostDaoImpl implements PostDao {
    private static final Logger LOGGER = LogManager.getLogger(PostDaoImpl.class);

    @Value("${post.select}")
    private String getAllPostsSql;

    @Value("${post.selectById}")
    private String getPostByIdSql;

    @Value("${post.selectByUserId}")
    private String getAllPostsByAuthorIdSql;

    @Value("${post.selectByInitialIdAndQuantity}")
    private String getAllPostsByInitialIdAndQuantitySql;

    @Value("${post.selectByTag}")
    private String getAllPostsByTagSql;

    @Value("${post.insert}")
    private String addPostSql;

    @Value("${post.insertTagToPost}")
    private String addTagToPostSql;

    @Value("${post.update}")
    private String updatePostSql;

    @Value("${post.delete}")
    private String deletePostSql;

    @Value("${post.deleteTag}")
    private String deleteTagInPostSql;

    @Value("${post.checkPostById}")
    private String checkPostByIdSql;

    @Value("${post.checkTagInPostById}")
    private String checkTagInPostByIdSql;

    @Value("${post.checkPostByUserId}")
    private String checkPostByUserIdSql;

    @Value("${post.getCountWithPages}")
    private String getCountOfPages;

    /**
     * Allows to make a mapping for an {Post} object from database.
     */
    private PostRowMapper postRowMapper;

    /**
     * The JdbcTemplate which uses named parameters.
     */
    private NamedParameterJdbcTemplate jdbcTemplate;

    /**
     * Create a new PostDaoImpl for the given {@link PostRowMapper} and {@link NamedParameterJdbcTemplate }
     *
     * @param postRowMapper the post row mapper
     * @param jdbcTemplate  the jdbc template
     */
    @Autowired
    public PostDaoImpl(PostRowMapper postRowMapper, NamedParameterJdbcTemplate jdbcTemplate) {
        this.postRowMapper = postRowMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Post> getAllPosts() throws DataAccessException {
        LOGGER.debug("Get all posts from database.");
        return jdbcTemplate.query(getAllPostsSql, postRowMapper);
    }

    public Post getPostById(Long id) throws DataAccessException {
        LOGGER.debug("Get post by id = [{}] from database.", id);
        SqlParameterSource parameterSource = new MapSqlParameterSource(ID, id);
        return jdbcTemplate.queryForObject(getPostByIdSql, parameterSource, postRowMapper);
    }

    public List<Post> getAllPostsByAuthorId(Long authorId) throws DataAccessException {
        LOGGER.debug("Get list of posts by author id = [{}] from database.", authorId);
        SqlParameterSource parameterSource = new MapSqlParameterSource(AUTHOR_ID, authorId);
        return jdbcTemplate.query(getAllPostsByAuthorIdSql, parameterSource, (resultSet, i) -> new PostShortRowMapper().mapRow(resultSet, i));
    }

    public List<Post> getPostsByInitialIdAndQuantity(Long initial, Long quantity) throws DataAccessException {
        LOGGER.debug("Get list of posts by initial = [{}] and quantity = [{}] from database.", initial, quantity);
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(INITIAL, initial - 1L);
        parameterSource.addValue(QUANTITY, quantity);
        return jdbcTemplate.query(getAllPostsByInitialIdAndQuantitySql, parameterSource, (resultSet, i) -> new PostShortRowMapper().mapRow(resultSet, i));
    }

    public List<Post> getAllPostsByTagId(Long tagId) throws DataAccessException {
        LOGGER.debug("Get list of posts by tag id = [{}] from database.", tagId);
        SqlParameterSource parameterSource = new MapSqlParameterSource(TAG_ID, tagId);
        return jdbcTemplate.query(getAllPostsByTagSql, parameterSource, (resultSet, i) -> new PostShortRowMapper().mapRow(resultSet, i));
    }

    public Long addPost(final Post post) throws DataAccessException {
        LOGGER.debug("Add new post [{}] in database.", post);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameterSource = getParameterSourcePost(post);
        jdbcTemplate.update(addPostSql, parameterSource, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public boolean addTagToPost(Long id, Long tagId) throws DataAccessException {
        LOGGER.debug("Add tag id = [{}] to post id = [{}] in database.", tagId, id);
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(ID, id);
        parameterSource.addValue(TAG_ID, tagId);
        return jdbcTemplate.update(addTagToPostSql, parameterSource) == 1;
    }

    public boolean updatePost(final Post post) throws DataAccessException {
        LOGGER.debug("Update post [{}] in database.", post);
        MapSqlParameterSource parameterSource = getParameterSourcePost(post);
        return jdbcTemplate.update(updatePostSql, parameterSource) == 1;
    }

    public boolean deletePost(Long id) throws DataAccessException {
        LOGGER.debug("Delete post id = [{}] from database.", id);
        SqlParameterSource parameterSource = new MapSqlParameterSource(ID, id);
        return jdbcTemplate.update(deletePostSql, parameterSource) == 1;
    }

    public boolean deleteTagInPost(Long id, Long tagId) throws DataAccessException {
        LOGGER.debug("Delete tag id = [{}] from post id = [{}] in database.", tagId, id);
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(ID, id);
        parameterSource.addValue(TAG_ID, tagId);
        return jdbcTemplate.update(deleteTagInPostSql, parameterSource) == 1;
    }

    public boolean checkPostById(Long id) {
        LOGGER.debug("Check post by id = [{}]  in database.", id);
        SqlParameterSource parameterSource = new MapSqlParameterSource(ID, id);
        return jdbcTemplate.queryForObject(checkPostByIdSql, parameterSource, boolean.class);
    }

    public boolean checkTagInPostById(Long id, Long tagId) {
        LOGGER.debug("Check tag by id = [{}] in post id = [{}] from database.", tagId, id);
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(ID, id);
        parameterSource.addValue(TAG_ID, tagId);
        return jdbcTemplate.queryForObject(checkTagInPostByIdSql, parameterSource, boolean.class);
    }

    public boolean checkPostByAuthorId(Long authorId) {
        LOGGER.debug("Check post by author id = [{}]  in database.", authorId);
        SqlParameterSource parameterSource = new MapSqlParameterSource(AUTHOR_ID, authorId);
        return jdbcTemplate.queryForObject(checkPostByUserIdSql, parameterSource, boolean.class);
    }

    public Long getCountOfPosts() {
        LOGGER.debug("Get count posts in database.");
        SqlParameterSource parameterSource = new MapSqlParameterSource();
        return jdbcTemplate.queryForObject(getCountOfPages, parameterSource, Long.class);
    }

    private MapSqlParameterSource getParameterSourcePost(Post post) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(ID, post.getId());
        parameterSource.addValue(TITLE, post.getTitle());
        parameterSource.addValue(DESCRIPTION, post.getDescription());
        parameterSource.addValue(TEXT, post.getText());
        parameterSource.addValue(CREATED_DATE, post.getTimeOfCreation());
        parameterSource.addValue(PATH_IMAGE, post.getPathImage());
        parameterSource.addValue(AUTHOR_ID, post.getAuthorId());
        return parameterSource;
    }
}
