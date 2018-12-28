package com.yeutushenka.dao.jdbc;

import com.yeutushenka.Author;
import com.yeutushenka.dao.AuthorDao;
import com.yeutushenka.dao.jdbc.mapper.AuthorRowMapper;
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

import static com.yeutushenka.dao.jdbc.mapper.AuthorRowMapper.*;

@Repository
public class AuthorDaoImpl implements AuthorDao {


    @Value("${author.select}")
    String getAllAuthorsSql;

    @Value("${author.selectById}")
    String getAuthorByIdSql;

    @Value("${author.selectByLogin}")
    String getAuthorByLoginSql;

    @Value("${author.insert}")
    String addAuthorSql;

    @Value("${author.update}")
    String updateAuthorSql;

    @Value("${author.delete}")
    String deleteAuthorSql;

    @Value("${author.checkUserById}")
    String checkAuthorByIdSql;

    @Value("${author.checkUserByLogin}")
    String checkAuthorByLogin;

    private AuthorRowMapper authorRowMapper;
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Author> getAllAuthors() throws DataAccessException {
        return jdbcTemplate.query(getAllAuthorsSql, authorRowMapper);

    }

    @Override
    public Author getAuthorById(Long id) throws DataAccessException {
        SqlParameterSource parameterSource = new MapSqlParameterSource(ID, id);
        return jdbcTemplate.queryForObject(getAuthorByIdSql, parameterSource, authorRowMapper);
    }


    @Override
    public Author getAuthorByLogin(String login) throws DataAccessException {
        SqlParameterSource parameterSource = new MapSqlParameterSource(LOGIN, login);
        return jdbcTemplate.queryForObject(getAuthorByIdSql, parameterSource, authorRowMapper);
    }

    @Override
    public Long addAuthor(Author author) throws DataAccessException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
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
        jdbcTemplate.update(addAuthorSql, parameterSource, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public int updateAuthor(Author author) throws DataAccessException {
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
        return jdbcTemplate.update(updateAuthorSql, parameterSource);

    }

    @Override
    public int deleteAuthor(Long id) throws DataAccessException {
        SqlParameterSource parameterSource = new MapSqlParameterSource(ID, id);
        return jdbcTemplate.update(deleteAuthorSql, parameterSource);
    }


    @Override
    public boolean checkAuthorById(Long id) {
        SqlParameterSource parameterSource = new MapSqlParameterSource(ID, id);
        return jdbcTemplate.queryForObject(checkAuthorByIdSql, parameterSource, boolean.class);
    }

    @Override
    public boolean checkAuthorByLogin(String login) {
        SqlParameterSource parameterSource = new MapSqlParameterSource(LOGIN, login);
        return jdbcTemplate.queryForObject(checkAuthorByIdSql, parameterSource, boolean.class);
    }


    @Autowired
    public void setTagRowMapper(AuthorRowMapper authorRowMapper) {
        this.authorRowMapper = authorRowMapper;
    }

    @Autowired
    public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
