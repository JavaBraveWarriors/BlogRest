package com.blog.dao.jdbc;

import com.blog.Author;
import com.blog.dao.AuthorDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:test-spring-dao.xml"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthorDaoImplTest {

    private static Long CORRECT_ID_1 = 1L;
    private static Long CORRECT_CHECKED_ID = 2L;
    private static Long INCORRECT_ID_6 = 6L;
    private static Long INCORRECT_NEGATIVE_ID = -111L;
    private static String CORRECT_AUTHOR_LOGIN_2 = "testLogin2";
    private static String INCORRECT_AUTHOR_LOGIN_7 = "testLogin7";

    private static Long NEW_ID_4 = 5L;
    private static String NEW_AUTHOR_MAIL_4 = "testMail4";
    private static String NEW_AUTHOR_LOGIN_4 = "testLogin4";
    private static String NEW_AUTHOR_PASSWORD_4 = "testPsw4";
    private static String NEW_AUTHOR_FIRST_NAME_4 = "testFirst4";
    private static String NEW_AUTHOR_LAST_NAME_4 = "testLast4";
    private static String NEW_AUTHOR_DESCRIPTION_4 = "description4";
    private static String NEW_AUTHOR_PhONE_4 = "8072123";


    private static String UPDATED_AUTHOR_PHONE_1 = "1232223";
    private static String UPDATED_AUTHOR_MAIL_1 = "testUpdateMail1";
    private static String UPDATED_AUTHOR_PASSWORD_1 = "testUpdatePsw1";
    private static String UPDATED_AUTHOR_LAST_NAME_1 = "testUpdateLast1";


    private static Author author = new Author(
            null,
            NEW_AUTHOR_MAIL_4,
            NEW_AUTHOR_LOGIN_4,
            NEW_AUTHOR_PASSWORD_4,
            NEW_AUTHOR_FIRST_NAME_4,
            NEW_AUTHOR_LAST_NAME_4,
            NEW_AUTHOR_DESCRIPTION_4,
            NEW_AUTHOR_PhONE_4);

    @Autowired
    private AuthorDao authorDao;


    @Test
    public void getAllAuthorsSuccess() {
        List<Author> authors = authorDao.getAllAuthors();
        assertNotNull(authors);
        assertEquals(4, authors.size());
    }

    @Test
    public void getAuthorByIdSuccess() {
        Author author = authorDao.getAuthorById(CORRECT_ID_1);
        assertNotNull(author);
        assertEquals(CORRECT_ID_1, author.getId());
    }

    @Test(expected = DataAccessException.class)
    public void getAuthorByIdWithDataAccessException1() {
        assertNull(authorDao.getAuthorById(INCORRECT_ID_6));
    }

    @Test(expected = DataAccessException.class)
    public void getAuthorByIdWithDataAccessException2() {
        assertNull(authorDao.getAuthorById(null));
    }

    @Test
    public void getAuthorByLoginSuccess() {
        Author author = authorDao.getAuthorByLogin(CORRECT_AUTHOR_LOGIN_2);
        assertNotNull(author);
        assertEquals(CORRECT_AUTHOR_LOGIN_2, author.getLogin());
    }

    @Test(expected = DataAccessException.class)
    public void getAuthorByLoginWithException1() {
        assertNull(authorDao.getAuthorByLogin(INCORRECT_AUTHOR_LOGIN_7));
    }

    @Test(expected = DataAccessException.class)
    public void getAuthorByLoginWithException2() {
        assertNull(authorDao.getAuthorByLogin(null));

    }

    @Test
    public void addAuthorSuccess() {
        List<Author> authors = authorDao.getAllAuthors();
        assertNotNull(authors);
        int initialSize = authors.size();

        Long newId = authorDao.addAuthor(author);
        assertNotNull(newId);
        Author newAuthor = authorDao.getAuthorById(newId);
        assertEquals(NEW_ID_4, newAuthor.getId());
        assertEquals(NEW_AUTHOR_MAIL_4, newAuthor.getMail());
        assertEquals(NEW_AUTHOR_LOGIN_4, newAuthor.getLogin());
        assertEquals(NEW_AUTHOR_PASSWORD_4, newAuthor.getPassword());
        assertEquals(NEW_AUTHOR_FIRST_NAME_4, newAuthor.getFirstName());
        assertEquals(NEW_AUTHOR_LAST_NAME_4, newAuthor.getLastName());
        assertEquals(NEW_AUTHOR_DESCRIPTION_4, newAuthor.getDescription());
        assertEquals(NEW_AUTHOR_PhONE_4, newAuthor.getPhone());

        authors = authorDao.getAllAuthors();
        assertNotNull(authors);
        assertEquals(initialSize + 1, authors.size());
    }

    @Test(expected = NullPointerException.class)
    public void addAuthorWithNullPointerException() {
        assertNull(authorDao.addAuthor(null));
    }

    @Test
    public void updateAuthorSuccess() {
        Author author = authorDao.getAuthorById(CORRECT_CHECKED_ID);
        assertNotNull(author);

        author.setPhone(UPDATED_AUTHOR_PHONE_1);
        author.setMail(UPDATED_AUTHOR_MAIL_1);
        author.setPassword(UPDATED_AUTHOR_PASSWORD_1);
        author.setLastName(UPDATED_AUTHOR_LAST_NAME_1);

        assertTrue(authorDao.updateAuthor(author));

        Author updatedAuthor = authorDao.getAuthorById(CORRECT_CHECKED_ID);
        assertNotNull(updatedAuthor);
        assertEquals(author.getLogin(), updatedAuthor.getLogin());
    }

    @Test
    public void updateAuthorNotUpdated() {
        Author author = authorDao.getAuthorById(CORRECT_ID_1);
        assertNotNull(author);
        author.setId(INCORRECT_ID_6);
        author.setPhone(UPDATED_AUTHOR_PHONE_1);
        author.setMail(UPDATED_AUTHOR_MAIL_1);
        author.setPassword(UPDATED_AUTHOR_PASSWORD_1);
        author.setLastName(UPDATED_AUTHOR_LAST_NAME_1);

        assertFalse(authorDao.updateAuthor(author));
    }

    @Test(expected = NullPointerException.class)
    public void updateAuthorWithNullPointerException() {
        assertFalse(authorDao.updateAuthor(null));
    }

    @Test
    public void deleteAuthorSuccess() {
        List<Author> authors = authorDao.getAllAuthors();
        assertNotNull(authors);
        int initialSize = authors.size();

        assertTrue(authorDao.deleteAuthor(CORRECT_ID_1));

        authors = authorDao.getAllAuthors();
        assertNotNull(authors);
        assertEquals(initialSize - 1, authors.size());
    }

    @Test
    public void deleteAuthorNotDeleted1() {
        List<Author> authors = authorDao.getAllAuthors();
        assertNotNull(authors);
        int initialSize = authors.size();

        assertFalse(authorDao.deleteAuthor(INCORRECT_ID_6));

        authors = authorDao.getAllAuthors();
        assertNotNull(authors);
        assertEquals(initialSize, authors.size());
    }

    @Test
    public void deleteAuthorNotDeleted2() {
        List<Author> authors = authorDao.getAllAuthors();
        assertNotNull(authors);
        int initialSize = authors.size();

        assertFalse(authorDao.deleteAuthor(null));

        authors = authorDao.getAllAuthors();
        assertNotNull(authors);
        assertEquals(initialSize, authors.size());
    }

    @Test
    public void checkAuthorByIdReturnedTrue() {
        assertTrue(authorDao.checkAuthorById(CORRECT_CHECKED_ID));
    }

    @Test
    public void checkAuthorByIdReturnedFalse1() {
        assertFalse(authorDao.checkAuthorById(INCORRECT_ID_6));
    }

    @Test
    public void checkAuthorByIdReturnedFalse2() {
        assertFalse(authorDao.checkAuthorById(null));
    }

    @Test
    public void checkAuthorByIdReturnedFalse3() {
        assertFalse(authorDao.checkAuthorById(INCORRECT_NEGATIVE_ID));
    }

    @Test
    public void checkAuthorByLoginReturnedTrue() {
        assertTrue(authorDao.checkAuthorByLogin(CORRECT_AUTHOR_LOGIN_2));
    }

    @Test
    public void checkAuthorByLoginReturnedFalse1() {
        assertFalse(authorDao.checkAuthorByLogin(INCORRECT_AUTHOR_LOGIN_7));
    }

    @Test
    public void checkAuthorByLoginReturnedFalse2() {
        assertFalse(authorDao.checkAuthorByLogin(null));
    }
}