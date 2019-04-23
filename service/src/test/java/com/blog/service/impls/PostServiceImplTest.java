package com.blog.service.impls;

import com.blog.dao.PostDao;
import com.blog.dao.ViewDao;
import com.blog.dto.TagDto;
import com.blog.exception.InternalServerException;
import com.blog.exception.NotFoundException;
import com.blog.exception.ValidationException;
import com.blog.model.*;
import com.blog.service.CommentService;
import com.blog.service.PostService;
import com.blog.service.TagService;
import com.blog.service.impls.config.ServiceTestConfiguration;
import com.blog.validator.Validator;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * The Post service impl test.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ServiceTestConfiguration.class)
public class PostServiceImplTest {

    private static final Long CORRECT_TAG_ID = 12L;
    private static final Long INCORRECT_TAG_ID = -14L;
    private static List<ResponsePostDto> testPosts = new ArrayList<>();
    private static List<Tag> tags = new ArrayList<>();
    private static List<Long> tagUpdated;
    private static final Long CORRECT_POST_ID = 1L;
    private static List<TagDto> postTags = new ArrayList<>();
    private static Comment correctComment;
    private static Comment incorrectComment;
    private static final String TEST_TEXT = "testText";
    private static final Long CORRECT_AUTHOR_ID = 21L;
    private static final Long CORRECT_COMMENT_ID = 13L;
    private static final Long INCORRECT_COMMENT_ID = -123L;

    private static RequestPostDto testRequestPostDto = new RequestPostDto(
            1L,
            TEST_TEXT,
            TEST_TEXT,
            TEST_TEXT,
            "",
            1L
    );
    private static ResponsePostDto testPostGet = new ResponsePostDto(
            1L,
            TEST_TEXT,
            TEST_TEXT,
            TEST_TEXT,
            "",
            1L
    );

    @Autowired
    @Qualifier("tagServiceMock")
    private TagService tagService;

    @Autowired
    @Qualifier("mockValidator")
    private Validator validator;

    @Autowired
    private ViewDao viewDao;

    @Autowired
    @Qualifier("commentServiceMock")
    private CommentService commentService;

    @Autowired
    private PostDao postDao;

    @Autowired
    @Qualifier("testPostService")
    private PostService postService;

    @After
    public void updateData() {
        Mockito.reset(tagService, validator, viewDao, commentService, postDao);
    }

    @BeforeClass
    public static void setUp() {
        testPosts.add(new ResponsePostDto());
        testPosts.add(new ResponsePostDto());
        testPosts.add(new ResponsePostDto());
        testPosts.forEach(post -> post.setId(1L));
        testPosts.forEach(post -> post.setAuthorId(1L));

        tagUpdated = new ArrayList<>();
        tagUpdated.add(2L);
        tagUpdated.add(3L);
        tagUpdated.add(4L);
        tagUpdated.add(6L);

        tags.add(new Tag(1L, "1", "1"));
        tags.add(new Tag(2L, "2", "2"));
        tags.add(new Tag(3L, "3", "3"));

        postTags.add(new TagDto(CORRECT_POST_ID, new Tag(1L, "1", "1")));
        postTags.add(new TagDto(CORRECT_POST_ID, new Tag(2L, "2", "2")));
        postTags.add(new TagDto(CORRECT_POST_ID, new Tag(3L, "3", "3")));

        testPostGet.setTags(tags);

        correctComment = new Comment(
                TEST_TEXT,
                CORRECT_AUTHOR_ID,
                CORRECT_POST_ID);
        incorrectComment = new Comment(TEST_TEXT, CORRECT_AUTHOR_ID, null);
    }

    @Test
    public void getAllPostsByAuthorIdSuccess() {
        when(postDao.getAllPostsByAuthorId(anyLong())).thenReturn(testPosts);
        PostListWrapper posts = postService.getAllPostsByAuthorId(anyLong());
        assertNotNull(posts);
        assertEquals(posts.getPosts().size(), testPosts.size());
        verify(validator, times(1)).validateAuthorId(anyLong());
        verify(postDao, times(1)).getAllPostsByAuthorId(anyLong());
        verify(tagService, times(1)).getAllTagsByPostsId(any());
    }

    @Test(expected = ValidationException.class)
    public void getAllPostsByAuthorIncorrectId() {
        doThrow(ValidationException.class).when(validator).validateAuthorId(anyLong());
        postService.getAllPostsByAuthorId(anyLong());
        verify(validator, times(1)).validateAuthorId(anyLong());
        verifyNoMoreInteractions(validator);
    }

    @Test(expected = NotFoundException.class)
    public void getAllPostsByNotExistAuthorId() {
        doThrow(NotFoundException.class).when(validator).validateAuthorId(anyLong());
        postService.getAllPostsByAuthorId(anyLong());
        verify(validator, times(1)).validateAuthorId(anyLong());
        verifyNoMoreInteractions(validator);
    }

    @Test
    public void getPostsWithPaginationSuccess() {
        when(postDao.getPostsByInitialIdAndQuantity(anyLong(), anyLong(), anyString())).thenReturn(testPosts);
        when(postDao.getCountOfPosts()).thenReturn(5L);
        PostListWrapper postListWrapper = postService.getPostsWithPaginationAndSorting(5L, 5L, "created_date");
        assertNotNull(postListWrapper);
        verify(postDao, times(1)).getPostsByInitialIdAndQuantity(anyLong(), anyLong(), anyString());
        verify(tagService, times(1)).getAllTagsByPostsId(any());
        verify(validator, times(1)).validatePageAndSize(anyLong(), anyLong());
    }

    @Test(expected = ValidationException.class)
    public void getPostsWithPaginationByIncorrectSize() {
        doThrow(ValidationException.class).when(validator).validatePageAndSize(anyLong(), anyLong());
        postService.getPostsWithPaginationAndSorting(anyLong(), anyLong(), "created_date");
        verify(validator, times(1)).validatePageAndSize(anyLong(), anyLong());
        verifyNoMoreInteractions(validator);
    }

    @Test
    public void addCorrectCommentToPost() {
        postService.addCommentToPost(correctComment);

        verify(commentService, times(1)).addComment(any(Comment.class));
        verify(postDao, times(1)).addComment(correctComment.getPostId());
        verifyNoMoreInteractions(commentService, postDao);
    }

    @Test(expected = ValidationException.class)
    public void addIncorrectCommentToPost() {
        doThrow(ValidationException.class).when(commentService).addComment(any(Comment.class));
        postService.addCommentToPost(incorrectComment);

        verify(commentService, times(1)).addComment(any(Comment.class));
        verify(postDao, times(0)).addComment(correctComment.getPostId());
        verifyNoMoreInteractions(commentService);
    }

    @Test
    public void deleteCorrectCommentInPost() {
        postService.deleteCommentInPost(CORRECT_POST_ID, CORRECT_COMMENT_ID);

        verify(commentService, times(1)).deleteComment(anyLong());
        verify(validator, times(1)).validateCommentInPost(anyLong(), anyLong());
        verify(postDao, times(1)).deleteComment(CORRECT_POST_ID);
        verifyNoMoreInteractions(commentService, validator, postDao);
    }

    @Test(expected = ValidationException.class)
    public void deleteIncorrectCommentInPost() {
        doThrow(ValidationException.class).when(validator).validateCommentInPost(anyLong(), anyLong());

        postService.deleteCommentInPost(CORRECT_POST_ID, INCORRECT_COMMENT_ID);

        verify(commentService, times(0)).deleteComment(anyLong());
        verify(validator, times(1)).validateCommentInPost(anyLong(), anyLong());
        verify(postDao, times(0)).deleteComment(anyLong());
        verifyNoMoreInteractions(validator);
    }

    @Test
    public void getAllPostsByTagIdSuccess() {
        when(postDao.getAllPostsByTagId(anyLong())).thenReturn(testPosts);
        PostListWrapper posts = postService.getAllPostsByTagId(anyLong());
        assertNotNull(posts);
        assertEquals(posts.getPosts().size(), testPosts.size());
        verify(postDao, times(1)).getAllPostsByTagId(anyLong());
        verify(tagService, times(1)).getAllTagsByPostsId(any());
        verify(validator, times(1)).validateTagId(anyLong());
    }

    @Test(expected = ValidationException.class)
    public void getAllPostsByIncorrectTagId() {
        doThrow(ValidationException.class).when(validator).validateTagId(anyLong());
        postService.getAllPostsByTagId(anyLong());
        verify(validator, times(1)).validateTagId(anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void getAllPostsByNotExistTagId() {
        doThrow(NotFoundException.class).when(validator).validateTagId(anyLong());
        postService.getAllPostsByTagId(anyLong());
        verify(validator, times(1)).validateTagId(anyLong());
    }

    @Test
    public void getPostByIdSuccess() {
        when(postDao.getPostById(anyLong())).thenReturn(testPostGet);
        //when(viewDao.checkViewByPostIdAndUserId(anyLong(), anyLong())).thenReturn(false);
        Post post = postService.getPostById(anyLong());
        assertNotNull(post);
        assertEquals(post.getTitle(), testRequestPostDto.getTitle());
        verify(postDao, times(1)).getPostById(anyLong());
        verify(tagService, times(1)).getAllTagsByPostsId(any());
        verify(validator, times(1)).validatePostId(anyLong());
    }

    @Test(expected = ValidationException.class)
    public void getPostByIncorrectId() {
        doThrow(ValidationException.class).when(validator).validatePostId(anyLong());
        postService.getPostById(anyLong());
        verify(validator, times(1)).validatePostId(anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void getPostByNotExistId() {
        doThrow(NotFoundException.class).when(validator).validatePostId(anyLong());
        postService.getPostById(anyLong());
        verify(validator, times(1)).validatePostId(anyLong());
    }

    @Test
    public void addPostSuccess() {
        when(postDao.addPost(any(Post.class))).thenReturn(2L);
        assertEquals((Long) 2L, postService.addPost(testRequestPostDto));
        verify(postDao, times(1)).addPost(any(Post.class));
        verify(validator, times(testRequestPostDto.getTags().size())).validateTagId(anyLong());
        verify(validator, times(1)).validateAuthorId(anyLong());
        verify(postDao, times(testRequestPostDto.getTags().size())).addTagToPost(anyLong(), anyLong());
    }

    @Test(expected = ValidationException.class)
    public void addPostWithIncorrectAuthorId() {
        doThrow(ValidationException.class).when(validator).validateAuthorId(anyLong());
        postService.addPost(testRequestPostDto);
        verify(validator, times(1)).validateAuthorId(anyLong());
    }

    @Test(expected = ValidationException.class)
    public void addPostWithIncorrectTag() {
        doThrow(ValidationException.class).when(validator).validateTagId(anyLong());
        when(postDao.addPost(any(Post.class))).thenReturn(anyLong());
        postService.addPost(testRequestPostDto);
        verify(postDao, times(1)).addPost(any(Post.class));
        verify(validator, times(1)).validateTagId(anyLong());
        verify(validator, times(1)).validateAuthorId(anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void addPostWithNotExistAuthor() {
        doThrow(NotFoundException.class).when(validator).validateAuthorId(anyLong());
        postService.addPost(testRequestPostDto);
        verify(validator, times(1)).validateAuthorId(anyLong());
    }

    @Test
    public void addTagToPostSuccess() {
        when(postDao.addTagToPost(anyLong(), anyLong())).thenReturn(true);
        postService.addTagToPost(1L, 1L);
        verify(postDao, times(1)).addTagToPost(anyLong(), anyLong());
        verify(validator, times(1)).validateTagId(anyLong());
        verify(validator, times(1)).validatePostId(anyLong());
        verify(validator, times(1)).validateTagInPost(anyLong(), anyLong());
    }

    @Test(expected = ValidationException.class)
    public void addTagToIncorrectPost() {
        doThrow(ValidationException.class).when(validator).validatePostId(anyLong());
        postService.addTagToPost(1L, 1L);
        verify(validator, times(1)).validatePostId(anyLong());
    }

    @Test(expected = ValidationException.class)
    public void addIncorrectTagToPost() {
        doThrow(ValidationException.class).when(validator).validateTagId(anyLong());
        postService.addTagToPost(1L, 1L);
        verify(validator, times(1)).validatePostId(anyLong());
        verify(validator, times(1)).validateTagId(anyLong());
    }

    @Test(expected = ValidationException.class)
    public void addExistenceInPostTagToPost() {
        doThrow(ValidationException.class).when(validator).validateTagInPost(anyLong(), anyLong());
        postService.addTagToPost(1L, 1L);
        verify(validator, times(1)).validatePostId(anyLong());
        verify(validator, times(1)).validateTagInPost(anyLong(), anyLong());
        verify(validator, times(1)).validateTagId(anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void addTagToNotExistPost() {
        doThrow(NotFoundException.class).when(validator).validatePostId(anyLong());
        postService.addTagToPost(1L, 1L);
        verify(validator, times(1)).validatePostId(anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void addNotExistTagToPost() {
        doThrow(NotFoundException.class).when(validator).validateTagId(anyLong());
        postService.addTagToPost(1L, 1L);
        verify(validator, times(1)).validatePostId(anyLong());
        verify(validator, times(1)).validateTagId(anyLong());
    }

    @Test(expected = InternalServerException.class)
    public void addTagToPostWithDataAccessException() {
        when(postDao.addTagToPost(anyLong(), anyLong())).thenReturn(false);
        postService.addTagToPost(1L, 1L);
        verify(validator, times(1)).validatePostId(anyLong());
        verify(validator, times(1)).validateTagId(anyLong());
    }

    @Test
    public void deleteCorrectTagInPost() {
        when(postDao.deleteTagInPost(anyLong(), anyLong())).thenReturn(true);
        postService.deleteTagInPost(CORRECT_POST_ID, CORRECT_TAG_ID);
        verify(validator, times(1)).validatePostId(anyLong());
        verify(validator, times(1)).validateTagId(anyLong());
        verify(validator, times(1)).validateTagInPost(anyLong(), anyLong());
    }

    @Test(expected = InternalServerException.class)
    public void deleteCorrectTagInPostWithNotDeleted() {
        when(postDao.deleteTagInPost(anyLong(), anyLong())).thenReturn(false);
        postService.deleteTagInPost(CORRECT_POST_ID, CORRECT_TAG_ID);
        verify(validator, times(1)).validatePostId(anyLong());
        verify(validator, times(1)).validateTagId(anyLong());
        verify(validator, times(1)).validateTagInPost(anyLong(), anyLong());
    }

    @Test(expected = ValidationException.class)
    public void deleteIncorrectTagInPost() {
        doThrow(ValidationException.class).when(validator).validateTagId(anyLong());
        postService.deleteTagInPost(CORRECT_POST_ID, INCORRECT_TAG_ID);
        verify(validator, times(1)).validatePostId(anyLong());
        verify(validator, times(1)).validateTagId(anyLong());
    }

    @Test
    public void updatePostSuccess() {
        testRequestPostDto.setTags(tagUpdated);
        when(postDao.updatePost(any(Post.class))).thenReturn(true);
        postService.updatePost(testRequestPostDto);
        verify(postDao, times(1)).updatePost(any(Post.class));
        verify(validator, times(1)).checkPost(any(Post.class));
        verify(postDao, times(1)).deleteAllTags(anyLong());
        verify(postDao, times(1)).addTags(anyLong(), any());
        verify(validator, times(1)).validateTags(anyList());
    }

    @Test(expected = InternalServerException.class)
    public void updatePostWithNotUpdatedInDao() {
        testRequestPostDto.setTags(tagUpdated);
        when(postDao.updatePost(any(Post.class))).thenReturn(false);
        postService.updatePost(testRequestPostDto);
        verify(postDao, times(1)).updatePost(any(Post.class));
        verify(validator, times(1)).checkPost(any(Post.class));
        verify(tagService, times(1)).getAllTagsByPostsId(any());
        verify(postDao, times(1)).deleteTagInPost(anyLong(), anyLong());
        verify(postDao, times(2)).addTagToPost(anyLong(), anyLong());
        verify(validator, times(1)).validateTags(anyList());
    }

    @Test(expected = InternalServerException.class)
    public void updatePostWithNotAddedTagToPost() {
        testRequestPostDto.setTags(tagUpdated);

        postService.updatePost(testRequestPostDto);

        verify(postDao, times(1)).updatePost(any(Post.class));
        verify(validator, times(1)).checkPost(any(Post.class));
        verify(tagService, times(1)).getAllTagsByPostsId(any());
        verify(postDao, times(1)).deleteTagInPost(anyLong(), anyLong());
        verify(postDao, times(2)).addTagToPost(anyLong(), anyLong());
        verify(validator, times(1)).validateTags(anyList());
    }

    @Test(expected = InternalServerException.class)
    public void updatePostWithNotDeletedTagFromPost() {
        testRequestPostDto.setTags(tagUpdated);
        postService.updatePost(testRequestPostDto);
        verify(postDao, times(1)).updatePost(any(Post.class));
        verify(validator, times(1)).checkPost(any(Post.class));
        verify(tagService, times(1)).getAllTagsByPostsId(any());
        verify(postDao, times(1)).deleteTagInPost(anyLong(), anyLong());
        verify(postDao, times(2)).addTagToPost(anyLong(), anyLong());
        verify(validator, times(1)).validateTags(anyList());
    }

    @Test
    public void deletePostSuccess() {
        when(postDao.deletePost(anyLong())).thenReturn(true);
        postService.deletePost(anyLong());
        verify(postDao, times(1)).deletePost(anyLong());
        verify(validator, times(1)).validatePostId(anyLong());
    }

    @Test(expected = InternalServerException.class)
    public void deletePostWithInternalServerException() {
        when(postDao.deletePost(anyLong())).thenReturn(false);
        postService.deletePost(anyLong());
        verify(postDao, times(1)).deletePost(anyLong());
        verify(validator, times(1)).validatePostId(anyLong());
    }
}