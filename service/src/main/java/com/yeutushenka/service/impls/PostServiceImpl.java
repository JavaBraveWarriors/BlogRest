package com.yeutushenka.service.impls;

import com.yeutushenka.Post;
import com.yeutushenka.ResponseStatus;
import com.yeutushenka.dao.AuthorDao;
import com.yeutushenka.dao.PostDao;
import com.yeutushenka.dao.TagDao;
import com.yeutushenka.exception.InternalServerException;
import com.yeutushenka.exception.NotFoundException;
import com.yeutushenka.exception.ValidationException;
import com.yeutushenka.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<Post> getAllPostsByAuthorIdAndTagId(Long userId, Long tagId) {
        if (userId < 0L || userId == null)
            throw new ValidationException(incorrectAuthorId);
        if (tagId < 0L || tagId == null)
            throw new ValidationException(incorrectTagId);
        if (!tagDao.checkTagById(tagId))
            throw new NotFoundException(notExistTag);
        if (!authorDao.checkAuthorById(userId))
            throw new NotFoundException(notExistAuthor);
        return postDao.getAllPostsByAuthorIdAndTagId(userId, tagId);
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
    public Post getPostById(Long id) {
        if (id < 0L || id == null)
            throw new ValidationException(incorrectPostId);
        if (!postDao.checkPostById(id))
            throw new NotFoundException(notExistPost);
        return postDao.getPostById(id);
    }

    @Override
    public Long addPost(Post post) {
        return postDao.addPost(post);
    }

    @Override
    public void updatePost(Post post) {
        if (!postDao.checkPostById(post.getId()))
            throw new NotFoundException(notExistPost);
        if (!authorDao.checkAuthorById(post.getAuthorId()))
            throw new NotFoundException(notExistAuthor);
        if (postDao.updatePost(post) == ResponseStatus.ERROR)
            throw new InternalServerException(updateError);
    }

    @Override
    public void deletePost(Long id) {
        if (id < 0L || id == null)
            throw new ValidationException(incorrectPostId);
        if (!postDao.checkPostById(id))
            throw new NotFoundException(notExistPost);
        if (postDao.deletePost(id) == ResponseStatus.ERROR)
            throw new InternalServerException(updateError);
    }
}
