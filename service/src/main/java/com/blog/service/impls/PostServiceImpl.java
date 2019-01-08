package com.blog.service.impls;

import com.blog.Post;
import com.blog.Tag;
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

    @Value("${authorService.incorrectInitialNumber}")
    String incorrectInitialNumber;

    @Value("${authorService.incorrectQuantityNumber}")
    String incorrectQuantityNumber;


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

    @Value("${postService.errorOfDeleting}")
    String deleteError;


    @Autowired
    public PostServiceImpl(PostDao postDao, TagDao tagDao, AuthorDao authorDao) {
        this.postDao = postDao;
        this.tagDao = tagDao;
        this.authorDao = authorDao;
    }


    @Override
    public List<Post> getAllPosts() {
        List<Post> posts = postDao.getAllPosts();
        posts.forEach(post -> post.setTags(postDao.getAllTagsByPostId(post.getId())));
        return posts;
    }

    @Override
    public List<Post> getAllPostsByAuthorId(Long authorId) {
        checkAuthor(authorId);
        List<Post> posts = postDao.getAllPostsByAuthorId(authorId);
        posts.forEach(post -> post.setTags(postDao.getAllTagsByPostId(post.getId())));
        return posts;
    }


    @Override
    public List<Post> getPostsByInitialIdAndQuantity(Long initial, Long quantity) {
        if (initial == null || initial < 0L)
            throw new ValidationException(incorrectInitialNumber);
        if (quantity == null || quantity < 0L)
            throw new ValidationException(incorrectQuantityNumber);

        List<Post> posts = postDao.getPostsByInitialIdAndQuantity(initial, quantity);
        posts.forEach(post -> post.setTags(postDao.getAllTagsByPostId(post.getId())));
        return posts;
    }

    @Override
    public List<Post> getAllPostsByTagId(Long tagId) {
        if (tagId == null || tagId < 0L)
            throw new ValidationException(incorrectTagId);

        List<Post> posts = postDao.getAllPostsByTagId(tagId);
        posts.forEach(post -> post.setTags(postDao.getAllTagsByPostId(post.getId())));
        return posts;
    }

    @Override
    public Post getPostById(Long id) {
        validatePostId(id);
        Post post = postDao.getPostById(id);
        post.setTags(postDao.getAllTagsByPostId(post.getId()));
        return post;
    }

    @Override
    public Long addPost(Post post) {
        checkAuthor(post.getAuthorId());
        Long key = postDao.addPost(post);
        post.getTags().forEach(tag -> {
            if (!tagDao.checkTagById(tag.getId()))
                throw new ValidationException(incorrectTagId);
            else
                postDao.addTagToPost(key, tag.getId());
        });
        post.setDate(LocalDate.now());
        return key;
    }

    @Override
    public void updatePost(Post post) {
        checkPost(post);
        validateTags(post.getTags());
        List<Tag> tagsPost = postDao.getAllTagsByPostId(post.getId());
        tagsPost.forEach(tag -> {
            if (!post.getTags().contains(tag))
                postDao.deleteTagInPost(post.getId(), tag.getId());
        });
        post.getTags().forEach(tag -> {
            if (!postDao.checkTagInPostById(post.getId(), tag.getId()))
                postDao.addTagToPost(post.getId(), tag.getId());
        });
        if (postDao.updatePost(post) == 0)
            throw new InternalServerException(updateError);
    }


    @Override
    public void deletePost(Long id) {
        validatePostId(id);
        List<Tag> tags = postDao.getAllTagsByPostId(id);
        tags.forEach(tag -> postDao.deleteTagInPost(id, tag.getId()));
        if (postDao.deletePost(id) == 0)
            throw new InternalServerException(deleteError);
    }

    private void validatePostId(Long id) {
        if (id == null || id < 0L)
            throw new ValidationException(incorrectPostId);
        if (!postDao.checkPostById(id))
            throw new NotFoundException(notExistPost);
    }

    private void validateTags(List<Tag> tagsValidation) {
        tagsValidation.forEach(tag -> {
            if (!tagDao.checkTagById(tag.getId()))
                throw new ValidationException(incorrectTagId);
            if (tag != tagDao.getTagById(tag.getId()))
                throw new ValidationException(incorrectTagId);
        });
    }

    private void checkPost(Post post) {
        validatePostId(post.getId());
        checkAuthor(post.getAuthorId());
    }

    private void checkAuthor(Long authorId) {
        if (authorId == null || authorId < 0L)
            throw new ValidationException(incorrectAuthorId);
        if (!authorDao.checkAuthorById(authorId))
            throw new NotFoundException(notExistAuthor);
    }
}
