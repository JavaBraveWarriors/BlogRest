package com.blog.dao.jdbc;

import com.blog.Tag;
import com.blog.dao.TagDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:test-spring-dao.xml"})
@Transactional
public class TagDaoImplTest {

    @Autowired
    private TagDao tagDao;


    @Test
    public void getAllTags() {
        List<Tag> tags = tagDao.getAllTags();
        for (Tag tag:
                tags) {
            System.out.println(tag);
        }
        assertNotNull(tags);
        //assertEquals(4, tags.size());
    }

    @Test
    public void getTagById() {
        System.out.println(tagDao.getTagById(1L));

    }

    @Test
    public void checkTagById() {
    }
}