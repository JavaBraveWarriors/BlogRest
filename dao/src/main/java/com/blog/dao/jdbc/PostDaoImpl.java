package com.blog.dao.jdbc;

import com.blog.Post;
import com.blog.dao.PostDao;
import com.blog.dao.jdbc.mapper.PostRowMapper;
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

@Repository
public class PostDaoImpl implements PostDao {


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


    private PostRowMapper postRowMapper;
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public PostDaoImpl(PostRowMapper postRowMapper, NamedParameterJdbcTemplate jdbcTemplate) {
        this.postRowMapper = postRowMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Post> getAllPosts() throws DataAccessException {
        return jdbcTemplate.query(getAllPostsSql, postRowMapper);
    }

    public Post getPostById(Long id) throws DataAccessException {
        SqlParameterSource parameterSource = new MapSqlParameterSource(ID, id);
        return jdbcTemplate.queryForObject(getPostByIdSql, parameterSource, postRowMapper);
    }

    public List<Post> getAllPostsByAuthorId(Long userId) throws DataAccessException {
        SqlParameterSource parameterSource = new MapSqlParameterSource(AUTHOR_ID, userId);
        return jdbcTemplate.query(getAllPostsByAuthorIdSql, parameterSource, (resultSet, i) -> new PostRowMapper().mapRow(resultSet, i));
    }

    /*TODO: refactor "initial - 1L"*/
    public List<Post> getPostsByInitialIdAndQuantity(Long initial, Long quantity) throws DataAccessException {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(INITIAL, initial - 1L);
        parameterSource.addValue(QUANTITY, quantity);
        return jdbcTemplate.query(getAllPostsByInitialIdAndQuantitySql, parameterSource, (resultSet, i) -> new PostRowMapper().mapRow(resultSet, i));
    }

    public List<Post> getAllPostsByTagId(Long tagId) throws DataAccessException {
        SqlParameterSource parameterSource = new MapSqlParameterSource(TAG_ID, tagId);
        return jdbcTemplate.query(getAllPostsByTagSql, parameterSource, (resultSet, i) -> new PostRowMapper().mapRow(resultSet, i));
    }

    public Long addPost(Post post) throws DataAccessException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameterSource = getParameterSourcePost(post);
        jdbcTemplate.update(addPostSql, parameterSource, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public boolean addTagToPost(Long id, Long tagId) throws DataAccessException {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(ID, id);
        parameterSource.addValue(TAG_ID, tagId);
        return jdbcTemplate.update(addTagToPostSql, parameterSource) == 1;
    }

    public boolean updatePost(Post post) throws DataAccessException {
        MapSqlParameterSource parameterSource = getParameterSourcePost(post);
        return jdbcTemplate.update(updatePostSql, parameterSource) == 1;
    }

    public boolean deletePost(Long id) throws DataAccessException {
        SqlParameterSource parameterSource = new MapSqlParameterSource(ID, id);
        return jdbcTemplate.update(deletePostSql, parameterSource) == 1;
    }

    public boolean deleteTagInPost(Long id, Long tagId) throws DataAccessException {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(ID, id);
        parameterSource.addValue(TAG_ID, tagId);
        return jdbcTemplate.update(deleteTagInPostSql, parameterSource) == 1;
    }

    public boolean checkPostById(Long id) {
        SqlParameterSource parameterSource = new MapSqlParameterSource(ID, id);
        return jdbcTemplate.queryForObject(checkPostByIdSql, parameterSource, boolean.class);
    }

    public boolean checkTagInPostById(Long id, Long tagId) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(ID, id);
        parameterSource.addValue(TAG_ID, tagId);
        return jdbcTemplate.queryForObject(checkTagInPostByIdSql, parameterSource, boolean.class);
    }

    public boolean checkPostByAuthorId(Long userId) {
        SqlParameterSource parameterSource = new MapSqlParameterSource(AUTHOR_ID, userId);
        return jdbcTemplate.queryForObject(checkPostByUserIdSql, parameterSource, boolean.class);
    }

    private MapSqlParameterSource getParameterSourcePost(Post post) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(ID, post.getId());
        parameterSource.addValue(TITLE, post.getTitle());
        parameterSource.addValue(DESCRIPTION, post.getDescription());
        parameterSource.addValue(TEXT, post.getText());
        parameterSource.addValue(CREATED_DATE, post.getDate());
        parameterSource.addValue(PATH_IMAGE, post.getPathImage());
        parameterSource.addValue(AUTHOR_ID, post.getAuthorId());
        return parameterSource;
    }


}
