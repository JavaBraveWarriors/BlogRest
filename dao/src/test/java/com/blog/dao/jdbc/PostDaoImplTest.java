package com.blog.dao.jdbc;

import com.blog.dao.PostDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:test-spring-dao.xml"})
@Transactional
public class PostDaoImplTest {

    @Autowired
    private PostDao postDao;



    @Test
    public void getAllPosts() {
        System.out.println(postDao.getAllPosts());
    }

    @Test
    public void getPostById() {
    }

    @Test
    public void getAllPostsByAuthorId() {
    }

    @Test
    public void getPostsByInitialIdAndQuantity() {
    }

    @Test
    public void getAllPostsByAuthorIdAndTagId() {
    }

    @Test
    public void addPost() {
    }

    @Test
    public void updatePost() {
    }

    @Test
    public void deletePost() {
    }

    @Test
    public void checkPostById() {
    }

    @Test
    public void checkPostByAuthorId() {
    }
}