package com.blog.service.impls;

import com.blog.Author;
import com.blog.Post;
import com.blog.Tag;
import com.blog.dao.AuthorDao;
import com.blog.dao.PostDao;
import com.blog.exception.InternalServerException;
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

    /**
     * This field is used to management {Post} in database.
     */
    private PostDao postDao;

    /**
     * This field is used for validate data.
     */
    private Validator validator;

    /**
     * This field is used to management {Tag}.
     */
    private TagService tagService;

    private AuthorDao authorDao;

    @Value("${postService.errorOfUpdating}")
    private String updateError;

    @Value("${postService.errorOfDeleting}")
    private String deleteError;

    @Value("${postService.errorOfAddingTag}")
    private String addTagToPost;

    @Value("${postService.errorOfDeletingTag}")
    private String deleteTagInPost;


    @Autowired
    public PostServiceImpl(PostDao postDao, Validator validator, TagService tagService, AuthorDao authorDao) {
        this.authorDao = authorDao;
        this.postDao = postDao;
        this.validator = validator;
        this.tagService = tagService;
    }

    // refactored with pagination
    public List<Post> getAllPostsByAuthorId(Long authorId) {
        LOGGER.debug("Gets all posts by author id = [{}].", authorId);
        validator.validateAuthorId(authorId);
        List<Post> posts = postDao.getAllPostsByAuthorId(authorId);

        Author author = authorDao.getAuthorById(authorId);

        posts.forEach(post -> post.setAuthorName(author.getFirstName()));
        posts.forEach(post -> post.setAuthorLastName(author.getLastName()));
        posts.forEach(post -> post.setTags(tagService.getAllTagsByPostId(post.getId())));
        return posts;
    }

    public List<Post> getPostsWithPaginationAndSorting(Long page, Long size, String sort) {
        LOGGER.debug("Gets list of posts by page id = [{}] and size = [{}].", page, size);
        validator.validateInitialAndQuantity(page, size);
        Long startItem = (page - 1) * size + 1;
        List<Post> posts = postDao.getPostsByInitialIdAndQuantity(startItem, size, sort);
        addDataInPosts(posts);
        return posts;
    }

    public Long getCountOfPagesWithPagination(Long size) {
        LOGGER.debug("Gets count of pages by size = [{}].", size);
        validator.validateSize(size);
        Long countOfPosts = postDao.getCountOfPosts();
        Long countOfPages = countOfPosts / size;
        if(countOfPosts % size != 0){
            countOfPages++;
        }
        return countOfPages;
    }

    public List<Post> getAllPostsByTagId(Long tagId) {
        LOGGER.debug("Gets list of posts by tag id = [{}].", tagId);
        validator.validateTagId(tagId);
        List<Post> posts = postDao.getAllPostsByTagId(tagId);
        addDataInPosts(posts);
        return posts;
    }

    public Post getPostById(Long postId) {
        LOGGER.debug("Gets post by id = [{}].", postId);
        validator.validatePostId(postId);
        Post post = postDao.getPostById(postId);
        post.setTags(tagService.getAllTagsByPostId(post.getId()));
        Author author = authorDao.getAuthorById(post.getAuthorId());
        post.setAuthorName(author.getFirstName());
        post.setAuthorLastName(author.getLastName());
        return post;
    }

    public Long addPost(Post post) {
        LOGGER.debug("Adds new post = [{}].", post);
        validator.validateAuthorId(post.getAuthorId());
        post.setTimeOfCreation(LocalDateTime.now());
        Long key = postDao.addPost(post);
        post.getTags().forEach(tag -> {
            validator.validateTagId(tag.getId());
            postDao.addTagToPost(key, tag.getId());
        });
        return key;
    }

    public void addTagToPost(Long postId, Long tagId) {
        LOGGER.debug("Adds tag id = [{}] to post id = [{}].", tagId, postId);
        validator.validatePostId(postId);
        validator.validateTagId(tagId);
        validator.validateTagInPost(postId, tagId);
        if (!postDao.addTagToPost(postId, tagId))
            throw new InternalServerException(addTagToPost);
    }

    public void updatePost(Post post) {
        LOGGER.debug("Updates post = [{}].", post);
        validator.checkPost(post);
        validator.validateTags(post.getTags(), tagService);
        List<Tag> postTags = tagService.getAllTagsByPostId(post.getId());
        updatePostTags(post, postTags);
        if (!postDao.updatePost(post))
            throw new InternalServerException(updateError);
    }


    public void deletePost(Long postId) {
        LOGGER.debug("Deletes post by id = [{}].", postId);
        validator.validatePostId(postId);
        if (!postDao.deletePost(postId))
            throw new InternalServerException(deleteError);
    }

    public void deleteTagInPost(Long postId, Long tagId) {
        LOGGER.debug("Deletes tag id = [{}] in post id = [{}].", tagId, postId);
        validator.validatePostId(postId);
        validator.validateTagId(tagId);
        validator.validateTagInPost(postId, tagId);
        if (!postDao.deleteTagInPost(postId, tagId))
            throw new InternalServerException(deleteTagInPost);
    }

    private void updatePostTags(Post post, List<Tag> postTags) {
        removeTagInPost(post, postTags);
        addTagsToPost(post);
    }

    private void removeTagInPost(Post post, List<Tag> postTags) {
        postTags.forEach(tag -> {
            if (!post.getTags().contains(tag)) {
                if (!postDao.deleteTagInPost(post.getId(), tag.getId()))
                    throw new InternalServerException(deleteTagInPost);
            }
        });
    }

    private void addTagsToPost(Post post) {
        post.getTags().forEach(tag -> {
            if (!postDao.checkTagInPostById(post.getId(), tag.getId())) {
                if (!postDao.addTagToPost(post.getId(), tag.getId()))
                    throw new InternalServerException(addTagToPost);
            }
        });
    }

    private void addDataInPosts(List<Post> posts) {
        posts.forEach(post -> {
            Author author = authorDao.getAuthorById(post.getAuthorId());
            post.setAuthorName(author.getFirstName());
            post.setAuthorLastName(author.getLastName());
            post.setTags(tagService.getAllTagsByPostId(post.getId()));
        });
    }
}
