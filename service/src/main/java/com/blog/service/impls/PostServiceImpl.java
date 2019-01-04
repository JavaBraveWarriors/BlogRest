package com.blog.service.impls;

import com.blog.Post;
import com.blog.dao.AuthorDao;
import com.blog.dao.PostDao;
import com.blog.dao.TagDao;
import com.blog.exception.InternalServerException;
import com.blog.exception.NotFoundException;
import com.blog.exception.ValidationException;
import com.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    private PostDao postDao;
    private TagDao tagDao;
    private AuthorDao authorDao;

    @Value("${authorService.incorrectId}")
    String incorrectAuthorId;

    @Value("${authorService.notExist}")
    String notExistAuthor;

    @Value("${tagService.incorrectId}")
    String incorrectTagId;

    @Value("${tagService.notExist}")
    String notExistTag;

    @Value("${postService.incorrectId}")
    String incorrectPostId;

    @Value("${postService.notExist}")
    String notExistPost;

    @Value("${postService.errorOfUpdating}")
    String updateError;


    @Autowired
    public PostServiceImpl(PostDao postDao, TagDao tagDao, AuthorDao authorDao) {
        this.postDao = postDao;
        this.tagDao = tagDao;
        this.authorDao = authorDao;
    }


    @Override
    public List<Post> getAllPosts() {
        return postDao.getAllPosts();
    }

    @Override
    public List<Post> getAllPostsByAuthorId(Long userId) {
        if (userId < 0L || userId == null)
            throw new ValidationException(incorrectAuthorId);
        if (!authorDao.checkAuthorById(userId))
            throw new NotFoundException(notExistAuthor);
        return postDao.getAllPostsByAuthorId(userId);
    }


    @Override
    public List<Post> getPostsByInitialIdAndQuantity(Long initial, Long quantity) {
        if (initial < 0L || initial == null)
            throw new ValidationException(incorrectAuthorId);
        if (quantity < 0L || quantity == null)
            throw new ValidationException(incorrectTagId);
        return postDao.getPostsByInitialIdAndQuantity(initial, quantity);
    }

    @Override
    public List<Post> getAllPostsByTagId(Long tagId) {
        if (tagId < 0L || tagId == null)
            throw new ValidationException(incorrectAuthorId);
        return postDao.getAllPostsByTagId(tagId);
    }

    @Override
    public Post getPostById(Long id) {
        if (id < 0L || id == null)
            throw new ValidationException(incorrectPostId);
        if (!postDao.checkPostById(id))
            throw new NotFoundException(notExistPost);
        return postDao.getPostById(id);
    }

    @Override
    public Long addPost(Post post) {
        post.setDate(LocalDate.now());
        return postDao.addPost(post);
    }

    @Override
    public void updatePost(Post post) {
        if (!postDao.checkPostById(post.getId()))
            throw new NotFoundException(notExistPost);
        if (!authorDao.checkAuthorById(post.getAuthorId()))
            throw new NotFoundException(notExistAuthor);
        if (postDao.updatePost(post) == 0)
            throw new InternalServerException(updateError);
    }

    @Override
    public void deletePost(Long id) {
        if (id < 0L || id == null)
            throw new ValidationException(incorrectPostId);
        if (!postDao.checkPostById(id))
            throw new NotFoundException(notExistPost);
        if (postDao.deletePost(id) == 0)
            throw new InternalServerException(updateError);
    }
}
