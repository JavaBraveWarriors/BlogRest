package com.blog.dao.jdbc;

import com.blog.Author;
import com.blog.dao.AuthorDao;
import com.blog.dao.jdbc.mapper.AuthorRowMapper;
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

import static com.blog.dao.jdbc.mapper.AuthorRowMapper.*;

@Repository
public class AuthorDaoImpl implements AuthorDao {


    @Value("${author.select}")
    private String getAllAuthorsSql;

    @Value("${author.selectById}")
    private String getAuthorByIdSql;

    @Value("${author.selectByLogin}")
    private String getAuthorByLoginSql;

    @Value("${author.insert}")
    private String addAuthorSql;

    @Value("${author.update}")
    private String updateAuthorSql;

    @Value("${author.delete}")
    private String deleteAuthorSql;

    @Value("${author.checkUserById}")
    private String checkAuthorByIdSql;

    @Value("${author.checkUserByLogin}")
    private String checkAuthorByLoginSql;

    private AuthorRowMapper authorRowMapper;
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public AuthorDaoImpl(AuthorRowMapper authorRowMapper, NamedParameterJdbcTemplate jdbcTemplate) {
        this.authorRowMapper = authorRowMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Author> getAllAuthors() throws DataAccessException {
        return jdbcTemplate.query(getAllAuthorsSql, authorRowMapper);

    }

    public Author getAuthorById(Long id) throws DataAccessException {
        SqlParameterSource parameterSource = new MapSqlParameterSource(ID, id);
        return jdbcTemplate.queryForObject(getAuthorByIdSql, parameterSource, authorRowMapper);
    }


    public Author getAuthorByLogin(String login) throws DataAccessException {
        SqlParameterSource parameterSource = new MapSqlParameterSource(LOGIN, login);
        return jdbcTemplate.queryForObject(getAuthorByLoginSql, parameterSource, authorRowMapper);
    }

    public Long addAuthor(Author author) throws DataAccessException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameterSource = getParameterSourceAuthor(author);
        jdbcTemplate.update(addAuthorSql, parameterSource, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public boolean updateAuthor(Author author) throws DataAccessException {
        MapSqlParameterSource parameterSource = getParameterSourceAuthor(author);
        return jdbcTemplate.update(updateAuthorSql, parameterSource) == 1;
    }

    public boolean deleteAuthor(Long id) throws DataAccessException {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource(ID, id);
        return jdbcTemplate.update(deleteAuthorSql, parameterSource) == 1;
    }

    public boolean checkAuthorById(Long id) {
        SqlParameterSource parameterSource = new MapSqlParameterSource(ID, id);
        return jdbcTemplate.queryForObject(checkAuthorByIdSql, parameterSource, boolean.class);
    }

    public boolean checkAuthorByLogin(String login) {
        SqlParameterSource parameterSource = new MapSqlParameterSource(LOGIN, login);
        return jdbcTemplate.queryForObject(checkAuthorByLoginSql, parameterSource, boolean.class);
    }

    private MapSqlParameterSource getParameterSourceAuthor(Author author) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(ID, author.getId());
        parameterSource.addValue(MAIL, author.getMail());
        parameterSource.addValue(LOGIN, author.getLogin());
        parameterSource.addValue(PASSWORD, author.getPassword());
        parameterSource.addValue(FIRST_NAME, author.getFirstName());
        parameterSource.addValue(LAST_NAME, author.getLastName());
        parameterSource.addValue(REGISTRATION_DATE, author.getRegistrationTime());
        parameterSource.addValue(PHONE, author.getPhone());
        parameterSource.addValue(DESCRIPTION, author.getDescription());
        return parameterSource;
    }

}
