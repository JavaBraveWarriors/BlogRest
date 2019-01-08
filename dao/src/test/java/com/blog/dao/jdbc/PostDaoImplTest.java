package com.blog.dao.jdbc;

import com.blog.Post;
import com.blog.dao.PostDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:test-spring-dao.xml"})
public class PostDaoImplTest {

    private static Long CORRECT_POST_ID = 1L;
    private static Long INCORRECT_POST_ID = 6L;
    private static Long CORRECT_USER_ID = 1L;
    private static Long INCORRECT_USER_ID = 9L;
    private static Long CORRECT_TAG_ID = 3L;
    private static Long CORRECT_TAG_ID_2 = 2L;


    private static Long NEW_POST_ID_4 = 4L;
    private static String NEW_POST_TITLE_4 = "newTitle4";
    private static String NEW_POST_DESCRIPTION_4 = "newDescription4";
    private static String NEW_POST_TEXT_4 = "newText4";
    private static String NEW_POST_PATH_IMAGE_4 = "newPathImage4";
    private static Long NEW_POST_AUTHOR_ID_4 = 2L;

    private static String UPDATED_POST_TITLE_1 = "updatedTestTitle1";
    private static String UPDATED_POST_TEXT_1 = "updatedTestText1";
    private static String UPDATED_POST_DESCRIPTION1 = "updatedTestDescription1";

    private static Post post = new Post(
            null,
            NEW_POST_TITLE_4,
            NEW_POST_DESCRIPTION_4,
            NEW_POST_TEXT_4,
            NEW_POST_PATH_IMAGE_4,
            NEW_POST_AUTHOR_ID_4
    );
    @Autowired
    private PostDao postDao;


    @Test
    public void getAllPosts() {
        List<Post> posts = postDao.getAllPosts();
        assertNotNull(posts);
        assertEquals(3, posts.size());
    }

    @Test
    public void getPostById() {
        Post post = postDao.getPostById(CORRECT_POST_ID);
        assertNotNull(post);
        assertEquals(CORRECT_POST_ID, post.getId());
    }




    @Test(expected = DataAccessException.class)
    public void getPostByIdWithException() {
        Post post = postDao.getPostById(INCORRECT_POST_ID);
        assertNotNull(post);
        assertEquals(CORRECT_POST_ID, post.getId());
    }

    @Test
    public void getAllPostsByAuthorId() {
        List<Post> posts = postDao.getAllPostsByAuthorId(CORRECT_USER_ID);
        assertNotNull(posts);
        assertEquals(2, posts.size());
    }

    @Test
    public void getPostsByInitialIdAndQuantity() {
        List<Post> posts = postDao.getPostsByInitialIdAndQuantity(2L, 2L);
        assertNotNull(posts);
        assertEquals(2, posts.size());
        assertEquals((Long) 2L, posts.get(0).getId());
        assertEquals((Long) 3L, posts.get(1).getId());
    }

    @Test
    public void getAllPostsByTagId() {
        List<Post> posts = postDao.getAllPostsByTagId(CORRECT_TAG_ID);
        assertNotNull(posts);
        assertEquals(2, posts.size());
    }

    @Test
    public void addPost() {
        List<Post> posts = postDao.getAllPosts();
        assertNotNull(posts);
        int initialSize = posts.size();

        Long newPostId = postDao.addPost(post);
        assertNotNull(newPostId);
        Post newPost = postDao.getPostById(newPostId);
        assertEquals(NEW_POST_ID_4, newPost.getId());
        assertEquals(NEW_POST_TITLE_4, newPost.getTitle());
        assertEquals(NEW_POST_DESCRIPTION_4, newPost.getDescription());
        assertEquals(NEW_POST_TEXT_4, newPost.getText());
        assertEquals(NEW_POST_PATH_IMAGE_4, newPost.getPathImage());
        assertEquals(NEW_POST_AUTHOR_ID_4, newPost.getAuthorId());

        posts = postDao.getAllPosts();
        assertNotNull(posts);
        assertEquals(initialSize + 1, posts.size());
    }

    @Test
    public void addTagToPost() {
        Post post = postDao.getPostById(CORRECT_POST_ID);
        assertNotNull(post);
        post.setDate(LocalDate.now());

        List<Post> posts = postDao.getAllPostsByTagId(CORRECT_TAG_ID);
        int initialSize = posts.size();

        assertEquals(1, postDao.addTagToPost(post.getId(), CORRECT_TAG_ID));
        posts = postDao.getAllPostsByTagId(CORRECT_TAG_ID);
        assertEquals(initialSize + 1, posts.size());
    }


    @Test
    public void updatePost() {
        Post post = postDao.getPostById(CORRECT_POST_ID);
        assertNotNull(post);
        post.setDate(LocalDate.now());

        post.setTitle(UPDATED_POST_TITLE_1);
        post.setText(UPDATED_POST_TEXT_1);
        post.setDescription(UPDATED_POST_DESCRIPTION1);

        assertEquals(1, postDao.updatePost(post));

        Post updatedPost = postDao.getPostById(CORRECT_POST_ID);
        assertNotNull(updatedPost);
        assertEquals(post, updatedPost);
    }

    @Test
    public void deletePost() {
        List<Post> posts = postDao.getAllPosts();
        assertNotNull(posts);
        int initialSize = posts.size();

        Post post = postDao.getPostById(CORRECT_POST_ID);
        assertNotNull(post);

        assertEquals(1, postDao.deletePost(CORRECT_POST_ID));
        assertEquals(0, postDao.deletePost(INCORRECT_POST_ID));

        posts = postDao.getAllPosts();
        assertNotNull(posts);
        assertEquals(initialSize - 1, posts.size());
    }

    @Test
    public void deleteTagInPost() {
        Post post = postDao.getPostById(CORRECT_POST_ID);
        assertNotNull(post);
        post.setDate(LocalDate.now());

        List<Post> posts = postDao.getAllPostsByTagId(CORRECT_TAG_ID_2);
        int initialSize = posts.size();

        assertEquals(1, postDao.deleteTagInPost(post.getId(), CORRECT_TAG_ID_2));
        posts = postDao.getAllPostsByTagId(CORRECT_TAG_ID_2);
        assertEquals(initialSize - 1, posts.size());
    }

    @Test
    public void checkTagInPostByIdReturnedTrue() {
        assertTrue(postDao.checkTagInPostById(CORRECT_POST_ID, CORRECT_TAG_ID_2));
    }

    @Test
    public void checkTagInPostByIdReturnedFalse() {
        assertFalse(postDao.checkTagInPostById(CORRECT_POST_ID, CORRECT_TAG_ID));
    }


    @Test
    public void checkPostByIdReturnedTrue() {
        assertTrue(postDao.checkPostById(CORRECT_POST_ID));

    }

    @Test
    public void checkPostByIdReturnedFalse() {
        assertFalse(postDao.checkPostById(INCORRECT_POST_ID));
    }

    @Test
    public void checkPostByAuthorIdReturnedTrue() {
        assertTrue(postDao.checkPostByAuthorId(CORRECT_USER_ID));
    }

    @Test
    public void checkPostByAuthorIdReturnedFalse() {
        assertFalse(postDao.checkPostByAuthorId(INCORRECT_USER_ID));

    }
}