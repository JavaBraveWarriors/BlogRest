package com.blog.dao.jdbc;

import com.blog.Author;
import com.blog.dao.AuthorDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:test-spring-dao.xml"})
@Transactional
public class AuthorDaoImplTest {

    private static Long CORRECT_ID_1 = 1L;
    private static Long CORRECT_CHECKED_ID = 2L;
    private static Long INCORRECT_ID_6 = 6L;
    private static String CORRECT_AUTHOR_LOGIN_2 = "testLogin2";
    private static String INCORRECT_AUTHOR_LOGIN_7 = "testLogin7";

    private static Long NEW_ID_4 = 4L;
    private static String NEW_AUTHOR_MAIL_4 = "testMail4";
    private static String NEW_AUTHOR_LOGIN_4 = "testLogin4";
    private static String NEW_AUTHOR_PASSWORD_4 = "testPsw4";
    private static String NEW_AUTHOR_FIRST_NAME_4 = "testFirst4";
    private static String NEW_AUTHOR_LAST_NAME_4 = "testLast4";
    private static String NEW_AUTHOR_DESCRIPTION_4 = "description4";
    private static String NEW_AUTHOR_PhONE_4 = "8072123";


    private static String UPDATED_AUTHOR_PhONE_1 = "1232223";
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
    public void getAllAuthors() {
        List<Author> authors = authorDao.getAllAuthors();
        assertNotNull(authors);
        assertEquals(3, authors.size());
    }

    @Test
    public void getAuthorById() {
        Author author = authorDao.getAuthorById(CORRECT_ID_1);
        assertNotNull(author);
        assertEquals(CORRECT_ID_1, author.getId());
    }

    @Test(expected = DataAccessException.class)
    public void getAuthorByIdWithException() {
        assertEquals("", authorDao.getAuthorById(INCORRECT_ID_6).getLogin());
    }

    @Test
    public void getAuthorByLogin() {
        Author author = authorDao.getAuthorByLogin(CORRECT_AUTHOR_LOGIN_2);
        assertNotNull(author);
        assertEquals(CORRECT_AUTHOR_LOGIN_2, author.getLogin());
    }

    @Test(expected = DataAccessException.class)
    public void getAuthorByLoginWithException() {
        Author author = authorDao.getAuthorByLogin(INCORRECT_AUTHOR_LOGIN_7);
        assertNotNull(author);
        assertEquals("testLogin3", author.getLogin());
    }

    @Test
    public void addAuthor() {
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

    @Test
    public void updateAuthor() {
        Author author = authorDao.getAuthorById(CORRECT_ID_1);
        assertNotNull(author);

        author.setPhone(UPDATED_AUTHOR_PhONE_1);
        author.setMail(UPDATED_AUTHOR_MAIL_1);
        author.setPassword(UPDATED_AUTHOR_PASSWORD_1);
        author.setLastName(UPDATED_AUTHOR_LAST_NAME_1);

        assertEquals(1, authorDao.updateAuthor(author));

        Author updatedAuthor = authorDao.getAuthorById(CORRECT_ID_1);
        assertNotNull(updatedAuthor);
        assertEquals(author, updatedAuthor);

    }

    @Test
    public void deleteAuthor() {
        List<Author> authors = authorDao.getAllAuthors();
        assertNotNull(authors);
        int initialSize = authors.size();

        Author newAuthor = authorDao.getAuthorById(CORRECT_ID_1);
        assertNotNull(newAuthor);

        assertEquals(1, authorDao.deleteAuthor(CORRECT_ID_1));

        authors = authorDao.getAllAuthors();
        assertNotNull(authors);
        assertEquals(initialSize - 1, authors.size());

    }

    @Test
    public void checkAuthorByIdReturnedTrue() {
        assertTrue(authorDao.checkAuthorById(CORRECT_CHECKED_ID));
    }

    @Test
    public void checkAuthorByIdReturnedFalse() {
        assertFalse(authorDao.checkAuthorById(INCORRECT_ID_6));
    }

    @Test
    public void checkAuthorByLoginReturnedTrue() {
        assertTrue(authorDao.checkAuthorByLogin(CORRECT_AUTHOR_LOGIN_2));

    }

    @Test
    public void checkAuthorByLoginReturnedFalse() {
        assertFalse(authorDao.checkAuthorByLogin(INCORRECT_AUTHOR_LOGIN_7));

    }
}