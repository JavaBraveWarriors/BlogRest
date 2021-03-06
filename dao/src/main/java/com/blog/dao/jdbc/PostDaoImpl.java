package com.blog.dao.jdbc;

import com.blog.dao.PostDao;
import com.blog.dao.jdbc.mapper.PostRowMapper;
import com.blog.dao.jdbc.mapper.PostShortRowMapper;
import com.blog.model.Post;
import com.blog.model.ResponsePostDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.blog.dao.jdbc.mapper.PostRowMapper.*;
import static com.blog.dao.jdbc.mapper.ViewRowMapper.POST_ID;

/**
 * This interface implementation {PostDao} allows operations to easily manage a database for an Post object.
 * Use this class if you want to access the Post database.
 *
 * @author Aliaksandr Yeutushenka
 * @see PostDao
 * @see PostRowMapper
 * @see PostShortRowMapper
 * @see Post
 */
@Repository
public class PostDaoImpl implements PostDao {
    private static final Logger LOGGER = LogManager.getLogger();

    @Value("${post.selectById}")
    private String getPostByIdSql;

    @Value("${post.selectByUserId}")
    private String getAllPostsByAuthorIdSql;

    @Value("${post.selectByInitialIdAndQuantity}")
    private String getAllPostsByInitialIdAndQuantitySql;

    @Value("${post.selectByInitialIdQuantityAndSort}")
    private String getAllPostsByInitialIdQuantityAndSortSql;

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

    @Value("${post.addComment}")
    private String addCommentSql;

    @Value("${post.deleteComment}")
    private String deleteCommentSql;

    @Value("${post.addView}")
    private String addViewSql;

    @Value("${post.addTagsInPost}")
    private String addTagsToPostSql;

    @Value("${post.deleteAllTags}")
    private String deleteAllTagsInPostSql;

    private PostRowMapper postRowMapper;
    private PostShortRowMapper postShortRowMapper;

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
    public PostDaoImpl(PostRowMapper postRowMapper, NamedParameterJdbcTemplate jdbcTemplate, PostShortRowMapper postShortRowMapper) {
        this.postRowMapper = postRowMapper;
        this.jdbcTemplate = jdbcTemplate;
        this.postShortRowMapper = postShortRowMapper;
    }

    public ResponsePostDto getPostById(final Long id) {
        LOGGER.debug("Get post by id = [{}] from database.", id);
        SqlParameterSource parameterSource = new MapSqlParameterSource(ID, id);
        return jdbcTemplate.queryForObject(getPostByIdSql, parameterSource, postRowMapper);
    }

    public List<ResponsePostDto> getAllPostsByAuthorId(final Long authorId) {
        LOGGER.debug("Get list of posts by author id = [{}] from database.", authorId);
        SqlParameterSource parameterSource = new MapSqlParameterSource(AUTHOR_ID, authorId);
        return jdbcTemplate.query(getAllPostsByAuthorIdSql, parameterSource, (resultSet, i) -> postShortRowMapper.mapRow(resultSet, i));
    }

    public List<ResponsePostDto> getPostsByInitialIdAndQuantity(final Long initial, final Long quantity) {
        LOGGER.debug("Get list of posts by initial = [{}] and quantity = [{}] from database.", initial, quantity);
        MapSqlParameterSource parameterSource = getParameterSourceForInitialAndQuantity(initial, quantity);
        return jdbcTemplate.query(getAllPostsByInitialIdAndQuantitySql, parameterSource, (resultSet, i) -> postShortRowMapper.mapRow(resultSet, i));
    }

    public List<ResponsePostDto> getPostsByInitialIdAndQuantity(final Long initial, final Long quantity, final String sort) {
        LOGGER.debug("Get list of posts by initial = [{}], quantity = [{}] and sort = [{}] from database.", initial, quantity, sort);
        MapSqlParameterSource parameterSource = getParameterSourceForInitialAndQuantity(initial, quantity);
        return jdbcTemplate.query(String.format(getAllPostsByInitialIdQuantityAndSortSql, sort), parameterSource, (resultSet, i) -> postShortRowMapper.mapRow(resultSet, i));
    }

    public List<ResponsePostDto> getAllPostsByTagId(final Long tagId) {
        LOGGER.debug("Get list of posts by tag id = [{}] from database.", tagId);
        SqlParameterSource parameterSource = new MapSqlParameterSource(TAG_ID, tagId);
        return jdbcTemplate.query(getAllPostsByTagSql, parameterSource, (resultSet, i) -> postShortRowMapper.mapRow(resultSet, i));
    }

    public Long addPost(final Post post) {
        LOGGER.debug("Add new post [{}] in database.", post);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameterSource = getParameterSourcePost(post);
        jdbcTemplate.update(addPostSql, parameterSource, keyHolder, new String[]{ID});
        return keyHolder.getKey().longValue();
    }

    public boolean addTagToPost(final Long id, final Long tagId) {
        LOGGER.debug("Add tag id = [{}] to post id = [{}] in database.", tagId, id);
        MapSqlParameterSource parameterSource = new MapSqlParameterSource(ID, id);
        parameterSource.addValue(TAG_ID, tagId);
        return jdbcTemplate.update(addTagToPostSql, parameterSource) == 1;
    }

    public boolean updatePost(final Post post) {
        LOGGER.debug("Update post [{}] in database.", post);
        MapSqlParameterSource parameterSource = getParameterSourcePost(post);
        return jdbcTemplate.update(updatePostSql, parameterSource) == 1;
    }

    public boolean deletePost(final Long id) {
        LOGGER.debug("Delete post id = [{}] from database.", id);
        SqlParameterSource parameterSource = new MapSqlParameterSource(ID, id);
        return jdbcTemplate.update(deletePostSql, parameterSource) == 1;
    }

    public boolean deleteTagInPost(final Long id, final Long tagId) {
        LOGGER.debug("Delete tag id = [{}] from post id = [{}] in database.", tagId, id);
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(ID, id);
        parameterSource.addValue(TAG_ID, tagId);
        return jdbcTemplate.update(deleteTagInPostSql, parameterSource) == 1;
    }

    public boolean checkPostById(final Long id) {
        LOGGER.debug("Check post by id = [{}]  in database.", id);
        SqlParameterSource parameterSource = new MapSqlParameterSource(ID, id);
        return jdbcTemplate.queryForObject(checkPostByIdSql, parameterSource, boolean.class);
    }

    public boolean checkTagInPostById(final Long id, final Long tagId) {
        LOGGER.debug("Check tag by id = [{}] in post id = [{}] from database.", tagId, id);
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(ID, id);
        parameterSource.addValue(TAG_ID, tagId);
        return jdbcTemplate.queryForObject(checkTagInPostByIdSql, parameterSource, boolean.class);
    }

    public boolean checkPostByAuthorId(final Long authorId) {
        LOGGER.debug("Check post by author id = [{}]  in database.", authorId);
        SqlParameterSource parameterSource = new MapSqlParameterSource(AUTHOR_ID, authorId);
        return jdbcTemplate.queryForObject(checkPostByUserIdSql, parameterSource, boolean.class);
    }

    public Long getCountOfPosts() {
        LOGGER.debug("Get count posts in database.");
        SqlParameterSource parameterSource = new MapSqlParameterSource();
        return jdbcTemplate.queryForObject(getCountOfPages, parameterSource, Long.class);
    }

    public boolean addComment(final Long postId) {
        LOGGER.debug("Add comment to post id = [{}] in database.", postId);
        SqlParameterSource parameterSource = new MapSqlParameterSource(ID, postId);
        return jdbcTemplate.update(addCommentSql, parameterSource) == 1;
    }

    public boolean deleteComment(final Long postId) {
        LOGGER.debug("Delete comment in post id = [{}] in database.", postId);
        SqlParameterSource parameterSource = new MapSqlParameterSource(ID, postId);
        return jdbcTemplate.update(deleteCommentSql, parameterSource) == 1;
    }

    public boolean addViewToPost(final Long postId) {
        LOGGER.debug("Add view to post id = [{}] in database.", postId);
        MapSqlParameterSource parameterSource = new MapSqlParameterSource(ID, postId);
        return jdbcTemplate.update(addViewSql, parameterSource) == 1;
    }

    public boolean deleteAllTags(final Long postId) {
        LOGGER.debug("Delete all tags from post id = [{}] in database.", postId);
        MapSqlParameterSource parameterSource = new MapSqlParameterSource(ID, postId);
        return jdbcTemplate.update(deleteAllTagsInPostSql, parameterSource) != 0;
    }

    public boolean addTags(final Long postId, final List<Long> tags) {
        LOGGER.debug("Add tags to post by id = [{}], tags = [{}]", postId, tags);
        List<Map<String, Object>> batchValues = new ArrayList<>(tags.size());
        tags.forEach(tagId -> batchValues.add(
                new MapSqlParameterSource(POST_ID, postId)
                        .addValue(TAG_ID, tagId)
                        .getValues()));

        return jdbcTemplate.batchUpdate(addTagsToPostSql, batchValues.toArray(new Map[tags.size()])) != null;
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

    private MapSqlParameterSource getParameterSourceForInitialAndQuantity(Long initial, Long quantity) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(INITIAL, initial - 1L);
        parameterSource.addValue(QUANTITY, quantity);
        return parameterSource;
    }
}