package com.blog.service.impls;

import com.blog.*;
import com.blog.dao.PostDao;
import com.blog.exception.InternalServerException;
import com.blog.exception.NotFoundException;
import com.blog.exception.ValidationException;
import com.blog.service.TagService;
import com.blog.validator.Validator;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PostServiceImplTest {

    @Mock
    private TagService tagService;

    @Mock
    private Validator validator;

    @Mock
    private PostDao postDao;

    @InjectMocks
    private PostServiceImpl postService;

    private static List<PostForGet> testPosts = new ArrayList<>();
    private static List<Tag> tags = new ArrayList<>();
    private static List<Long> tagUpdated;

    private static PostForAdd testPostForAdd = new PostForAdd(
            1L,
            "testTitle",
            "testDescription",
            "testText",
            "",
            1L
    );
    private static PostForGet testPostGet = new PostForGet(
            1L,
            "testTitle",
            "testDescription",
            "testText",
            "",
            1L
    );

    private static Author author = new Author(
            1L,
            "test@mail.ru",
            "testLogin",
            "testPsw",
            "testFirstName",
            "testLastName",
            "testDescription",
            "testPhone");

    @BeforeClass
    public static void setUp() {

        testPosts.add(new PostForGet());
        testPosts.add(new PostForGet());
        testPosts.add(new PostForGet());
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
        testPostGet.setTags(tags);
    }

    @Test
    public void getAllPostsByAuthorIdSuccess() {
        when(postDao.getAllPostsByAuthorId(anyLong())).thenReturn(testPosts);
        List<PostForGet> posts = postService.getAllPostsByAuthorId(anyLong());
        assertNotNull(posts);
        assertEquals(posts.size(), testPosts.size());
        verify(validator, times(1)).validateAuthorId(anyLong());
        verify(postDao, times(1)).getAllPostsByAuthorId(anyLong());
        verify(tagService, times(3)).getAllTagsByPostId(anyLong());
    }

    @Test(expected = ValidationException.class)
    public void getAllPostsByAuthorIncorrectId() {
        doThrow(ValidationException.class).when(validator).validateAuthorId(anyLong());
        postService.getAllPostsByAuthorId(anyLong());
        verify(validator, times(1)).validateAuthorId(anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void getAllPostsByNotExistAuthorId() {
        doThrow(NotFoundException.class).when(validator).validateAuthorId(anyLong());
        postService.getAllPostsByAuthorId(anyLong());
        verify(validator, times(1)).validateAuthorId(anyLong());
    }

    @Test
    public void getPostsWithPaginationSuccess() {
        when(postDao.getPostsByInitialIdAndQuantity(anyLong(), anyLong(), anyString())).thenReturn(testPosts);
        postService.getPostsWithPaginationAndSorting(anyLong(), anyLong(), "created_date");
        verify(postDao, times(1)).getPostsByInitialIdAndQuantity(anyLong(), anyLong(), anyString());
        verify(tagService, times(testPosts.size())).getAllTagsByPostId(anyLong());
        verify(validator, times(1)).validatePageAndSize(anyLong(), anyLong());
    }

    @Test(expected = ValidationException.class)
    public void getPostsWithPaginationByIncorrectSize() {
        doThrow(ValidationException.class).when(validator).validatePageAndSize(anyLong(), anyLong());
        postService.getPostsWithPaginationAndSorting(anyLong(), anyLong(), "created_date");
        verify(validator, times(1)).validatePageAndSize(anyLong(), anyLong());
    }

    @Test
    public void getAllPostsByTagIdSuccess() {
        when(postDao.getAllPostsByTagId(anyLong())).thenReturn(testPosts);
        List<PostForGet> posts = postService.getAllPostsByTagId(anyLong());
        assertNotNull(posts);
        assertEquals(posts.size(), testPosts.size());
        verify(postDao, times(1)).getAllPostsByTagId(anyLong());
        verify(tagService, times(testPosts.size())).getAllTagsByPostId(anyLong());
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
        Post post = postService.getPostById(anyLong());
        assertNotNull(post);
        assertEquals(post.getTitle(), testPostForAdd.getTitle());
        verify(postDao, times(1)).getPostById(anyLong());
        verify(tagService, times(1)).getAllTagsByPostId(anyLong());
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
        assertEquals((Long) 2L, postService.addPost(testPostForAdd));
        verify(postDao, times(1)).addPost(any(Post.class));
        verify(validator, times(testPostForAdd.getTags().size())).validateTagId(anyLong());
        verify(validator, times(1)).validateAuthorId(anyLong());
        verify(postDao, times(testPostForAdd.getTags().size())).addTagToPost(anyLong(), anyLong());
    }

    @Test(expected = ValidationException.class)
    public void addPostWithIncorrectAuthorId() {
        doThrow(ValidationException.class).when(validator).validateAuthorId(anyLong());
        postService.addPost(testPostForAdd);
        verify(validator, times(1)).validateAuthorId(anyLong());
    }

    @Test(expected = ValidationException.class)
    public void addPostWithIncorrectTag() {
        doThrow(ValidationException.class).when(validator).validateTagId(anyLong());
        when(postDao.addPost(any(Post.class))).thenReturn(anyLong());
        postService.addPost(testPostForAdd);
        verify(postDao, times(1)).addPost(any(Post.class));
        verify(validator, times(1)).validateTagId(anyLong());
        verify(validator, times(1)).validateAuthorId(anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void addPostWithNotExistAuthor() {
        doThrow(NotFoundException.class).when(validator).validateAuthorId(anyLong());
        postService.addPost(testPostForAdd);
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

    @Test
    public void updatePostSuccess() {
        testPostForAdd.setTags(tagUpdated);
        when(postDao.updatePost(any(Post.class))).thenReturn(true);
        when(postDao.checkTagInPostById(anyLong(), eq(2L))).thenReturn(true);
        when(postDao.checkTagInPostById(anyLong(), eq(3L))).thenReturn(true);
        when(postDao.checkTagInPostById(anyLong(), eq(6L))).thenReturn(false);
        when(postDao.addTagToPost(anyLong(), anyLong())).thenReturn(true);
        when(postDao.deleteTagInPost(anyLong(), anyLong())).thenReturn(true);
        when(tagService.getAllTagsByPostId(anyLong())).thenReturn(tags);
        postService.updatePost(testPostForAdd);
        verify(postDao, times(1)).updatePost(any(Post.class));
        verify(validator, times(1)).checkPost(any(Post.class));
        verify(tagService, times(1)).getAllTagsByPostId(anyLong());
        verify(postDao, times(1)).deleteTagInPost(anyLong(), anyLong());
        verify(postDao, times(2)).addTagToPost(anyLong(), anyLong());
        verify(validator, times(1)).validateTags(anyList());
    }

    @Test(expected = InternalServerException.class)
    public void updatePostWithNotUpdatedInDao() {
        testPostForAdd.setTags(tagUpdated);
        when(postDao.updatePost(any(Post.class))).thenReturn(false);
        when(postDao.checkTagInPostById(anyLong(), eq(2L))).thenReturn(true);
        when(postDao.checkTagInPostById(anyLong(), eq(3L))).thenReturn(true);
        when(postDao.checkTagInPostById(anyLong(), eq(6L))).thenReturn(false);
        when(postDao.addTagToPost(anyLong(), anyLong())).thenReturn(true);
        when(postDao.deleteTagInPost(anyLong(), anyLong())).thenReturn(true);
        when(tagService.getAllTagsByPostId(anyLong())).thenReturn(tags);
        postService.updatePost(testPostForAdd);
        verify(postDao, times(1)).updatePost(any(Post.class));
        verify(validator, times(1)).checkPost(any(Post.class));
        verify(tagService, times(1)).getAllTagsByPostId(anyLong());
        verify(postDao, times(1)).deleteTagInPost(anyLong(), anyLong());
        verify(postDao, times(2)).addTagToPost(anyLong(), anyLong());
        verify(validator, times(1)).validateTags(anyList());
    }

    @Test(expected = InternalServerException.class)
    public void updatePostWithNotAddedTagToPost() {
        testPostForAdd.setTags(tagUpdated);
        when(tagService.getAllTagsByPostId(anyLong())).thenReturn(tags);
        when(postDao.deleteTagInPost(anyLong(), anyLong())).thenReturn(true);
        when(postDao.addTagToPost(anyLong(), anyLong())).thenReturn(false);

        postService.updatePost(testPostForAdd);

        verify(postDao, times(1)).updatePost(any(Post.class));
        verify(validator, times(1)).checkPost(any(Post.class));
        verify(tagService, times(1)).getAllTagsByPostId(anyLong());
        verify(postDao, times(1)).deleteTagInPost(anyLong(), anyLong());
        verify(postDao, times(2)).addTagToPost(anyLong(), anyLong());
        verify(validator, times(1)).validateTags(anyList());
    }

    @Test(expected = InternalServerException.class)
    public void updatePostWithNotDeletedTagFromPost() {
        testPostForAdd.setTags(tagUpdated);
        when(postDao.deleteTagInPost(anyLong(), anyLong())).thenReturn(false);
        when(tagService.getAllTagsByPostId(anyLong())).thenReturn(tags);
        postService.updatePost(testPostForAdd);
        verify(postDao, times(1)).updatePost(any(Post.class));
        verify(validator, times(1)).checkPost(any(Post.class));
        verify(tagService, times(1)).getAllTagsByPostId(anyLong());
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