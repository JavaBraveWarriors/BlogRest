package com.blog.service.impls;

import com.blog.Post;
import com.blog.Tag;
import com.blog.dao.AuthorDao;
import com.blog.dao.PostDao;
import com.blog.exception.InternalServerException;
import com.blog.exception.NotFoundException;
import com.blog.exception.ValidationException;
import com.blog.service.PostService;
import com.blog.service.TagService;
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
    private TagService tagService;
    private AuthorDao authorDao;

    @Value("${authorService.incorrectId}")
    private String incorrectAuthorId;

    @Value("${authorService.incorrectInitialNumber}")
    private String incorrectInitialNumber;

    @Value("${authorService.incorrectQuantityNumber}")
    private String incorrectQuantityNumber;


    @Value("${authorService.notExist}")
    private String notExistAuthor;

    @Value("${tagService.incorrectId}")
    private String incorrectTagId;

    @Value("${tagService.incorrect}")
    private String incorrectTag;

    @Value("${tagService.notExist}")
    private String notExistTag;

    @Value("${postService.incorrectId}")
    private String incorrectPostId;

    @Value("${postService.notExist}")
    private String notExistPost;

    @Value("${postService.errorOfUpdating}")
    private String updateError;

    @Value("${postService.errorOfDeleting}")
    private String deleteError;

    @Value("${postService.tagExistInPost}")
    private String tagExistInPost;

    @Autowired
    public PostServiceImpl(PostDao postDao, TagService tagService, AuthorDao authorDao) {
        this.postDao = postDao;
        this.tagService = tagService;
        this.authorDao = authorDao;
    }


    public List<Post> getAllPosts() {
        List<Post> posts = postDao.getAllPosts();
        posts.forEach(post -> post.setTags(tagService.getAllTagsByPostId(post.getId())));
        return posts;
    }

    public List<Post> getAllPostsByAuthorId(Long authorId) {
        checkAuthor(authorId);
        List<Post> posts = postDao.getAllPostsByAuthorId(authorId);
        posts.forEach(post -> post.setTags(tagService.getAllTagsByPostId(post.getId())));
        return posts;
    }

    public List<Post> getPostsByInitialIdAndQuantity(Long initial, Long quantity) {
        if (initial == null || initial < 0L)
            throw new ValidationException(incorrectInitialNumber);
        if (quantity == null || quantity < 0L)
            throw new ValidationException(incorrectQuantityNumber);

        List<Post> posts = postDao.getPostsByInitialIdAndQuantity(initial, quantity);
        posts.forEach(post -> post.setTags(tagService.getAllTagsByPostId(post.getId())));
        return posts;
    }

    public List<Post> getAllPostsByTagId(Long tagId) {
        validateTagId(tagId);
        List<Post> posts = postDao.getAllPostsByTagId(tagId);
        posts.forEach(post -> post.setTags(tagService.getAllTagsByPostId(post.getId())));
        return posts;
    }

    public Post getPostById(Long id) {
        validatePostId(id);
        Post post = postDao.getPostById(id);
        post.setTags(tagService.getAllTagsByPostId(post.getId()));
        return post;
    }

    public Long addPost(Post post) {
        checkAuthor(post.getAuthorId());
        post.setDate(LocalDate.now());
        Long key = postDao.addPost(post);
        post.getTags().forEach(tag -> {
            tagService.validateTagId(tag.getId());
            postDao.addTagToPost(key, tag.getId());
        });
        return key;
    }

    @Override
    public void addTagToPost(Long postId, Long tagId) {
        validatePostId(postId);
        validateTagId(tagId);
        if(postDao.checkTagInPostById(postId,tagId))
            throw new ValidationException(tagExistInPost);
        postDao.addTagToPost(postId,tagId);

    }

    public void updatePost(Post post) {
        checkPost(post);
        validateTags(post.getTags());
        List<Tag> tagsPost = tagService.getAllTagsByPostId(post.getId());
        tagsPost.forEach(tag -> {
            if (!post.getTags().contains(tag))
                postDao.deleteTagInPost(post.getId(), tag.getId());
        });
        post.getTags().forEach(tag -> {
            if (!postDao.checkTagInPostById(post.getId(), tag.getId()))
                postDao.addTagToPost(post.getId(), tag.getId());
        });
        if (!postDao.updatePost(post))
            throw new InternalServerException(updateError);
    }


    public void deletePost(Long id) {
        validatePostId(id);
        List<Tag> tags = tagService.getAllTagsByPostId(id);
        tags.forEach(tag -> postDao.deleteTagInPost(id, tag.getId()));
        if (!postDao.deletePost(id))
            throw new InternalServerException(deleteError);
    }

    private void validatePostId(Long id) {
        if (id == null || id < 0L)
            throw new ValidationException(incorrectPostId);
        if (!postDao.checkPostById(id))
            throw new NotFoundException(notExistPost);
    }
    private void validateTagId(Long tagId){
        if (tagId == null || tagId < 0L)
            throw new ValidationException(incorrectTagId);
    }

    private void validateTags(List<Tag> tagsValidation) {
        tagsValidation.forEach(tag -> {
            tagService.validateTagId(tag.getId());
            if (tag != tagService.getTagById(tag.getId()))
                throw new ValidationException(incorrectTag);
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
