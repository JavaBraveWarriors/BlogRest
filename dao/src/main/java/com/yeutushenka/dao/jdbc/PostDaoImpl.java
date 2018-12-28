package com.yeutushenka.dao.jdbc;

import com.yeutushenka.Post;
import com.yeutushenka.dao.PostDao;
import com.yeutushenka.dao.jdbc.mapper.PostRowMapper;
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

import static com.yeutushenka.dao.jdbc.mapper.PostRowMapper.*;

@Repository
public class PostDaoImpl implements PostDao {


    @Value("${post.select}")
    String getAllPostsSql;

    @Value("${post.selectById}")
    String getPostByIdSql;

    @Value("${post.selectByUserId}")
    String getAllPostsByAuthorIdSql;

    @Value("${post.selectByTag}")
    String getAllPostsByTagSql;

    @Value("${post.insert}")
    String addPostSql;

    @Value("${post.update}")
    String updatePostSql;

    @Value("${post.delete}")
    String deletePostSql;

    @Value("${post.deleteAll}")
    String deleteAllPostsSql;

    @Value("${post.checkPostByUserId}")
    String checkPostByIdSql;

    @Value("${post.checkPostById}")
    String checkPostByUserIdSql;


    private PostRowMapper postRowMapper;
    private NamedParameterJdbcTemplate jdbcTemplate;


    @Override
    public List<Post> getAllPosts() throws DataAccessException {
        return jdbcTemplate.query(getAllPostsSql, postRowMapper);

    }

    @Override
    public Post getPostById(Long id) throws DataAccessException {
        SqlParameterSource parameterSource = new MapSqlParameterSource("id", id);
        return jdbcTemplate.queryForObject(getPostByIdSql, parameterSource, postRowMapper);
    }

    @Override
    public List<Post> getAllPostsByUserId(Long userId) throws DataAccessException {
        SqlParameterSource parameterSource = new MapSqlParameterSource(AUTHOR_ID, userId);
        return jdbcTemplate.queryForList(getAllPostsByAuthorIdSql, parameterSource, Post.class);
    }

    @Override
    public List<Post> getAllPostsByTagId(Long userId, Long tagId) throws DataAccessException {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource(AUTHOR_ID, userId);
        parameterSource.addValue("tag_id", tagId);
        return jdbcTemplate.queryForList(getAllPostsByAuthorIdSql, parameterSource, Post.class);
    }

    @Override
    public Long addPost(Post post) throws DataAccessException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(ID, post.getId());
        parameterSource.addValue(TITLE, post.getTitle());
        parameterSource.addValue(DESCRIPTION, post.getTitle());
        parameterSource.addValue(TEXT, post.getTitle());
        parameterSource.addValue(CREATED_DATE, post.getTitle());
        parameterSource.addValue(PATH_IMAGE, post.getTitle());
        parameterSource.addValue(AUTHOR_ID, post.getTitle());
        jdbcTemplate.update(addPostSql, parameterSource, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public int updatePost(Post post) throws DataAccessException {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(ID, post.getId());
        parameterSource.addValue(TITLE, post.getTitle());
        parameterSource.addValue(DESCRIPTION, post.getTitle());
        parameterSource.addValue(TEXT, post.getTitle());
        parameterSource.addValue(CREATED_DATE, post.getTitle());
        parameterSource.addValue(PATH_IMAGE, post.getTitle());
        parameterSource.addValue(AUTHOR_ID, post.getTitle());
        return jdbcTemplate.update(updatePostSql, parameterSource);

    }

    @Override
    public int deletePost(Long id) throws DataAccessException {
        SqlParameterSource parameterSource = new MapSqlParameterSource(ID, id);
        return jdbcTemplate.update(deletePostSql, parameterSource);
    }

    @Override
    public boolean checkPostById(Long id) {
        SqlParameterSource parameterSource = new MapSqlParameterSource(ID, id);
        return jdbcTemplate.queryForObject(checkPostByIdSql, parameterSource, boolean.class);
    }

    @Override
    public boolean checkPostByUserId(Long userId) {
        SqlParameterSource parameterSource = new MapSqlParameterSource(AUTHOR_ID, userId);
        return jdbcTemplate.queryForObject(checkPostByUserIdSql, parameterSource, boolean.class);
    }


    @Autowired
    public void setTagRowMapper(PostRowMapper postRowMapper) {
        this.postRowMapper = postRowMapper;
    }

    @Autowired
    public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
