package com.blog.service.impls;

import com.blog.Author;
import com.blog.dao.AuthorDao;
import com.blog.exception.NotFoundException;
import com.blog.exception.ValidationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthorServiceImplTest {

    @Mock
    private AuthorDao authorDao;

    @InjectMocks
    private AuthorServiceImpl authorService;

    private static Long CORRECT_AUTHOR_ID = 1L;
    private static Long INCORRECT_AUTHOR_ID = -2L;
    private static String CORRECT_AUTHOR_LOGIN = "testLoginCorrect";
    private static String INCORRECT_AUTHOR_LOGIN = "";
    private static Author author = new Author(
            1L,
            "test@mail.ru",
            "testLogin",
            "testPsw",
            "testFirstName",
            "testLastName",
            "testDescription",
            "testPhone");
    private static List<Author> testAuthors = Collections.singletonList(author);


    @Test
    public void getAllAuthors() {
        Mockito.when(authorDao.getAllAuthors()).thenReturn(testAuthors);
        List<Author> allAuthors = authorService.getAllAuthors();
        verify(authorDao, times(1)).getAllAuthors();
        Assert.assertNotNull(allAuthors);
        Assert.assertEquals(allAuthors.size(), testAuthors.size());
    }

    /*
    authorDao.checkAuthorById(anyLong())
    - success
    - null
    - -100
    - 1 -> DAO returns null = not found
     */
    @Test
    public void getAuthorByIdSuccess() {
        when(authorDao.checkAuthorById(anyLong())).thenReturn(true);
        authorService.getAuthorById(CORRECT_AUTHOR_ID);
        verify(authorDao, times(1)).getAuthorById(anyLong());
        verify(authorDao, times(1)).checkAuthorById(anyLong());
    }

    @Test(expected = ValidationException.class)
    public void getAuthorByIdWithValidateException() {
        authorService.getAuthorById(INCORRECT_AUTHOR_ID);
        verify(authorDao, never()).getAuthorById(anyLong());
        verify(authorDao, never()).checkAuthorById(anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void getAuthorByIdWithNotFoundException() {
        when(authorDao.checkAuthorById(anyLong())).thenReturn(false);
        authorService.getAuthorById(CORRECT_AUTHOR_ID);
        verify(authorDao, never()).getAuthorById(anyLong());
        verify(authorDao, times(1)).checkAuthorById(anyLong());
    }

    @Test
    public void getAuthorByLoginSuccess() {
        when(authorDao.checkAuthorByLogin(anyString())).thenReturn(true);
        authorService.getAuthorByLogin(CORRECT_AUTHOR_LOGIN);
        verify(authorDao, times(1)).getAuthorByLogin(anyString());
        verify(authorDao, times(1)).checkAuthorByLogin(anyString());
    }

    @Test(expected = ValidationException.class)
    public void getAuthorByLoginWithValidateException() {
        authorService.getAuthorByLogin(INCORRECT_AUTHOR_LOGIN);
        verify(authorDao, never()).getAuthorByLogin(anyString());
        verify(authorDao, never()).checkAuthorByLogin(anyString());
    }

    @Test(expected = NotFoundException.class)
    public void getAuthorByLoginWithNotFoundException() {
        when(authorDao.checkAuthorByLogin(anyString())).thenReturn(false);
        authorService.getAuthorByLogin(CORRECT_AUTHOR_LOGIN);
        verify(authorDao, never()).getAuthorByLogin(anyString());
        verify(authorDao, times(1)).checkAuthorByLogin(anyString());
    }


    @Test
    public void addAuthorSuccess() {
        when(authorDao.checkAuthorByLogin(anyString())).thenReturn(false);
        authorService.addAuthor(author);
        verify(authorDao, times(1)).addAuthor(any(Author.class));
        verify(authorDao, times(1)).checkAuthorByLogin(anyString());
    }

    @Test(expected = ValidationException.class)
    public void addAuthorWithValidationException() {
        when(authorDao.checkAuthorByLogin(anyString())).thenReturn(true);
        authorService.addAuthor(author);
        verify(authorDao, never()).addAuthor(any(Author.class));
        verify(authorDao, times(1)).checkAuthorByLogin(anyString());
    }

    @Test
    public void updateAuthorSuccess() {
        when(authorDao.checkAuthorById(anyLong())).thenReturn(true);
        authorService.updateAuthor(author);
        verify(authorDao, times(1)).updateAuthor(any(Author.class));
        verify(authorDao, times(1)).checkAuthorById(anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void updateAuthorWithNotFoundException() {
        when(authorDao.checkAuthorById(anyLong())).thenReturn(false);
        authorService.updateAuthor(author);
        verify(authorDao, never()).updateAuthor(any(Author.class));
        verify(authorDao, times(1)).checkAuthorById(anyLong());
    }

    @Test
    public void deleteAuthorSuccess() {
        when(authorDao.checkAuthorById(anyLong())).thenReturn(true);
        authorService.deleteAuthor(CORRECT_AUTHOR_ID);
        verify(authorDao, times(1)).deleteAuthor(anyLong());
        verify(authorDao, times(1)).checkAuthorById(anyLong());
    }

    @Test(expected = ValidationException.class)
    public void deleteAuthorWithValidationException() {
        authorService.deleteAuthor(INCORRECT_AUTHOR_ID);
        verify(authorDao, never()).deleteAuthor(anyLong());
        verify(authorDao, never()).checkAuthorById(anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void deleteAuthorWithNotFoundException() {
        when(authorDao.checkAuthorById(anyLong())).thenReturn(false);
        authorService.deleteAuthor(CORRECT_AUTHOR_ID);
        verify(authorDao, never()).deleteAuthor(anyLong());
        verify(authorDao, times(1)).checkAuthorById(anyLong());
    }
}