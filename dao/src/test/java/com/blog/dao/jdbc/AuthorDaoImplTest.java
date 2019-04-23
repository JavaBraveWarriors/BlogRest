package com.blog.dao.jdbc;

import com.blog.dao.AuthorDao;
import com.blog.model.Author;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * The Author dao impl test.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-spring-dao.xml"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthorDaoImplTest {

    private static final int COUNT_USERS_IN_DB = 5;
    private static final Long CORRECT_ID = 1L;
    private static final Long CORRECT_CHECKED_ID = 2L;
    private static final Long INCORRECT_ID = 6L;
    private static final Long INCORRECT_NEGATIVE_ID = -111L;
    private static final String CORRECT_AUTHOR_LOGIN = "testLogin2";
    private static final String INCORRECT_AUTHOR_LOGIN = "testLogin7";
    private static final Long CORRECT_DELETE_ID = 5L;
    private static final String UPDATED_AUTHOR_PHONE = "1232223";
    private static final String UPDATED_AUTHOR_MAIL = "testUpdateMail1";
    private static final String UPDATED_AUTHOR_PASSWORD = "testUpdatePsw1";
    private static final String UPDATED_AUTHOR_LAST_NAME = "testUpdateLast1";

    private static final Author CORRECT_AUTHOR = new Author(
            null,
            "testMail4",
            "testLogin4",
            "testPsw4",
            "testFirst4",
            "testLast4",
            "description4",
            "8072123");

    @Autowired
    private AuthorDao authorDao;

    @Test
    public void getAllAuthorsSuccess() {
        List<Author> authors = authorDao.getAllAuthors();
        assertNotNull(authors);
        assertEquals(COUNT_USERS_IN_DB, authors.size());
    }

    @Test
    public void getAuthorByIdSuccess() {
        Author author = authorDao.getAuthorById(CORRECT_ID);
        assertNotNull(author);
        assertEquals(CORRECT_ID, author.getId());
    }

    @Test(expected = DataAccessException.class)
    public void getAuthorByIncorrectId() {
        assertNull(authorDao.getAuthorById(INCORRECT_ID));
    }

    @Test(expected = DataAccessException.class)
    public void getAuthorByNullId() {
        assertNull(authorDao.getAuthorById(null));
    }

    @Test
    public void getAuthorByLoginSuccess() {
        Author author = authorDao.getAuthorByLogin(CORRECT_AUTHOR_LOGIN);
        assertNotNull(author);
        assertEquals(CORRECT_AUTHOR_LOGIN, author.getLogin());
    }

    @Test(expected = DataAccessException.class)
    public void getAuthorByIncorrectLogin() {
        assertNull(authorDao.getAuthorByLogin(INCORRECT_AUTHOR_LOGIN));
    }

    @Test(expected = DataAccessException.class)
    public void getAuthorByNullLogin() {
        assertNull(authorDao.getAuthorByLogin(null));
    }

    @Test
    public void addAuthorSuccess() {
        List<Author> authors = authorDao.getAllAuthors();
        assertNotNull(authors);
        int initialSize = authors.size();

        Long newId = authorDao.addAuthor(CORRECT_AUTHOR);
        assertNotNull(newId);
        Author newAuthor = authorDao.getAuthorById(newId);
        assertEquals(CORRECT_AUTHOR.getMail(), newAuthor.getMail());
        assertEquals(CORRECT_AUTHOR.getLogin(), newAuthor.getLogin());
        assertEquals(CORRECT_AUTHOR.getPassword(), newAuthor.getPassword());
        assertEquals(CORRECT_AUTHOR.getFirstName(), newAuthor.getFirstName());
        assertEquals(CORRECT_AUTHOR.getLastName(), newAuthor.getLastName());
        assertEquals(CORRECT_AUTHOR.getDescription(), newAuthor.getDescription());
        assertEquals(CORRECT_AUTHOR.getPhone(), newAuthor.getPhone());

        authors = authorDao.getAllAuthors();
        assertNotNull(authors);
        assertEquals(initialSize + 1, authors.size());
    }

    @Test(expected = NullPointerException.class)
    public void addNullAuthor() {
        assertNull(authorDao.addAuthor(null));
    }

    @Test
    public void updateAuthorSuccess() {
        Author author = authorDao.getAuthorById(CORRECT_CHECKED_ID);
        assertNotNull(author);

        author.setPhone(UPDATED_AUTHOR_PHONE);
        author.setMail(UPDATED_AUTHOR_MAIL);
        author.setPassword(UPDATED_AUTHOR_PASSWORD);
        author.setLastName(UPDATED_AUTHOR_LAST_NAME);

        assertTrue(authorDao.updateAuthor(author));

        Author updatedAuthor = authorDao.getAuthorById(CORRECT_CHECKED_ID);
        assertNotNull(updatedAuthor);
        assertEquals(author.getLogin(), updatedAuthor.getLogin());
    }

    @Test
    public void updateIncorrectAuthor() {
        Author author = authorDao.getAuthorById(CORRECT_ID);
        assertNotNull(author);

        author.setId(INCORRECT_ID);
        author.setPhone(UPDATED_AUTHOR_PHONE);
        author.setMail(UPDATED_AUTHOR_MAIL);
        author.setPassword(UPDATED_AUTHOR_PASSWORD);
        author.setLastName(UPDATED_AUTHOR_LAST_NAME);

        assertFalse(authorDao.updateAuthor(author));
    }

    @Test(expected = NullPointerException.class)
    public void updateNullAuthor() {
        assertFalse(authorDao.updateAuthor(null));
    }

    @Test
    public void deleteAuthorSuccess() {
        List<Author> authors = authorDao.getAllAuthors();
        assertNotNull(authors);
        int initialSize = authors.size();

        assertTrue(authorDao.deleteAuthor(CORRECT_DELETE_ID));

        authors = authorDao.getAllAuthors();
        assertNotNull(authors);
        assertEquals(initialSize - 1, authors.size());
    }

    @Test
    public void deleteIncorrectAuthor() {
        List<Author> authors = authorDao.getAllAuthors();
        assertNotNull(authors);
        int initialSize = authors.size();

        assertFalse(authorDao.deleteAuthor(INCORRECT_ID));

        authors = authorDao.getAllAuthors();
        assertNotNull(authors);
        assertEquals(initialSize, authors.size());
    }

    @Test
    public void deleteNullAuthor() {
        List<Author> authors = authorDao.getAllAuthors();
        assertNotNull(authors);
        int initialSize = authors.size();

        assertFalse(authorDao.deleteAuthor(null));

        authors = authorDao.getAllAuthors();
        assertNotNull(authors);
        assertEquals(initialSize, authors.size());
    }

    @Test
    public void checkAuthorByCorrectId() {
        assertTrue(authorDao.checkAuthorById(CORRECT_CHECKED_ID));
    }

    @Test
    public void checkAuthorByIncorrectId() {
        assertFalse(authorDao.checkAuthorById(INCORRECT_ID));
    }

    @Test
    public void checkAuthorByNullId() {
        assertFalse(authorDao.checkAuthorById(null));
    }

    @Test
    public void checkAuthorByIncorrectNegativeId() {
        assertFalse(authorDao.checkAuthorById(INCORRECT_NEGATIVE_ID));
    }

    @Test
    public void checkAuthorByCorrectLogin() {
        assertTrue(authorDao.checkAuthorByLogin(CORRECT_AUTHOR_LOGIN));
    }

    @Test
    public void checkAuthorByIncorrectLogin() {
        assertFalse(authorDao.checkAuthorByLogin(INCORRECT_AUTHOR_LOGIN));
    }

    @Test
    public void checkAuthorByNullLogin() {
        assertFalse(authorDao.checkAuthorByLogin(null));
    }
}