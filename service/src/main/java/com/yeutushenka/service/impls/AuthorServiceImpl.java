package com.yeutushenka.service.impls;

import com.yeutushenka.Author;
import com.yeutushenka.service.AuthorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {
    @Override
    public List<Author> getAllAuthors() {
        return null;
    }

    @Override
    public Author getAuthorById(Long userId) {
        return null;
    }

    @Override
    public Author getAuthorByLogin(String login) {
        return null;
    }

    @Override
    public Long addAuthor(Author author) {
        return null;
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
