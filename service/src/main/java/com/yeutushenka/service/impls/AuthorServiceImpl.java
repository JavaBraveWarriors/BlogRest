package com.yeutushenka.service.impls;

import com.yeutushenka.Author;
import com.yeutushenka.dao.AuthorDao;
import com.yeutushenka.exception.NotFoundException;
import com.yeutushenka.exception.ValidationException;
import com.yeutushenka.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    public AuthorServiceImpl(AuthorDao authorDao) {
        this.authorDao = authorDao;
    }


    @Override
    public List<Author> getAllAuthors() {
        return authorDao.getAllAuthors();
    }

    @Override
    public Author getAuthorById(Long userId) {
        if (userId == null || userId < 0L)
            throw new ValidationException(incorrectAuthorId);
        if (!authorDao.checkAuthorById(userId))
            throw new NotFoundException(authorDoesNotExist);
        return authorDao.getAuthorById(userId);
    }

    @Override
    public Author getAuthorByLogin(String login) {
        if (login == null || login.isEmpty())
            throw new ValidationException(incorrectAuthorLogin);
        if (!authorDao.checkAuthorByLogin(login))
            throw new NotFoundException(authorDoesNotExist);
        return authorDao.getAuthorByLogin(login);
    }

    @Override
    public Long addAuthor(Author author) {
        return 0L;
    }

    @Override
    public int updateAuthor(Author author) {
        return 0;
    }

    @Override
    public int deleteAuthor(Long userId) {
        return 0;
    }
}
