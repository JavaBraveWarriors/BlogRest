package com.blog.service.impls;

import com.blog.Author;
import com.blog.dao.AuthorDao;
import com.blog.exception.NotFoundException;
import com.blog.exception.ValidationException;
import com.blog.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

    private AuthorDao authorDao;

    @Value("${authorService.incorrectId}")
    private String incorrectAuthorId;

    @Value("${authorService.notExist}")
    private String authorDoesNotExist;

    @Value("${authorService.incorrectLogin}")
    private String incorrectAuthorLogin;

    @Value("${authorService.exist}")
    private String authorExist;

    @Autowired
    public AuthorServiceImpl(AuthorDao authorDao) {
        this.authorDao = authorDao;
    }


    public List<Author> getAllAuthors() {
        return authorDao.getAllAuthors();
    }

    public Author getAuthorById(Long authorId) {
        validateAuthorId(authorId);
        checkAuthor(authorId);
        return authorDao.getAuthorById(authorId);
    }

    public Author getAuthorByLogin(String login) {
        validateAuthorLogin(login);
        checkAuthor(login);
        return authorDao.getAuthorByLogin(login);
    }

    public Long addAuthor(Author author) {
        checkAuthorExistence(author);
        author.setRegistrationTime(LocalDate.now());
        return authorDao.addAuthor(author);
    }

    public void updateAuthor(Author author) {
        authorDao.updateAuthor(author);
    }

    public void deleteAuthor(Long authorId) {
        validateAuthorId(authorId);
        checkAuthor(authorId);
        authorDao.deleteAuthor(authorId);
    }

    private void checkAuthorExistence(Author author) {
        if (authorDao.checkAuthorByLogin(author.getLogin()))
            throw new ValidationException(authorExist);
    }

    private void checkAuthor(Long authorId) {
        if (!authorDao.checkAuthorById(authorId))
            throw new NotFoundException(authorDoesNotExist);
    }

    private void checkAuthor(String authorLogin) {
        if (!authorDao.checkAuthorByLogin(authorLogin))
            throw new NotFoundException(authorDoesNotExist);
    }

    private void validateAuthorId(Long authorId) {
        if (authorId == null || authorId < 0L)
            throw new ValidationException(incorrectAuthorId);
    }

    private void validateAuthorLogin(String authorLogin) {
        if (authorLogin == null || authorLogin.isEmpty())
            throw new ValidationException(incorrectAuthorLogin);
    }
}
