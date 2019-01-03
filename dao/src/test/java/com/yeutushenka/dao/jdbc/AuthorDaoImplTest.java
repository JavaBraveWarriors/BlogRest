package com.yeutushenka.dao.jdbc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:test-spring-dao.xml"})
@Transactional
public class AuthorDaoImplTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getAllAuthors() {
    }

    @Test
    public void getAuthorById() {
    }

    @Test
    public void getAuthorByLogin() {
    }

    @Test
    public void addAuthor() {
    }

    @Test
    public void updateAuthor() {
    }

    @Test
    public void deleteAuthor() {
    }

    @Test
    public void checkAuthorById() {
    }

    @Test
    public void checkAuthorByLogin() {
    }
}