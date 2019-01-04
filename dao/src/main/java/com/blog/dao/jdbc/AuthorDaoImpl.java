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

@Repository
public class AuthorDaoImpl implements AuthorDao {

    public static final String ID = "id";
    public static final String MAIL = "mail";
    public static final String LOGIN = "login";
    public static final String PASSWORD = "password";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String REGISTRATION_DATE = "registration_date";
    public static final String PHONE = "phone";
    public static final String DESCRIPTION = "description";

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
    String checkAuthorByLoginSql;

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
        return jdbcTemplate.queryForObject(getAuthorByLoginSql, parameterSource, authorRowMapper);
    }

    @Override
    public Long addAuthor(Author author) throws DataAccessException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameterSource = getParameterSourceAuthor(author);
        jdbcTemplate.update(addAuthorSql, parameterSource, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public int updateAuthor(Author author) throws DataAccessException {
        MapSqlParameterSource parameterSource = getParameterSourceAuthor(author);
        return jdbcTemplate.update(updateAuthorSql, parameterSource);
    }

    @Override
    public int deleteAuthor(Long id) throws DataAccessException {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource(ID, id);
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

    @Autowired
    public void setTagRowMapper(AuthorRowMapper authorRowMapper) {
        this.authorRowMapper = authorRowMapper;
    }

    @Autowired
    public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
