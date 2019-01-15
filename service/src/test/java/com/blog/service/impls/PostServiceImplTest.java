package com.blog.service.impls;

import com.blog.Post;
import com.blog.Tag;
import com.blog.dao.AuthorDao;
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
    private AuthorDao authorDao;

    @Mock
    private TagService tagService;

    @Mock
    private Validator validator;

    @Mock
    private PostDao postDao;

    @InjectMocks
    private PostServiceImpl postService;


    private static List<Post> testPosts = new ArrayList<>();
    private static List<Tag> tags = new ArrayList<>();
    private static List<Tag> tagUpdated = new ArrayList<>();

    private static Post testPost = new Post(
            1L,
            "testTitle",
            "testDescription",
            "testText",
            "",
            1L
    );

    @BeforeClass
    public static void setUp() {

        testPosts.add(new Post());
        testPosts.add(new Post());
        testPosts.add(new Post());
        testPosts.forEach(post -> post.setId(1L));
        tagUpdated.add(new Tag(2L, "2", "2"));
        tagUpdated.add(new Tag(3L, "3", "3"));
        tagUpdated.add(new Tag(5L, "5", "5"));
        tagUpdated.add(new Tag(6L, "6", "6"));
        tags.add(new Tag(1L, "1", "1"));
        tags.add(new Tag(2L, "2", "2"));
        tags.add(new Tag(3L, "3", "3"));
        testPost.setTags(tags);
    }

    @Test
    public void getAllPosts() {
        when(postDao.getAllPosts()).thenReturn(testPosts);
        List<Post> posts = postService.getAllPosts();
        verify(postDao, times(1)).getAllPosts();
        verify(tagService, times(3)).getAllTagsByPostId(anyLong());
        assertNotNull(posts);
        assertEquals(posts.size(), testPosts.size());
    }

    @Test
    public void getAllPostsByAuthorIdSuccess() {
        when(postDao.getAllPostsByAuthorId(anyLong())).thenReturn(testPosts);
        List<Post> posts = postService.getAllPostsByAuthorId(anyLong());
        assertNotNull(posts);
        assertEquals(posts.size(), testPosts.size());
        verify(validator, times(1)).validateAuthorId(anyLong());
        verify(postDao, times(1)).getAllPostsByAuthorId(anyLong());
        verify(tagService, times(3)).getAllTagsByPostId(anyLong());
    }

    @Test(expected = ValidationException.class)
    public void getAllPostsByAuthorIdWithValidationException() {
        doThrow(ValidationException.class).when(validator).validateAuthorId(anyLong());
        postService.getAllPostsByAuthorId(anyLong());
        verify(validator, times(1)).validateAuthorId(anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void getAllPostsByAuthorIdWithNotFoundException() {
        doThrow(NotFoundException.class).when(validator).validateAuthorId(anyLong());
        postService.getAllPostsByAuthorId(anyLong());
        verify(validator, times(1)).validateAuthorId(anyLong());

    }

    @Test
    public void getPostsByInitialIdAndQuantitySuccess() {
        when(postDao.getPostsByInitialIdAndQuantity(anyLong(), anyLong())).thenReturn(testPosts);
        postService.getPostsByInitialIdAndQuantity(anyLong(), anyLong());
        verify(postDao, times(1)).getPostsByInitialIdAndQuantity(anyLong(), anyLong());
        verify(tagService, times(testPosts.size())).getAllTagsByPostId(anyLong());
        verify(validator, times(1)).validateInitialAndQuantity(anyLong(), anyLong());
    }

    @Test(expected = ValidationException.class)
    public void getPostsByInitialIdAndQuantityWithValidationException() {
        doThrow(ValidationException.class).when(validator).validateInitialAndQuantity(anyLong(), anyLong());
        postService.getPostsByInitialIdAndQuantity(anyLong(), anyLong());
        verify(validator, times(1)).validateInitialAndQuantity(anyLong(), anyLong());
    }


    @Test
    public void getAllPostsByTagIdSuccess() {
        when(postDao.getAllPostsByTagId(anyLong())).thenReturn(testPosts);
        List<Post> posts = postService.getAllPostsByTagId(anyLong());
        assertNotNull(posts);
        assertEquals(posts.size(), testPosts.size());
        verify(postDao, times(1)).getAllPostsByTagId(anyLong());
        verify(tagService, times(testPosts.size())).getAllTagsByPostId(anyLong());
        verify(validator, times(1)).validateTagId(anyLong());
    }

    @Test(expected = ValidationException.class)
    public void getAllPostsByTagIdWithValidationException() {
        doThrow(ValidationException.class).when(validator).validateTagId(anyLong());
        postService.getAllPostsByTagId(anyLong());
        verify(validator, times(1)).validateTagId(anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void getAllPostsByTagIdWithNotFoundException() {
        doThrow(NotFoundException.class).when(validator).validateTagId(anyLong());
        postService.getAllPostsByTagId(anyLong());
        verify(validator, times(1)).validateTagId(anyLong());
    }


    @Test
    public void getPostByIdSuccess() {
        when(postDao.getPostById(anyLong())).thenReturn(testPost);
        Post post = postService.getPostById(anyLong());
        assertNotNull(post);
        assertEquals(post.getTitle(), testPost.getTitle());
        verify(postDao, times(1)).getPostById(anyLong());
        verify(tagService, times(1)).getAllTagsByPostId(anyLong());
        verify(validator, times(1)).validatePostId(anyLong());
    }

    @Test(expected = ValidationException.class)
    public void getPostByIdWithValidationException() {
        doThrow(ValidationException.class).when(validator).validatePostId(anyLong());
        postService.getPostById(anyLong());
        verify(validator, times(1)).validatePostId(anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void getPostByIdWithNotFoundException() {
        doThrow(NotFoundException.class).when(validator).validatePostId(anyLong());
        postService.getPostById(anyLong());
        verify(validator, times(1)).validatePostId(anyLong());
    }


    @Test
    public void addPostSuccess() {
        when(postDao.addPost(any(Post.class))).thenReturn(2L);
        assertEquals((Long) 2L, postService.addPost(testPost));
        verify(postDao, times(1)).addPost(any(Post.class));
        verify(validator, times(testPost.getTags().size())).validateTagId(anyLong());
        verify(validator, times(1)).validateAuthorId(anyLong());
        verify(postDao, times(testPost.getTags().size())).addTagToPost(anyLong(), anyLong());
    }

    @Test(expected = ValidationException.class)
    public void addPostWithValidationException1() {
        doThrow(ValidationException.class).when(validator).validateAuthorId(anyLong());
        postService.addPost(testPost);
        verify(validator, times(1)).validateAuthorId(anyLong());
    }

    @Test(expected = ValidationException.class)
    public void addPostWithValidationException2() {
        doThrow(ValidationException.class).when(validator).validateTagId(anyLong());
        when(postDao.addPost(any(Post.class))).thenReturn(anyLong());
        postService.addPost(testPost);
        verify(postDao, times(1)).addPost(any(Post.class));
        verify(validator, times(1)).validateTagId(anyLong());
        verify(validator, times(1)).validateAuthorId(anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void addPostWithNotFoundException() {
        doThrow(NotFoundException.class).when(validator).validateAuthorId(anyLong());
        postService.addPost(testPost);
        verify(validator, times(1)).validateAuthorId(anyLong());
    }

    @Test
    public void addTagToPostSuccess() {
        postService.addTagToPost(1L, 1L);
        verify(postDao, times(1)).addTagToPost(anyLong(), anyLong());
        verify(validator, times(1)).validateTagId(anyLong());
        verify(validator, times(1)).validatePostId(anyLong());
        verify(validator, times(1)).validateTagInPost(anyLong(), anyLong());
    }

    @Test(expected = ValidationException.class)
    public void addTagToPostValidationException1() {
        doThrow(ValidationException.class).when(validator).validatePostId(anyLong());
        postService.addTagToPost(1L, 1L);
        verify(validator, times(1)).validatePostId(anyLong());
    }

    @Test(expected = ValidationException.class)
    public void addTagToPostValidationException2() {
        doThrow(ValidationException.class).when(validator).validateTagId(anyLong());
        postService.addTagToPost(1L, 1L);
        verify(validator, times(1)).validatePostId(anyLong());
        verify(validator, times(1)).validateTagId(anyLong());
    }

    @Test(expected = ValidationException.class)
    public void addTagToPostValidationException3() {
        doThrow(ValidationException.class).when(validator).validateTagInPost(anyLong(), anyLong());
        postService.addTagToPost(1L, 1L);
        verify(validator, times(1)).validatePostId(anyLong());
        verify(validator, times(1)).validateTagInPost(anyLong(), anyLong());
        verify(validator, times(1)).validateTagId(anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void addTagToPostNotFoundException1() {
        doThrow(NotFoundException.class).when(validator).validatePostId(anyLong());
        postService.addTagToPost(1L, 1L);
        verify(validator, times(1)).validatePostId(anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void addTagToPostNotFoundException2() {
        doThrow(NotFoundException.class).when(validator).validateTagId(anyLong());
        postService.addTagToPost(1L, 1L);
        verify(validator, times(1)).validatePostId(anyLong());
        verify(validator, times(1)).validateTagId(anyLong());
    }


    @Test
    public void updatePostSuccess() {
        testPost.setTags(tagUpdated);
        when(postDao.updatePost(any(Post.class))).thenReturn(true);
        when(postDao.checkTagInPostById(anyLong(), eq(2L))).thenReturn(true);
        when(postDao.checkTagInPostById(anyLong(), eq(3L))).thenReturn(true);
        when(postDao.checkTagInPostById(anyLong(), eq(5L))).thenReturn(false);
        when(postDao.checkTagInPostById(anyLong(), eq(6L))).thenReturn(false);
        when(postDao.addTagToPost(anyLong(), anyLong())).thenReturn(true);
        when(postDao.deleteTagInPost(anyLong(), anyLong())).thenReturn(true);
        when(tagService.getAllTagsByPostId(anyLong())).thenReturn(tags);
        postService.updatePost(testPost);
        verify(postDao, times(1)).updatePost(any(Post.class));
        verify(validator, times(1)).checkPost(any(Post.class));
        verify(tagService, times(1)).getAllTagsByPostId(anyLong());
        verify(postDao, times(1)).deleteTagInPost(anyLong(), anyLong());
        verify(postDao, times(2)).addTagToPost(anyLong(), anyLong());
        verify(validator, times(1)).validateTags(anyList(), any(TagService.class));
    }

    @Test(expected = InternalServerException.class)
    public void updatePostWithInternalServerException1() {
        testPost.setTags(tagUpdated);
        when(postDao.updatePost(any(Post.class))).thenReturn(false);
        when(postDao.checkTagInPostById(anyLong(), eq(2L))).thenReturn(true);
        when(postDao.checkTagInPostById(anyLong(), eq(3L))).thenReturn(true);
        when(postDao.checkTagInPostById(anyLong(), eq(5L))).thenReturn(false);
        when(postDao.checkTagInPostById(anyLong(), eq(6L))).thenReturn(false);
        when(postDao.addTagToPost(anyLong(), anyLong())).thenReturn(true);
        when(postDao.deleteTagInPost(anyLong(), anyLong())).thenReturn(true);
        when(tagService.getAllTagsByPostId(anyLong())).thenReturn(tags);
        postService.updatePost(testPost);
        verify(postDao, times(1)).updatePost(any(Post.class));
        verify(validator, times(1)).checkPost(any(Post.class));
        verify(tagService, times(1)).getAllTagsByPostId(anyLong());
        verify(postDao, times(1)).deleteTagInPost(anyLong(), anyLong());
        verify(postDao, times(2)).addTagToPost(anyLong(), anyLong());
        verify(validator, times(1)).validateTags(anyList(), any(TagService.class));
    }

    @Test(expected = InternalServerException.class)
    public void updatePostWithInternalServerException2() {
        testPost.setTags(tagUpdated);
        when(tagService.getAllTagsByPostId(anyLong())).thenReturn(tags);
        postService.updatePost(testPost);
        verify(postDao, times(1)).updatePost(any(Post.class));
        verify(validator, times(1)).checkPost(any(Post.class));
        verify(tagService, times(1)).getAllTagsByPostId(anyLong());
        verify(postDao, times(1)).deleteTagInPost(anyLong(), anyLong());
        verify(postDao, times(2)).addTagToPost(anyLong(), anyLong());
        verify(validator, times(1)).validateTags(anyList(), any(TagService.class));
    }

    @Test(expected = InternalServerException.class)
    public void updatePostWithInternalServerException3() {
        testPost.setTags(tagUpdated);
        when(postDao.deleteTagInPost(anyLong(), anyLong())).thenReturn(false);
        when(tagService.getAllTagsByPostId(anyLong())).thenReturn(tags);
        postService.updatePost(testPost);
        verify(postDao, times(1)).updatePost(any(Post.class));
        verify(validator, times(1)).checkPost(any(Post.class));
        verify(tagService, times(1)).getAllTagsByPostId(anyLong());
        verify(postDao, times(1)).deleteTagInPost(anyLong(), anyLong());
        verify(postDao, times(2)).addTagToPost(anyLong(), anyLong());
        verify(validator, times(1)).validateTags(anyList(), any(TagService.class));
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