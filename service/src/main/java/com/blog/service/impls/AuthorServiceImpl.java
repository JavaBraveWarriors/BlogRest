package com.blog.service.impls;

import com.blog.Author;
import com.blog.dao.AuthorDao;
import com.blog.exception.InternalServerException;
import com.blog.exception.NotFoundException;
import com.blog.exception.ValidationException;
import com.blog.service.AuthorService;
import com.blog.validator.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

    /**
     * This field used for logging events.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * This field is used to communicate with the database.
     */
    private AuthorDao authorDao;

    /**
     * This field is used for validate data.
     */
    private Validator validator;

    @Value("${authorService.notUpdated}")
    private String authorNotUpdated;

    @Value("${authorService.notDeleted}")
    private String authorNotDeleted;


    @Autowired
    public AuthorServiceImpl(final AuthorDao authorDao, Validator validator) {
        this.authorDao = authorDao;
        this.validator = validator;
    }

    public List<Author> getAllAuthors() {
        return authorDao.getAllAuthors();
    }

    public Author getAuthorById(Long authorId) throws ValidationException, NotFoundException {
        LOGGER.debug("Search author by id = [{}]", authorId);
        validator.validateAuthorId(authorId);
        return authorDao.getAuthorById(authorId);
    }

    public Author getAuthorByLogin(String login) throws ValidationException, NotFoundException {
        LOGGER.debug("Search author by login = [{}]", login);
        validator.validateAuthorLogin(login);
        return authorDao.getAuthorByLogin(login);
    }

    public Long addAuthor(Author author) throws ValidationException {
        LOGGER.debug("Adds new author = [{}]", author);
        validator.checkAuthorExistence(author);
        author.setRegistrationTime(LocalDateTime.now());
        return authorDao.addAuthor(author);
    }

    public void updateAuthor(Author author) throws NotFoundException, InternalServerException {
        LOGGER.debug("Updates author where id = [{}]", author.getId());
        validator.validateAuthorId(author.getId());
        if (!authorDao.updateAuthor(author))
            throw new InternalServerException(authorNotUpdated);
    }

    public void deleteAuthor(Long authorId) throws ValidationException, NotFoundException, InternalServerException {
        LOGGER.debug("Deletes author by id = [{}]", authorId);
        validator.validateAuthorId(authorId);
        if (!authorDao.deleteAuthor(authorId))
            throw new InternalServerException(authorNotDeleted);
    }
}
