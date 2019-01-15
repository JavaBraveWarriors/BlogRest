package com.blog.dao.jdbc;

import com.blog.Post;
import com.blog.dao.PostDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:test-spring-dao.xml"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PostDaoImplTest {

    private static Long CORRECT_POST_ID = 1L;
    private static Long CORRECT_POST_CHECK_ID = 4L;
    private static Long CORRECT_POST_DELETE_ID = 5L;

    private static Long INCORRECT_POST_ID = 9L;
    private static Long CORRECT_USER_ID = 1L;
    private static Long INCORRECT_USER_ID = 9L;
    private static Long CORRECT_TAG_ID = 3L;
    private static Long CORRECT_TAG_ID_2 = 2L;
    private static Long INCORRECT_TAG_ID = 5L;
    private static Long INCORRECT_NEGATIVE_ID = -3L;

    private static Long NEW_POST_ID_4 = 6L;
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
    public void getAllPostsSuccess() {
        List<Post> posts = postDao.getAllPosts();
        assertNotNull(posts);
        assertEquals(5, posts.size());
    }

    @Test
    public void getPostByIdSuccess() {
        Post post = postDao.getPostById(CORRECT_POST_ID);
        assertNotNull(post);
        assertEquals(CORRECT_POST_ID, post.getId());
    }

    @Test(expected = DataAccessException.class)
    public void getPostByIdWithException1() {
        assertNull(postDao.getPostById(INCORRECT_POST_ID));
    }

    @Test(expected = DataAccessException.class)
    public void getPostByIdWithException2() {
        assertNull(postDao.getPostById(null));
    }

    @Test
    public void getAllPostsByAuthorIdSuccess() {
        List<Post> posts = postDao.getAllPostsByAuthorId(CORRECT_USER_ID);
        assertNotNull(posts);
        assertEquals(2, posts.size());
    }

    @Test
    public void getAllPostsByAuthorIdReturnedEmptyList1() {
        List<Post> posts = postDao.getAllPostsByAuthorId(INCORRECT_USER_ID);
        assertNotNull(posts);
        assertEquals(0, posts.size());
    }

    @Test
    public void getAllPostsByAuthorIdReturnedEmptyList2() {
        List<Post> posts = postDao.getAllPostsByAuthorId(null);
        assertNotNull(posts);
        assertEquals(0, posts.size());
    }

    @Test
    public void getPostsByInitialIdAndQuantitySuccess() {
        List<Post> posts = postDao.getPostsByInitialIdAndQuantity(2L, 3L);
        assertNotNull(posts);
        assertEquals(3, posts.size());
        assertEquals((Long) 2L, posts.get(0).getId());
        assertEquals((Long) 3L, posts.get(1).getId());
        assertEquals((Long) 4L, posts.get(2).getId());
    }

    @Test(expected = NullPointerException.class)
    public void getPostsByInitialIdAndQuantityWithNullPointerException() {
        assertNull(postDao.getPostsByInitialIdAndQuantity(null, null));
    }

    @Test
    public void getAllPostsByTagIdSuccess() {
        List<Post> posts = postDao.getAllPostsByTagId(CORRECT_TAG_ID);
        assertNotNull(posts);
        assertEquals(3, posts.size());
    }

    @Test
    public void getAllPostsByTagIdReturnedEmptyList1() {
        List<Post> posts = postDao.getAllPostsByTagId(null);
        assertNotNull(posts);
        assertEquals(0, posts.size());
    }

    @Test
    public void getAllPostsByTagIdReturnedEmptyList2() {
        List<Post> posts = postDao.getAllPostsByTagId(INCORRECT_NEGATIVE_ID);
        assertNotNull(posts);
        assertEquals(0, posts.size());
    }

    @Test
    public void addPostSuccess() {
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

    @Test(expected = NullPointerException.class)
    public void addPostWithNullPointerException1() {
        assertNull(postDao.addPost(null));
    }


    @Test
    public void addTagToPostSuccess() {
        Post post = postDao.getPostById(CORRECT_POST_ID);
        assertNotNull(post);
        post.setTimeOfCreation(LocalDateTime.now());

        List<Post> posts = postDao.getAllPostsByTagId(CORRECT_TAG_ID);
        int initialSize = posts.size();

        assertTrue(postDao.addTagToPost(post.getId(), CORRECT_TAG_ID));
        posts = postDao.getAllPostsByTagId(CORRECT_TAG_ID);
        assertEquals(initialSize + 1, posts.size());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void addTagToPostWithDataIntegrityViolationException1() {
        Post post = postDao.getPostById(CORRECT_POST_ID);
        assertNotNull(post);
        post.setTimeOfCreation(LocalDateTime.now());
        assertTrue(postDao.addTagToPost(post.getId(), INCORRECT_NEGATIVE_ID));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void addTagToPostWithDataIntegrityViolationException2() {
        Post post = postDao.getPostById(CORRECT_POST_ID);
        assertNotNull(post);
        post.setTimeOfCreation(LocalDateTime.now());
        assertTrue(postDao.addTagToPost(post.getId(), null));
    }

    @Test
    public void updatePostSuccess() {
        Post post = postDao.getPostById(CORRECT_POST_ID);
        assertNotNull(post);
        post.setTimeOfCreation(LocalDateTime.now());

        post.setTitle(UPDATED_POST_TITLE_1);
        post.setText(UPDATED_POST_TEXT_1);
        post.setDescription(UPDATED_POST_DESCRIPTION1);

        assertTrue(postDao.updatePost(post));

        Post updatedPost = postDao.getPostById(CORRECT_POST_ID);
        assertNotNull(updatedPost);
        assertEquals(post.getTitle(), updatedPost.getTitle());
    }

    @Test(expected = NullPointerException.class)
    public void updatePostWithNullPointerException() {
        assertFalse(postDao.updatePost(null));
    }

    @Test
    public void updatePostNotUpdated() {
        Post post = postDao.getPostById(CORRECT_POST_ID);
        assertNotNull(post);
        post.setTimeOfCreation(LocalDateTime.now());
        post.setId(INCORRECT_POST_ID);
        post.setTitle(UPDATED_POST_TITLE_1);
        post.setText(UPDATED_POST_TEXT_1);
        post.setDescription(UPDATED_POST_DESCRIPTION1);

        assertFalse(postDao.updatePost(post));
    }

    @Test
    public void deletePostSuccess() {
        List<Post> posts = postDao.getAllPosts();
        assertNotNull(posts);
        int initialSize = posts.size();

        Post post = postDao.getPostById(CORRECT_POST_DELETE_ID);
        assertNotNull(post);

        assertTrue(postDao.deletePost(CORRECT_POST_DELETE_ID));

        posts = postDao.getAllPosts();
        assertNotNull(posts);
        assertEquals(initialSize - 1, posts.size());
    }

    @Test
    public void deletePostNotDeleted() {
        List<Post> posts = postDao.getAllPosts();
        assertNotNull(posts);
        int initialSize = posts.size();

        Post post = postDao.getPostById(CORRECT_POST_ID);
        assertNotNull(post);

        assertFalse(postDao.deletePost(INCORRECT_POST_ID));
        assertFalse(postDao.deletePost(null));

        posts = postDao.getAllPosts();
        assertNotNull(posts);
        assertEquals(initialSize, posts.size());
    }

    @Test
    public void deleteTagInPostSuccess() {
        Post post = postDao.getPostById(CORRECT_POST_ID);
        assertNotNull(post);
        post.setTimeOfCreation(LocalDateTime.now());

        List<Post> posts = postDao.getAllPostsByTagId(CORRECT_TAG_ID_2);
        int initialSize = posts.size();

        assertTrue(postDao.deleteTagInPost(post.getId(), CORRECT_TAG_ID_2));
        posts = postDao.getAllPostsByTagId(CORRECT_TAG_ID_2);
        assertEquals(initialSize - 1, posts.size());
    }

    @Test
    public void deleteTagInPostNotDeleted() {
        Post post = postDao.getPostById(CORRECT_POST_ID);
        assertNotNull(post);
        post.setTimeOfCreation(LocalDateTime.now());

        List<Post> posts = postDao.getAllPostsByTagId(CORRECT_TAG_ID_2);
        int initialSize = posts.size();

        assertFalse(postDao.deleteTagInPost(post.getId(), INCORRECT_NEGATIVE_ID));
        assertFalse(postDao.deleteTagInPost(post.getId(), null));

        posts = postDao.getAllPostsByTagId(CORRECT_TAG_ID_2);
        assertEquals(initialSize, posts.size());
    }

    @Test
    public void checkTagInPostByIdReturnedTrue() {
        assertTrue(postDao.checkTagInPostById(CORRECT_POST_CHECK_ID, CORRECT_TAG_ID));
    }

    @Test
    public void checkTagInPostByIdReturnedFalse1() {
        assertFalse(postDao.checkTagInPostById(CORRECT_POST_CHECK_ID, CORRECT_TAG_ID_2));
    }

    @Test
    public void checkTagInPostByIdReturnedFalse2() {
        assertFalse(postDao.checkTagInPostById(CORRECT_POST_CHECK_ID, INCORRECT_TAG_ID));
    }

    @Test
    public void checkTagInPostByIdReturnedFalse3() {
        assertFalse(postDao.checkTagInPostById(CORRECT_POST_CHECK_ID, INCORRECT_NEGATIVE_ID));
    }

    @Test
    public void checkTagInPostByIdReturnedFalse4() {
        assertFalse(postDao.checkTagInPostById(CORRECT_POST_CHECK_ID, null));
    }

    @Test
    public void checkTagInPostByIdReturnedFalse5() {
        assertFalse(postDao.checkTagInPostById(null, CORRECT_TAG_ID));
    }

    @Test
    public void checkTagInPostByIdReturnedFalse6() {
        assertFalse(postDao.checkTagInPostById(INCORRECT_POST_ID, CORRECT_TAG_ID));
    }

    @Test
    public void checkPostByIdReturnedTrue() {
        assertTrue(postDao.checkPostById(CORRECT_POST_ID));
    }

    @Test
    public void checkPostByIdReturnedFalse1() {
        assertFalse(postDao.checkPostById(INCORRECT_POST_ID));
    }

    @Test
    public void checkPostByIdReturnedFalse2() {
        assertFalse(postDao.checkPostById(null));
    }

    @Test
    public void checkPostByIdReturnedFalse3() {
        assertFalse(postDao.checkPostById(INCORRECT_NEGATIVE_ID));
    }

    @Test
    public void checkPostByAuthorIdReturnedTrue() {
        assertTrue(postDao.checkPostByAuthorId(CORRECT_USER_ID));
    }

    @Test
    public void checkPostByAuthorIdReturnedFalse1() {
        assertFalse(postDao.checkPostByAuthorId(INCORRECT_USER_ID));
    }

    @Test
    public void checkPostByAuthorIdReturnedFalse2() {
        assertFalse(postDao.checkPostByAuthorId(null));
    }

    @Test
    public void checkPostByAuthorIdReturnedFalse3() {
        assertFalse(postDao.checkPostByAuthorId(INCORRECT_NEGATIVE_ID));
    }
}