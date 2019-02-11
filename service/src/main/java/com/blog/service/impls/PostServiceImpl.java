package com.blog.service.impls;

import com.blog.Comment;
import com.blog.PostForAdd;
import com.blog.PostForGet;
import com.blog.Tag;
import com.blog.dao.PostDao;
import com.blog.exception.InternalServerException;
import com.blog.service.CommentService;
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
import java.util.Arrays;
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

    private CommentService commentService;

    @Value("${postService.errorOfUpdating}")
    private String updateError;

    @Value("${postService.errorOfDeleting}")
    private String deleteError;

    @Value("${postService.errorOfAddingTag}")
    private String addTagToPost;

    @Value("${postService.errorOfDeletingTag}")
    private String deleteTagInPost;

    @Autowired
    public PostServiceImpl(PostDao postDao, Validator validator, TagService tagService, CommentService commentService) {
        this.postDao = postDao;
        this.validator = validator;
        this.tagService = tagService;
        this.commentService = commentService;
    }

    // refactored with pagination
    public List<PostForGet> getAllPostsByAuthorId(Long authorId) {
        LOGGER.debug("Gets all posts by author id = [{}].", authorId);
        validator.validateAuthorId(authorId);
        List<PostForGet> posts = postDao.getAllPostsByAuthorId(authorId);

        posts.forEach(post -> post.setTags(tagService.getAllTagsByPostId(post.getId())));
        return posts;
    }

    public List<PostForGet> getPostsWithPaginationAndSorting(Long page, Long size, String sort) {
        LOGGER.debug("Gets list of posts by page id = [{}] and size = [{}].", page, size);
        validator.validatePageAndSize(page, size);
        Long startItem = (page - 1) * size + 1;
        List<PostForGet> posts = postDao.getPostsByInitialIdAndQuantity(startItem, size, sort);
        addDataInPosts(posts);
        return posts;
    }

    public Long getCountOfPagesWithPagination(Long size) {
        LOGGER.debug("Gets count of pages by size = [{}].", size);
        validator.validateSizeOfPages(size);
        Long countOfPosts = postDao.getCountOfPosts();
        Long countOfPages = countOfPosts / size;
        if (countOfPosts % size != 0) {
            countOfPages++;
        }
        return countOfPages;
    }

    public void addCommentToPost(Comment comment) {
        LOGGER.debug("Add comment to post id = [{}], comment = [{}].", comment.getPostId(), comment);
        commentService.addComment(comment);
        postDao.addComment(comment.getPostId());
    }

    public void deleteCommentInPost(Long postId, Long commentId) {
        LOGGER.debug("Delete comment in post id = [{}], comment id = [{}].", postId, commentId);
        validator.validateCommentInPost(postId, commentId);
        commentService.deleteComment(commentId);
        postDao.deleteComment(postId);
    }

    public List<PostForGet> getAllPostsByTagId(Long tagId) {
        LOGGER.debug("Gets list of posts by tag id = [{}].", tagId);
        validator.validateTagId(tagId);
        List<PostForGet> posts = postDao.getAllPostsByTagId(tagId);
        addDataInPosts(posts);
        return posts;
    }

    public PostForGet getPostById(Long postId) {
        LOGGER.debug("Gets post by id = [{}].", postId);
        validator.validatePostId(postId);
        PostForGet post = postDao.getPostById(postId);
        post.setTags(tagService.getAllTagsByPostId(post.getId()));
        return post;
    }

    public Long addPost(PostForAdd post) {
        LOGGER.debug("Adds new post = [{}].", post);
        validator.validateAuthorId(post.getAuthorId());
        post.setTimeOfCreation(LocalDateTime.now());
        Long key = postDao.addPost(post);
        Arrays.stream(post.getTags()).forEach(tag -> {
            validator.validateTagId(tag);
            postDao.addTagToPost(key, tag);
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

    public void updatePost(PostForAdd post) {
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

    private void updatePostTags(PostForAdd post, List<Tag> postTags) {
        removeTagInPost(post, postTags);
        addTagsToPost(post);
    }

    private void removeTagInPost(PostForAdd post, List<Tag> postTags) {
        postTags.forEach(tag -> {
            if (Arrays.stream(post.getTags()).noneMatch(t -> t.equals(tag.getId()))) {
                if (!postDao.deleteTagInPost(post.getId(), tag.getId()))
                    throw new InternalServerException(deleteTagInPost);
            }
        });
    }

    private void addTagsToPost(PostForAdd post) {
        Arrays.stream(post.getTags()).forEach(tag -> {
            if (!postDao.checkTagInPostById(post.getId(), tag)) {
                if (!postDao.addTagToPost(post.getId(), tag))
                    throw new InternalServerException(addTagToPost);
            }
        });
    }

    private void addDataInPosts(List<PostForGet> posts) {
        posts.forEach(post -> post.setTags(tagService.getAllTagsByPostId(post.getId())));
    }
}
