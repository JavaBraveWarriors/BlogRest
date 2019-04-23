package com.blog.dao.jdbc;

import com.blog.dao.CommentDao;
import com.blog.dao.jdbc.mapper.CommentRowMapper;
import com.blog.model.Comment;
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

import java.util.List;

import static com.blog.dao.jdbc.mapper.CommentRowMapper.*;

/**
 * This interface implementation {CommentDao} allows operations to easily manage a database for an Comment object.
 * Use this class if you want to access the Comment database.
 *
 * @author Aliaksandr Yeutushenka
 * @see CommentDao
 * @see CommentRowMapper
 * @see Comment
 */
@Repository
public class CommentDaoImpl implements CommentDao {
    private static final Logger LOGGER = LogManager.getLogger();

    @Value("${comment.insert}")
    private String addCommentSql;

    @Value("${comment.checkCommentInPost}")
    private String checkCommentInPostSql;

    @Value("${comment.checkComment}")
    private String checkCommentSql;

    @Value("${comment.delete}")
    private String deleteCommentSql;

    @Value("${comment.getCountInPost}")
    private String getCountOfCommentsSql;

    @Value("${comment.update}")
    private String updateCommentSql;

    @Value("${comment.selectByInitialIdAndQuantity}")
    private String selectByInitialIdAndQuantityCommentSql;

    @Value("${comment.selectById}")
    private String selectByIdSql;

    @Value("${comment.getAuthorIdByCommentId}")
    private String getAuthorIdByCommentIdSql;

    private CommentRowMapper commentRowMapper;
    /**
     * The JdbcTemplate which uses named parameters.
     */
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public CommentDaoImpl(CommentRowMapper commentRowMapper, NamedParameterJdbcTemplate jdbcTemplate) {
        this.commentRowMapper = commentRowMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    public Comment getCommentById(final Long commentId) {
        LOGGER.debug("Get comment by commentId = [{}] from database.", commentId);
        MapSqlParameterSource parameterSource = new MapSqlParameterSource(ID, commentId);
        return jdbcTemplate.queryForObject(selectByIdSql, parameterSource, commentRowMapper);
    }

    public List<Comment> getListCommentsByInitialAndSize(final Long initial, final Long size, final Long postId) {
        LOGGER.debug("Get list of comments by initial = [{}], quantity = [{}] and postId = [{}] from database.",
                initial, size, postId);
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(POST_ID, postId);
        parameterSource.addValue(INITIAL, initial - 1L);
        parameterSource.addValue(QUANTITY, size);
        return jdbcTemplate.query(
                selectByInitialIdAndQuantityCommentSql,
                parameterSource,
                (resultSet, i) -> commentRowMapper.mapRow(resultSet, i));
    }

    public Long addComment(final Comment comment) {
        LOGGER.debug("Add new comment = [{}] to database.", comment);
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(CREATED_DATE, comment.getTimeOfCreation());
        parameterSource.addValue(AUTHOR_ID, comment.getAuthorId());
        parameterSource.addValue(POST_ID, comment.getPostId());
        parameterSource.addValue(TEXT, comment.getText());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(addCommentSql, parameterSource, keyHolder, new String[]{ID});
        return keyHolder.getKey().longValue();
    }

    public Long getCountOfCommentsByPostId(final Long postId) {
        LOGGER.debug("Get count of comments by postId = [{}] to database.", postId);
        MapSqlParameterSource parameterSource = new MapSqlParameterSource(POST_ID, postId);
        return jdbcTemplate.queryForObject(getCountOfCommentsSql, parameterSource, Long.class);
    }

    public boolean updateComment(final Comment comment) {
        LOGGER.debug("Update comment [{}] in database.", comment);
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(ID, comment.getId());
        parameterSource.addValue(TEXT, comment.getText());
        return jdbcTemplate.update(updateCommentSql, parameterSource) == 1;
    }

    public boolean deleteComment(final Long commentId) {
        LOGGER.debug("Delete comment id = [{}] from database.", commentId);
        SqlParameterSource parameterSource = new MapSqlParameterSource(ID, commentId);
        return jdbcTemplate.update(deleteCommentSql, parameterSource) == 1;
    }

    public boolean checkCommentInPostById(final Long commentId, final Long postId) {
        LOGGER.debug("Check comment by id = [{}] in post id = [{}] from database.", commentId, postId);
        MapSqlParameterSource parameterSource = new MapSqlParameterSource(ID, commentId);
        parameterSource.addValue(POST_ID, postId);
        return jdbcTemplate.queryForObject(checkCommentInPostSql, parameterSource, boolean.class);
    }

    public boolean checkCommentById(final Long commentId) {
        LOGGER.debug("Check comment by id = [{}] in database.", commentId);
        MapSqlParameterSource parameterSource = new MapSqlParameterSource(ID, commentId);
        return jdbcTemplate.queryForObject(checkCommentSql, parameterSource, boolean.class);
    }

    public Long getAuthorIdByCommentId(final Long commentId) {
        LOGGER.debug("Get authorId by commentId = [{}] from database.", commentId);
        MapSqlParameterSource parameterSource = new MapSqlParameterSource(ID, commentId);
        return jdbcTemplate.queryForObject(getAuthorIdByCommentIdSql, parameterSource, Long.class);
    }
}