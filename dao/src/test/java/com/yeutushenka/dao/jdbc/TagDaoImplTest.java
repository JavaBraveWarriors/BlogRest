package com.yeutushenka.dao.jdbc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:test-spring-dao.xml"})
@Transactional
public class TagDaoImplTest {

    @Autowired
    private TagDao tagDao;


    @Test
    public void getAllTags() {
        List<Tag> tags = tagDao.getAllTags();
        assertNotNull(tags);
        assertEquals(4,tags.size());
    }

    @Test
    public void getTagById() {
    }

    @Test
    public void checkTagById() {
    }
}