package com.blog.service.impls;

import com.blog.Post;
import com.blog.Tag;
import com.blog.dao.PostDao;
import com.blog.exception.InternalServerException;
import com.blog.exception.ValidationException;
import com.blog.service.PostService;
import com.blog.service.TagService;
import com.blog.validator.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    /**
     * This field used for logging events
     */
    private static final Logger LOGGER = LogManager.getLogger();

    private PostDao postDao;
    private Validator validator;
    private TagService tagService;

    @Value("${postService.errorOfUpdating}")
    private String updateError;

    @Value("${postService.errorOfDeleting}")
    private String deleteError;

    @Value("${postService.tagExistInPost}")
    private String tagExistInPost;

    @Autowired
    public PostServiceImpl(PostDao postDao, Validator validator, TagService tagService) {
        this.postDao = postDao;
        this.validator = validator;
        this.tagService = tagService;
    }

    public List<Post> getAllPosts() {
        LOGGER.debug("Gets all posts.");
        List<Post> posts = postDao.getAllPosts();
        posts.forEach(post -> post.setTags(tagService.getAllTagsByPostId(post.getId())));
        return posts;
    }

    public List<Post> getAllPostsByAuthorId(Long authorId) {
        LOGGER.debug("Gets all posts by author id = [{}].", authorId);
        validator.validateAuthor(authorId);
        List<Post> posts = postDao.getAllPostsByAuthorId(authorId);
        posts.forEach(post -> post.setTags(tagService.getAllTagsByPostId(post.getId())));
        return posts;
    }

    public List<Post> getPostsByInitialIdAndQuantity(Long initial, Long quantity) {
        LOGGER.debug("Gets list of posts by initial id = [{}] and size = [{}].", initial, quantity);
        validator.validateInitialAndQuantity(initial, quantity);
        List<Post> posts = postDao.getPostsByInitialIdAndQuantity(initial, quantity);
        posts.forEach(post -> post.setTags(tagService.getAllTagsByPostId(post.getId())));
        return posts;
    }

    public List<Post> getAllPostsByTagId(Long tagId) {
        LOGGER.debug("Gets list of posts by tag id = [{}].", tagId);
        validator.validateTagId(tagId);
        List<Post> posts = postDao.getAllPostsByTagId(tagId);
        posts.forEach(post -> post.setTags(tagService.getAllTagsByPostId(post.getId())));
        return posts;
    }

    public Post getPostById(Long id) {
        LOGGER.debug("Gets list of posts by id = [{}].", id);
        validator.validatePostId(id);
        Post post = postDao.getPostById(id);
        post.setTags(tagService.getAllTagsByPostId(post.getId()));
        return post;
    }

    public Long addPost(Post post) {
        LOGGER.debug("Adds new post = [{}].", post);
        validator.validateAuthor(post.getAuthorId());
        post.setTimeOfCreation(LocalDateTime.now());
        Long key = postDao.addPost(post);
        post.getTags().forEach(tag -> {
            validator.validateTagId(tag.getId());
            postDao.addTagToPost(key, tag.getId());
        });
        return key;
    }

    public void addTagToPost(Long postId, Long tagId) {
        LOGGER.debug("Adds tag id  = [{}] to post id = [{}].", tagId, postId);
        validator.validatePostId(postId);
        validator.validateTagId(tagId);
        if (postDao.checkTagInPostById(postId, tagId))
            throw new ValidationException(tagExistInPost);
        postDao.addTagToPost(postId, tagId);
    }

    public void updatePost(Post post) {
        LOGGER.debug("Updates post = [{}].", post);
        validator.checkPost(post);
        validator.validateTags(post.getTags(), tagService);
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
        LOGGER.debug("Deletes post by id = [{}].", id);
        validator.validatePostId(id);
        List<Tag> tags = tagService.getAllTagsByPostId(id);
        tags.forEach(tag -> postDao.deleteTagInPost(id, tag.getId()));
        if (!postDao.deletePost(id))
            throw new InternalServerException(deleteError);
    }


}
