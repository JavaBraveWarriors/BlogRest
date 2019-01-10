package com.blog.service.impls;

import com.blog.Post;
import com.blog.dao.AuthorDao;
import com.blog.dao.PostDao;
import com.blog.exception.NotFoundException;
import com.blog.exception.ValidationException;
import com.blog.service.TagService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PostServiceImplTest {

    @Mock
    private AuthorDao authorDao;

    @Mock
    private TagService tagService;

    @Mock
    private PostDao postDao;

    @InjectMocks
    private PostServiceImpl postService;

    private static Long CORRECT_POST_ID = 1L;
    private static Long INCORRECT_POST_ID = -2L;
    private static Long CORRECT_AUTHOR_ID = 1L;
    private static Long INCORRECT_AUTHOR_ID = -2L;
    private static Long CORRECT_INITIAL_NUMBER = 1L;
    private static Long INCORRECT_INITIAL_NUMBER = -3L;
    private static Long CORRECT_QUANTITY_NUMBER = 1L;
    private static Long INCORRECT_QUANTITY_NUMBER = -3L;


    //private static ArrayList<Tag> tags = new ArrayList<>();
    private static ArrayList<Post> posts = new ArrayList<>();

    @BeforeClass
    public void setUp() {
/*        tags.add(new Tag());
        tags.add(new Tag());
        tags.add(new Tag());*/
        posts.add(new Post());
        posts.add(new Post());
        posts.add(new Post());
        posts.forEach(post -> post.setId(1L));
    }

    @Test
    public void getAllPosts() {
        when(postDao.getAllPosts()).thenReturn(posts);
        postService.getAllPosts();
        verify(postDao, times(1)).getAllPosts();
        verify(tagService, times(3)).getAllTagsByPostId(anyLong());
    }

    @Test
    public void getAllPostsByAuthorIdSuccess() {
        when(authorDao.checkAuthorById(anyLong())).thenReturn(true);
        when(postDao.getAllPostsByAuthorId(anyLong())).thenReturn(posts);
        postService.getAllPostsByAuthorId(CORRECT_AUTHOR_ID);
        verify(postDao, times(1)).getAllPostsByAuthorId(anyLong());
        verify(tagService, times(3)).getAllTagsByPostId(anyLong());
        verify(authorDao, times(1)).checkAuthorById(anyLong());
    }

    @Test(expected = ValidationException.class)
    public void getAllPostsByAuthorIdWithValidationException() {
        when(authorDao.checkAuthorById(anyLong())).thenReturn(true);
        when(postDao.getAllPostsByAuthorId(anyLong())).thenReturn(posts);
        postService.getAllPostsByAuthorId(INCORRECT_AUTHOR_ID);
        verify(postDao, never()).getAllPostsByAuthorId(anyLong());
        verify(tagService, never()).getAllTagsByPostId(anyLong());
        verify(authorDao, never()).checkAuthorById(anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void getAllPostsByAuthorIdWithNotFoundException() {
        when(authorDao.checkAuthorById(anyLong())).thenReturn(false);
        when(postDao.getAllPostsByAuthorId(anyLong())).thenReturn(posts);
        postService.getAllPostsByAuthorId(CORRECT_AUTHOR_ID);
        verify(postDao, never()).getAllPostsByAuthorId(anyLong());
        verify(tagService, never()).getAllTagsByPostId(anyLong());
        verify(authorDao, times(1)).checkAuthorById(anyLong());
    }

    @Test
    public void getPostsByInitialIdAndQuantitySuccess() {
        when(postDao.getPostsByInitialIdAndQuantity(anyLong(), anyLong())).thenReturn(posts);
        postService.getPostsByInitialIdAndQuantity(CORRECT_INITIAL_NUMBER, CORRECT_QUANTITY_NUMBER);
        verify(postDao, times(1)).getPostsByInitialIdAndQuantity(anyLong(), anyLong());
        verify(tagService, times(3)).getAllTagsByPostId(anyLong());
    }

    @Test(expected = ValidationException.class)
    public void getPostsByInitialIdAndQuantityWithValidationException_1() {
        when(postDao.getPostsByInitialIdAndQuantity(anyLong(), anyLong())).thenReturn(posts);
        postService.getPostsByInitialIdAndQuantity(INCORRECT_INITIAL_NUMBER, CORRECT_QUANTITY_NUMBER);
        verify(postDao, never()).getPostsByInitialIdAndQuantity(anyLong(), anyLong());
        verify(tagService, never()).getAllTagsByPostId(anyLong());
    }

    @Test(expected = ValidationException.class)
    public void getPostsByInitialIdAndQuantityWithValidationException_2() {
        when(postDao.getPostsByInitialIdAndQuantity(anyLong(), anyLong())).thenReturn(posts);
        postService.getPostsByInitialIdAndQuantity(CORRECT_INITIAL_NUMBER, INCORRECT_QUANTITY_NUMBER);
        verify(postDao, never()).getPostsByInitialIdAndQuantity(anyLong(), anyLong());
        verify(tagService, never()).getAllTagsByPostId(anyLong());
    }

    @Test
    public void getAllPostsByTagIdSuccess() {
//        when(tagService.validateTagId(anyLong())).thenReturn(true);
        when(postDao.getAllPostsByAuthorId(anyLong())).thenReturn(posts);
        postService.getAllPostsByAuthorId(CORRECT_AUTHOR_ID);
        verify(postDao, times(1)).getAllPostsByAuthorId(anyLong());
        verify(tagService, times(3)).getAllTagsByPostId(anyLong());
        verify(authorDao, times(1)).checkAuthorById(anyLong());
    }
//    @Test
//    public void getAllPostsByTagId() {
//    }
//    @Test
//    public void getAllPostsByTagId() {
//    }
//
//    @Test
//    public void getPostById() {
//    }
//
//    @Test
//    public void addPost() {
//    }
//
//    @Test
//    public void addTagToPost() {
//    }
//
//    @Test
//    public void updatePost() {
//    }
//
//    @Test
//    public void deletePost() {
//    }
}