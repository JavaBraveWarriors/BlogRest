package com.blog.dao.jdbc;

import com.blog.dao.jdbc.mapper.PostRowMapper;
import com.blog.Post;
import com.blog.ResponseStatus;
import com.blog.dao.PostDao;
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
public class PostDaoImpl implements PostDao {


    @Value("${post.select}")
    String getAllPostsSql;

    @Value("${post.selectById}")
    String getPostByIdSql;

    @Value("${post.selectByUserId}")
    String getAllPostsByAuthorIdSql;

    @Value("${post.selectByInitialIdAndQuantity}")
    String getAllPostsByInitialIdAndQuantitySql;

    @Value("${post.selectByTag}")
    String getAllPostsByTagSql;

    @Value("${post.insert}")
    String addPostSql;

    @Value("${post.update}")
    String updatePostSql;

    @Value("${post.delete}")
    String deletePostSql;


    @Value("${post.checkPostByUserId}")
    String checkPostByIdSql;

    @Value("${post.checkPostById}")
    String checkPostByUserIdSql;


    private PostRowMapper postRowMapper;
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public void setTagRowMapper(PostRowMapper postRowMapper) {
        this.postRowMapper = postRowMapper;
    }

    @Autowired
    public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


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
    public List<Post> getAllPostsByAuthorId(Long userId) throws DataAccessException {
        SqlParameterSource parameterSource = new MapSqlParameterSource(PostRowMapper.AUTHOR_ID, userId);
        return jdbcTemplate.queryForList(getAllPostsByAuthorIdSql, parameterSource, Post.class);
    }

    @Override
    public List<Post> getPostsByInitialIdAndQuantity(Long initial, Long quantity) throws DataAccessException {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("initial", initial);
        parameterSource.addValue("quantity", quantity);
        return jdbcTemplate.queryForList(getAllPostsByInitialIdAndQuantitySql, parameterSource, Post.class);

    }


    @Override
    public List<Post> getAllPostsByAuthorIdAndTagId(Long userId, Long tagId) throws DataAccessException {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource(PostRowMapper.AUTHOR_ID, userId);
        parameterSource.addValue("tag_id", tagId);
        return jdbcTemplate.queryForList(getAllPostsByAuthorIdSql, parameterSource, Post.class);
    }

    @Override
    public Long addPost(Post post) throws DataAccessException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameterSource = buildParamSourse(post);
        jdbcTemplate.update(addPostSql, parameterSource, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public ResponseStatus updatePost(Post post) throws DataAccessException {
        MapSqlParameterSource parameterSource = buildParamSourse(post);
        if (jdbcTemplate.update(updatePostSql, parameterSource) == 0)
            return ResponseStatus.ERROR;
        else
            return ResponseStatus.SUCCESS;

    }

    @Override
    public ResponseStatus deletePost(Long id) throws DataAccessException {
        SqlParameterSource parameterSource = new MapSqlParameterSource(PostRowMapper.ID, id);
        if (jdbcTemplate.update(deletePostSql, parameterSource) == 0)
            return ResponseStatus.ERROR;
        else
            return ResponseStatus.SUCCESS;

    }

    @Override
    public boolean checkPostById(Long id) {
        SqlParameterSource parameterSource = new MapSqlParameterSource(PostRowMapper.ID, id);
        return jdbcTemplate.queryForObject(checkPostByIdSql, parameterSource, boolean.class);
    }

    @Override
    public boolean checkPostByAuthorId(Long userId) {
        SqlParameterSource parameterSource = new MapSqlParameterSource(PostRowMapper.AUTHOR_ID, userId);
        return jdbcTemplate.queryForObject(checkPostByUserIdSql, parameterSource, boolean.class);
    }


    private MapSqlParameterSource buildParamSourse(Post post) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(PostRowMapper.ID, post.getId());
        parameterSource.addValue(PostRowMapper.TITLE, post.getTitle());
        parameterSource.addValue(PostRowMapper.DESCRIPTION, post.getTitle());
        parameterSource.addValue(PostRowMapper.TEXT, post.getTitle());
        parameterSource.addValue(PostRowMapper.CREATED_DATE, post.getTitle());
        parameterSource.addValue(PostRowMapper.PATH_IMAGE, post.getTitle());
        parameterSource.addValue(PostRowMapper.AUTHOR_ID, post.getTitle());
        return parameterSource;
    }

}
