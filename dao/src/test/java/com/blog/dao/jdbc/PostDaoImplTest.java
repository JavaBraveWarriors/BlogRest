package com.blog.dao.jdbc;

import com.blog.dao.PostDao;
import com.blog.dao.TagDao;
import com.blog.dto.TagDto;
import com.blog.model.Post;
import com.blog.model.ResponsePostDto;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * The Post dao impl test.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-spring-dao.xml"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PostDaoImplTest {

    private static final int ZERO_VALUE = 0;
    private static final String SORTED_FIELD = "id";
    private static final int COUNT_POSTS_IN_CORRECT_TAG = 3;
    private static final Long CORRECT_POST_ID = 1L;
    private static final Long CORRECT_POST_ID_2 = 5L;
    private static final Long CORRECT_SECOND_POST_ID = 2L;
    private static final Long CORRECT_THIRD_POST_ID = 3L;
    private static final Long CORRECT_FOURTH_POST_ID = 4L;
    private static final Long CORRECT_POST_CHECK_ID = 4L;
    private static final Long CORRECT_POST_DELETE_ID = 5L;
    private static final Long COUNT_NECESSARY_POSTS = 3L;

    private static final Long INCORRECT_POST_ID = 9L;
    private static final Long CORRECT_USER_ID = 1L;
    private static final Long COUNT_POSTS_IN_FIRST_USER = 2L;
    private static final Long INCORRECT_USER_ID = 9L;
    private static final Long CORRECT_TAG_ID = 3L;
    private static final Long CORRECT_TAG_ID_2 = 2L;
    private static final Long INCORRECT_TAG_ID = 5L;
    private static final Long INCORRECT_NEGATIVE_ID = -3L;

    private static final Long NEW_POST_ID = 6L;
    private static final String NEW_POST_TITLE = "newTitle4";
    private static final String NEW_POST_DESCRIPTION = "newDescription4";
    private static final String NEW_POST_TEXT = "newText4";
    private static final String NEW_POST_PATH_IMAGE = "newPathImage4";
    private static final Long NEW_POST_AUTHOR_ID = 2L;

    private static final String UPDATED_POST_TITLE = "updatedTestTitle1";
    private static final String UPDATED_POST_TEXT = "updatedTestText1";
    private static final String UPDATED_POST_DESCRIPTION = "updatedTestDescription1";
    private static List<Long> addedTagsToPost = new ArrayList<>();

    private static Post post = new Post(
            null,
            NEW_POST_TITLE,
            NEW_POST_DESCRIPTION,
            NEW_POST_TEXT,
            NEW_POST_PATH_IMAGE,
            NEW_POST_AUTHOR_ID
    );
    @Autowired
    private PostDao postDao;

    @Autowired
    private TagDao tagDao;

    @BeforeClass
    public static void setup() {
        addedTagsToPost.add(CORRECT_TAG_ID);
        addedTagsToPost.add(CORRECT_TAG_ID_2);
    }

    @Test
    public void getPostByIdSuccess() {
        Post post = postDao.getPostById(CORRECT_POST_ID);
        assertNotNull(post);
        assertEquals(CORRECT_POST_ID, post.getId());
    }

    @Test(expected = DataAccessException.class)
    public void getPostByIncorrectId() {
        assertNull(postDao.getPostById(INCORRECT_POST_ID));
    }

    @Test(expected = DataAccessException.class)
    public void getPostByNullId() {
        assertNull(postDao.getPostById(null));
    }

    @Test
    public void getAllPostsByAuthorIdSuccess() {
        List<ResponsePostDto> posts = postDao.getAllPostsByAuthorId(CORRECT_USER_ID);
        assertNotNull(posts);
        assertEquals(COUNT_POSTS_IN_FIRST_USER.intValue(), posts.size());
    }

    @Test
    public void getAllPostsByIncorrectAuthorId() {
        List<ResponsePostDto> posts = postDao.getAllPostsByAuthorId(INCORRECT_USER_ID);
        assertNotNull(posts);
        assertEquals(ZERO_VALUE, posts.size());
    }

    @Test
    public void getAllPostsByNullAuthorId() {
        List<ResponsePostDto> posts = postDao.getAllPostsByAuthorId(null);
        assertNotNull(posts);
        assertEquals(ZERO_VALUE, posts.size());
    }

    @Test
    public void addCommentSuccess() {
        ResponsePostDto newPost = postDao.getPostById(CORRECT_POST_ID);
        Long initialComments = newPost.getCommentsCount();

        postDao.addComment(CORRECT_POST_ID);

        newPost = postDao.getPostById(CORRECT_POST_ID);
        assertEquals(initialComments + 1L, newPost.getCommentsCount().longValue());
    }

    @Test
    public void deleteCommentSuccess() {
        ResponsePostDto newPost = postDao.getPostById(CORRECT_POST_ID);
        Long initialComments = newPost.getCommentsCount();

        postDao.deleteComment(CORRECT_POST_ID);

        newPost = postDao.getPostById(CORRECT_POST_ID);
        assertEquals(initialComments - 1L, newPost.getCommentsCount().longValue());
    }

    @Test
    public void getPostsByInitialIdAndQuantitySuccess() {
        List<ResponsePostDto> posts =
                postDao.getPostsByInitialIdAndQuantity(CORRECT_SECOND_POST_ID, COUNT_NECESSARY_POSTS);
        assertNotNull(posts);
        assertEquals(COUNT_NECESSARY_POSTS.intValue(), posts.size());
        assertEquals(CORRECT_SECOND_POST_ID, posts.get(0).getId());
        assertEquals(CORRECT_THIRD_POST_ID, posts.get(1).getId());
        assertEquals(CORRECT_FOURTH_POST_ID, posts.get(2).getId());
    }

    @Test
    public void getPostsByInitialIdAndQuantityWithSortSuccess() {
        List<ResponsePostDto> posts =
                postDao.getPostsByInitialIdAndQuantity(CORRECT_SECOND_POST_ID, COUNT_NECESSARY_POSTS, SORTED_FIELD);
        assertNotNull(posts);
        assertEquals(COUNT_NECESSARY_POSTS.intValue(), posts.size());
        assertEquals(CORRECT_FOURTH_POST_ID, posts.get(0).getId());
        assertEquals(CORRECT_THIRD_POST_ID, posts.get(1).getId());
        assertEquals(CORRECT_SECOND_POST_ID, posts.get(2).getId());
    }

    @Test(expected = NullPointerException.class)
    public void getPostsByInitialIdAndQuantityByNullNumbers() {
        assertNull(postDao.getPostsByInitialIdAndQuantity(null, null));
    }

    @Test
    public void getAllPostsByTagIdSuccess() {
        List<ResponsePostDto> posts = postDao.getAllPostsByTagId(CORRECT_TAG_ID);
        assertNotNull(posts);
        assertEquals(COUNT_POSTS_IN_CORRECT_TAG, posts.size());
    }

    @Test
    public void getAllPostsByNullTagId() {
        List<ResponsePostDto> posts = postDao.getAllPostsByTagId(null);
        assertNotNull(posts);
        assertEquals(ZERO_VALUE, posts.size());
    }

    @Test
    public void getAllPostsByIncorrectTagId() {
        List<ResponsePostDto> posts = postDao.getAllPostsByTagId(INCORRECT_NEGATIVE_ID);
        assertNotNull(posts);
        assertEquals(ZERO_VALUE, posts.size());
    }

    @Test
    public void addPostSuccess() {
        Long countOfPosts = postDao.getCountOfPosts();
        assertNotNull(countOfPosts);
        Long initialSize = countOfPosts;

        Long newPostId = postDao.addPost(post);
        assertNotNull(newPostId);
        Post newPost = postDao.getPostById(newPostId);
        assertEquals(NEW_POST_ID, newPost.getId());
        assertEquals(NEW_POST_TITLE, newPost.getTitle());
        assertEquals(NEW_POST_DESCRIPTION, newPost.getDescription());
        assertEquals(NEW_POST_TEXT, newPost.getText());
        assertEquals(NEW_POST_PATH_IMAGE, newPost.getPathImage());
        assertEquals(NEW_POST_AUTHOR_ID, newPost.getAuthorId());

        countOfPosts = postDao.getCountOfPosts();
        assertNotNull(countOfPosts);
        assertEquals(initialSize + 1L, countOfPosts.longValue());
    }

    @Test(expected = NullPointerException.class)
    public void addNullPost() {
        assertNull(postDao.addPost(null));
    }

    @Test
    public void addTagToPostSuccess() {
        Post post = postDao.getPostById(CORRECT_POST_ID);
        assertNotNull(post);
        post.setTimeOfCreation(LocalDateTime.now());

        List<ResponsePostDto> posts = postDao.getAllPostsByTagId(CORRECT_TAG_ID);
        int initialSize = posts.size();

        assertTrue(postDao.addTagToPost(post.getId(), CORRECT_TAG_ID));
        posts = postDao.getAllPostsByTagId(CORRECT_TAG_ID);
        assertEquals(initialSize + 1, posts.size());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void addIncorrectTagToPost() {
        Post post = postDao.getPostById(CORRECT_POST_ID);
        assertNotNull(post);
        post.setTimeOfCreation(LocalDateTime.now());
        assertTrue(postDao.addTagToPost(post.getId(), INCORRECT_NEGATIVE_ID));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void addNullTagToPost() {
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

        post.setTitle(UPDATED_POST_TITLE);
        post.setText(UPDATED_POST_TEXT);
        post.setDescription(UPDATED_POST_DESCRIPTION);

        assertTrue(postDao.updatePost(post));

        Post updatedPost = postDao.getPostById(CORRECT_POST_ID);
        assertNotNull(updatedPost);
        assertEquals(post.getTitle(), updatedPost.getTitle());
    }

    @Test(expected = NullPointerException.class)
    public void updateNullPost() {
        assertFalse(postDao.updatePost(null));
    }

    @Test
    public void updateIncorrectPost() {
        Post post = postDao.getPostById(CORRECT_POST_ID);
        assertNotNull(post);
        post.setTimeOfCreation(LocalDateTime.now());
        post.setId(INCORRECT_POST_ID);
        post.setTitle(UPDATED_POST_TITLE);
        post.setText(UPDATED_POST_TEXT);
        post.setDescription(UPDATED_POST_DESCRIPTION);

        assertFalse(postDao.updatePost(post));
    }

    @Test
    public void deletePostSuccess() {
        Long countOfPosts = postDao.getCountOfPosts();
        assertNotNull(countOfPosts);
        Long initialSize = countOfPosts;

        Post post = postDao.getPostById(CORRECT_POST_DELETE_ID);
        assertNotNull(post);

        assertTrue(postDao.deletePost(CORRECT_POST_DELETE_ID));

        countOfPosts = postDao.getCountOfPosts();
        assertNotNull(countOfPosts);
        assertEquals(initialSize - 1L, countOfPosts.longValue());
    }

    @Test
    public void deletePostByIncorrectId() {
        Long countOfPosts = postDao.getCountOfPosts();
        assertNotNull(countOfPosts);
        Long initialSize = countOfPosts;

        Post post = postDao.getPostById(CORRECT_POST_ID);
        assertNotNull(post);

        assertFalse(postDao.deletePost(INCORRECT_POST_ID));

        countOfPosts = postDao.getCountOfPosts();
        assertNotNull(countOfPosts);
        assertEquals(initialSize, countOfPosts);
    }

    @Test
    public void deletePostByNullId() {
        Long countOfPosts = postDao.getCountOfPosts();
        assertNotNull(countOfPosts);
        Long initialSize = countOfPosts;

        Post post = postDao.getPostById(CORRECT_POST_ID);
        assertNotNull(post);

        assertFalse(postDao.deletePost(null));

        countOfPosts = postDao.getCountOfPosts();
        assertNotNull(countOfPosts);
        assertEquals(initialSize, countOfPosts);
    }

    @Test
    public void addViewToPostSuccess() {
        ResponsePostDto postBeforeAddingView = postDao.getPostById(CORRECT_POST_ID);
        assertNotNull(postBeforeAddingView);

        postDao.addViewToPost(CORRECT_POST_ID);

        ResponsePostDto postAfterAddingView = postDao.getPostById(CORRECT_POST_ID);
        assertNotNull(postBeforeAddingView);

        assertEquals(postBeforeAddingView.getViewsCount() + 1L, postAfterAddingView.getViewsCount().longValue());
    }

    @Test
    public void addViewToIncorrectPost() {
        assertFalse(postDao.addViewToPost(INCORRECT_POST_ID));
    }

    @Test
    public void deleteTagInPostSuccess() {
        Post post = postDao.getPostById(CORRECT_POST_ID);
        assertNotNull(post);
        post.setTimeOfCreation(LocalDateTime.now());

        List<ResponsePostDto> posts = postDao.getAllPostsByTagId(CORRECT_TAG_ID_2);
        int initialSize = posts.size();

        assertTrue(postDao.deleteTagInPost(post.getId(), CORRECT_TAG_ID_2));
        posts = postDao.getAllPostsByTagId(CORRECT_TAG_ID_2);
        assertEquals(initialSize - 1, posts.size());
    }

    @Test
    public void deleteIncorrectTagInPost() {
        Post post = postDao.getPostById(CORRECT_POST_ID);
        assertNotNull(post);
        post.setTimeOfCreation(LocalDateTime.now());

        List<ResponsePostDto> posts = postDao.getAllPostsByTagId(CORRECT_TAG_ID_2);
        int initialSize = posts.size();

        assertFalse(postDao.deleteTagInPost(post.getId(), INCORRECT_NEGATIVE_ID));

        posts = postDao.getAllPostsByTagId(CORRECT_TAG_ID_2);
        assertEquals(initialSize, posts.size());
    }

    @Test
    public void deleteNullTagInPost() {
        Post post = postDao.getPostById(CORRECT_POST_ID);
        assertNotNull(post);
        post.setTimeOfCreation(LocalDateTime.now());

        List<ResponsePostDto> posts = postDao.getAllPostsByTagId(CORRECT_TAG_ID_2);
        int initialSize = posts.size();

        assertFalse(postDao.deleteTagInPost(post.getId(), null));

        posts = postDao.getAllPostsByTagId(CORRECT_TAG_ID_2);
        assertEquals(initialSize, posts.size());
    }

    @Test
    public void deleteAllTagsInPostSuccess() {
        List<TagDto> tagsBeforeDeleting = tagDao.getAllTagsByPostsId(Collections.singleton(CORRECT_POST_ID));
        assertNotNull(tagsBeforeDeleting);
        assertTrue(tagsBeforeDeleting.size() > 0);

        assertTrue(postDao.deleteAllTags(CORRECT_POST_ID));

        List<TagDto> tagsAfterDeleting = tagDao.getAllTagsByPostsId(Collections.singleton(CORRECT_POST_ID));
        assertNotNull(tagsAfterDeleting);
        assertEquals(ZERO_VALUE, tagsAfterDeleting.size());
    }

    @Test()
    public void deleteAllTagsInIncorrectPost() {
        assertFalse(postDao.deleteAllTags(INCORRECT_POST_ID));
    }

    @Test()
    public void deleteAllTagsInNullPost() {
        assertFalse(postDao.deleteAllTags(null));
    }

    @Test
    public void addTagsToPostSuccess() {
        List<TagDto> tagsBeforeAdding = tagDao.getAllTagsByPostsId(Collections.singleton(CORRECT_POST_ID_2));
        assertNotNull(tagsBeforeAdding);

        assertTrue(postDao.addTags(CORRECT_POST_ID_2, addedTagsToPost));

        List<TagDto> tagsAfterAdding = tagDao.getAllTagsByPostsId(Collections.singleton(CORRECT_POST_ID_2));
        assertNotNull(tagsAfterAdding);

        assertEquals(tagsBeforeAdding.size() + addedTagsToPost.size(), tagsAfterAdding.size());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void addTagsToIncorrectPost() {
        assertFalse(postDao.addTags(INCORRECT_POST_ID, addedTagsToPost));
    }

    @Test
    public void checkTagInPostByCorrectId() {
        assertTrue(postDao.checkTagInPostById(CORRECT_POST_CHECK_ID, CORRECT_TAG_ID));
    }

    @Test
    public void checkTagInPostByNotExistTagIdInPost() {
        assertFalse(postDao.checkTagInPostById(CORRECT_POST_CHECK_ID, CORRECT_TAG_ID_2));
    }

    @Test
    public void checkIncorrectTagInPostById() {
        assertFalse(postDao.checkTagInPostById(CORRECT_POST_CHECK_ID, INCORRECT_TAG_ID));
    }

    @Test
    public void checkIncorrectNegativeTagInPostById() {
        assertFalse(postDao.checkTagInPostById(CORRECT_POST_CHECK_ID, INCORRECT_NEGATIVE_ID));
    }

    @Test
    public void checkNullTagInPostById() {
        assertFalse(postDao.checkTagInPostById(CORRECT_POST_CHECK_ID, null));
    }

    @Test
    public void checkTagInNullPostById() {
        assertFalse(postDao.checkTagInPostById(null, CORRECT_TAG_ID));
    }

    @Test
    public void checkTagInIncorrectPostById() {
        assertFalse(postDao.checkTagInPostById(INCORRECT_POST_ID, CORRECT_TAG_ID));
    }

    @Test
    public void checkPostByCorrectId() {
        assertTrue(postDao.checkPostById(CORRECT_POST_ID));
    }

    @Test
    public void checkPostByIncorrectId() {
        assertFalse(postDao.checkPostById(INCORRECT_POST_ID));
    }

    @Test
    public void checkPostByNullId() {
        assertFalse(postDao.checkPostById(null));
    }

    @Test
    public void checkPostByIncorrectNegativeId() {
        assertFalse(postDao.checkPostById(INCORRECT_NEGATIVE_ID));
    }

    @Test
    public void checkPostByAuthorCorrectId() {
        assertTrue(postDao.checkPostByAuthorId(CORRECT_USER_ID));
    }

    @Test
    public void checkPostByAuthorIncorrectId() {
        assertFalse(postDao.checkPostByAuthorId(INCORRECT_USER_ID));
    }

    @Test
    public void checkPostByAuthorNullId() {
        assertFalse(postDao.checkPostByAuthorId(null));
    }

    @Test
    public void checkPostByAuthorIncorrectNegativeId() {
        assertFalse(postDao.checkPostByAuthorId(INCORRECT_NEGATIVE_ID));
    }
}