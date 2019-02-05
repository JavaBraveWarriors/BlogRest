package com.blog.dao.jdbc;

import com.blog.Author;
import com.blog.dao.AuthorDao;
import com.blog.dao.jdbc.mapper.AuthorRowMapper;
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

import static com.blog.dao.jdbc.mapper.AuthorRowMapper.*;

/**
 * This interface implementation {AuthorDao} allows operations to easily manage a database for an Author object.
 * Use this class if you want to access the Author database.
 *
 * @author Aliaksandr Yeutushenka
 * @see AuthorDao
 * @see AuthorRowMapper
 * @see Author
 */
@Repository
public class AuthorDaoImpl implements AuthorDao {

    private static final Logger LOGGER = LogManager.getLogger();

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

    /**
     * Allows to make a mapping for an {Author} object from database.
     */
    private AuthorRowMapper authorRowMapper;

    /**
     * The JdbcTemplate which uses named parameters.
     */
    private NamedParameterJdbcTemplate jdbcTemplate;

    /**
     * Create a new AuthorDaoImpl for the given {@link AuthorRowMapper} and {@link NamedParameterJdbcTemplate }
     *
     * @param authorRowMapper the author row mapper
     * @param jdbcTemplate    the jdbc template
     */
    @Autowired
    public AuthorDaoImpl(AuthorRowMapper authorRowMapper, NamedParameterJdbcTemplate jdbcTemplate) {
        this.authorRowMapper = authorRowMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Author> getAllAuthors() throws DataAccessException {
        LOGGER.debug("Get all authors from database.");
        return jdbcTemplate.query(getAllAuthorsSql, authorRowMapper);
    }

    public Author getAuthorById(Long id) throws DataAccessException {
        LOGGER.debug("Get author by id = [{}] from database.", id);
        SqlParameterSource parameterSource = new MapSqlParameterSource(ID, id);
        return jdbcTemplate.queryForObject(getAuthorByIdSql, parameterSource, authorRowMapper);
    }


    public Author getAuthorByLogin(String login) throws DataAccessException {
        LOGGER.debug("Get author by Login = {} from database.", login);
        SqlParameterSource parameterSource = new MapSqlParameterSource(LOGIN, login);
        return jdbcTemplate.queryForObject(getAuthorByLoginSql, parameterSource, authorRowMapper);
    }

    public Long addAuthor(final Author author) throws DataAccessException {
        LOGGER.debug("Add new author [{}] in database.", author);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameterSource = getParameterSourceAuthor(author);
        jdbcTemplate.update(addAuthorSql, parameterSource, keyHolder, new String[]{ID});
        return keyHolder.getKey().longValue();
    }

    public boolean updateAuthor(final Author author) throws DataAccessException {
        LOGGER.debug("Update author [{}] in database.", author);
        MapSqlParameterSource parameterSource = getParameterSourceAuthor(author);
        return jdbcTemplate.update(updateAuthorSql, parameterSource) == 1;
    }

    public boolean deleteAuthor(Long id) throws DataAccessException {
        LOGGER.debug("Delete author by id = [{}] from database.", id);
        MapSqlParameterSource parameterSource = new MapSqlParameterSource(ID, id);
        return jdbcTemplate.update(deleteAuthorSql, parameterSource) == 1;
    }

    public boolean checkAuthorById(Long id) {
        LOGGER.debug("Check author by id = [{}] from database.", id);
        SqlParameterSource parameterSource = new MapSqlParameterSource(ID, id);
        return jdbcTemplate.queryForObject(checkAuthorByIdSql, parameterSource, boolean.class);
    }

    public boolean checkAuthorByLogin(String login) {
        LOGGER.debug("Check author by id = [{}] from database.", login);
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
