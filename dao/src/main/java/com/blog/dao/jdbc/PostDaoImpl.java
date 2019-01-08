package com.blog.dao.jdbc;

import com.blog.Post;
import com.blog.Tag;
import com.blog.dao.PostDao;
import com.blog.dao.jdbc.mapper.PostRowMapper;
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
public class PostDaoImpl implements PostDao {

    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String TEXT = "text";
    public static final String CREATED_DATE = "created_date";
    public static final String PATH_IMAGE = "path_image";
    public static final String AUTHOR_ID = "author_id";
    private static final String INITIAL = "initial";
    private static final String QUANTITY = "quantity";

    private static final String TAG_ID = "tag_id";

    @Value("${post.select}")
    String getAllPostsSql;

    @Value("${tag.selectAllByPostId}")
    String selectAllByPostId;

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

    @Value("${post.insertTagToPost}")
    String addTagToPostSql;

    @Value("${post.update}")
    String updatePostSql;

    @Value("${post.delete}")
    String deletePostSql;

    @Value("${post.deleteTag}")
    String deleteTagInPostSql;


    @Value("${post.checkPostById}")
    String checkPostByIdSql;

    @Value("${post.checkTagInPostById}")
    String checkTagInPostByIdSql;

    @Value("${post.checkPostByUserId}")
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
    public List<Tag> getAllTagsByPostId(Long id) throws DataAccessException {
        SqlParameterSource parameterSource = new MapSqlParameterSource(ID, id);
        return jdbcTemplate.query(selectAllByPostId, parameterSource, (resultSet, i) -> new TagRowMapper().mapRow(resultSet, i));
    }

    @Override
    public Post getPostById(Long id) throws DataAccessException {
        SqlParameterSource parameterSource = new MapSqlParameterSource(ID, id);
        return jdbcTemplate.queryForObject(getPostByIdSql, parameterSource, postRowMapper);
    }

    @Override
    public List<Post> getAllPostsByAuthorId(Long userId) throws DataAccessException {
        SqlParameterSource parameterSource = new MapSqlParameterSource(AUTHOR_ID, userId);
        return jdbcTemplate.query(getAllPostsByAuthorIdSql, parameterSource, (resultSet, i) -> new PostRowMapper().mapRow(resultSet, i));
    }

    /*TODO: refactor "initial - 1L"*/
    @Override
    public List<Post> getPostsByInitialIdAndQuantity(Long initial, Long quantity) throws DataAccessException {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(INITIAL, initial - 1L);
        parameterSource.addValue(QUANTITY, quantity);
        return jdbcTemplate.query(getAllPostsByInitialIdAndQuantitySql, parameterSource, (resultSet, i) -> new PostRowMapper().mapRow(resultSet, i));
    }

    @Override
    public List<Post> getAllPostsByTagId(Long tagId) throws DataAccessException {
        SqlParameterSource parameterSource = new MapSqlParameterSource(TAG_ID, tagId);
        return jdbcTemplate.query(getAllPostsByTagSql, parameterSource, (resultSet, i) -> new PostRowMapper().mapRow(resultSet, i));
    }

    @Override
    public Long addPost(Post post) throws DataAccessException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameterSource = getParameterSourcePost(post);
        jdbcTemplate.update(addPostSql, parameterSource, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public int addTagToPost(Long id, Long tagId) throws DataAccessException {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(ID, id);
        parameterSource.addValue(TAG_ID, tagId);
        return jdbcTemplate.update(addTagToPostSql, parameterSource);
    }

    @Override
    public int updatePost(Post post) throws DataAccessException {
        MapSqlParameterSource parameterSource = getParameterSourcePost(post);
        return jdbcTemplate.update(updatePostSql, parameterSource);
    }

    @Override
    public int deletePost(Long id) throws DataAccessException {
        SqlParameterSource parameterSource = new MapSqlParameterSource(ID, id);
        return jdbcTemplate.update(deletePostSql, parameterSource);
    }

    @Override
    public int deleteTagInPost(Long id, Long tagId) throws DataAccessException {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(ID, id);
        parameterSource.addValue(TAG_ID, tagId);
        return jdbcTemplate.update(deleteTagInPostSql, parameterSource);
    }

    @Override
    public boolean checkPostById(Long id) {
        SqlParameterSource parameterSource = new MapSqlParameterSource(ID, id);
        return jdbcTemplate.queryForObject(checkPostByIdSql, parameterSource, boolean.class);
    }

    @Override
    public boolean checkTagInPostById(Long id, Long tagId) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(ID, id);
        parameterSource.addValue(TAG_ID, tagId);
        return jdbcTemplate.queryForObject(checkTagInPostByIdSql, parameterSource, boolean.class);
    }

    @Override
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
