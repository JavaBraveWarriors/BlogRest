package com.blog.service.impls;

import com.blog.dao.PostDao;
import com.blog.dao.ViewDao;
import com.blog.dto.TagDto;
import com.blog.exception.InternalServerException;
import com.blog.model.*;
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
import java.util.*;

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

    private ViewDao viewDao;

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
    public PostServiceImpl(PostDao postDao, Validator validator, TagService tagService, CommentService commentService, ViewDao viewDao) {
        this.postDao = postDao;
        this.validator = validator;
        this.tagService = tagService;
        this.commentService = commentService;
        this.viewDao = viewDao;
    }

    // refactored with pagination
    public PostListWrapper getAllPostsByAuthorId(Long authorId) {
        LOGGER.debug("Gets all posts by author id = [{}].", authorId);
        validator.validateAuthorId(authorId);
        PostListWrapper postListWrapper = new PostListWrapper();
        postListWrapper.setPosts(postDao.getAllPostsByAuthorId(authorId));
        addTagsToPosts(postListWrapper.getPosts());
        Long countOfPosts = postDao.getCountOfPosts();
        postListWrapper.setCountPosts(countOfPosts);
        // TODO: refactor this method - add pagination
        // postListWrapper.setCountPages(PageCounter.getCountPages());
        return postListWrapper;
    }

    public PostListWrapper getPostsWithPaginationAndSorting(Long page, Long size, String sort) {
        LOGGER.debug("Gets list of posts by page id = [{}] and size = [{}].", page, size);
        validator.validatePageAndSize(page, size);
        Long startItem = (page - 1) * size + 1;
        PostListWrapper postListWrapper = new PostListWrapper();
        postListWrapper.setPosts(postDao.getPostsByInitialIdAndQuantity(startItem, size, sort));
        addTagsToPosts(postListWrapper.getPosts());
        Long countOfPosts = postDao.getCountOfPosts();
        postListWrapper.setCountPosts(countOfPosts);
        postListWrapper.setCountPages(PageCounter.getCountPages(size, countOfPosts));
        return postListWrapper;
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

    // TODO: refactor this method
    public PostListWrapper getAllPostsByTagId(Long tagId) {
        LOGGER.debug("Gets list of posts by tag id = [{}].", tagId);
        validator.validateTagId(tagId);
        PostListWrapper postListWrapper = new PostListWrapper();
        postListWrapper.setPosts(postDao.getAllPostsByTagId(tagId));
        addTagsToPosts(postListWrapper.getPosts());
        return postListWrapper;
    }

    public ResponsePostDto getPostById(Long postId) {
        LOGGER.debug("Gets post by id = [{}].", postId);
        validator.validatePostId(postId);
        ResponsePostDto post = postDao.getPostById(postId);

        View view = new View();
        view.setPostId(postId);
        // TODO: refactor when will be security(add userId from)
        //view.setUserId();
        // TODO: refactor this !!!
        //validator.validateAuthorId(1L);
        //view.setUserId(1L);
        //if (!viewDao.checkViewByPostIdAndUserId(postId, view.getUserId())) {
        //  viewDao.addView(view);
        //  postDao.addViewToPost(postId);
        //}
        addTagsToPosts(Collections.singletonList(post));
        return post;
    }

    public Long addPost(RequestPostDto post) {
        LOGGER.debug("Adds new post = [{}].", post);
        validator.validateAuthorId(post.getAuthorId());
        post.setTimeOfCreation(LocalDateTime.now());
        Long key = postDao.addPost(post);
        post.getTags().forEach(tag -> {
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

    public void updatePost(RequestPostDto post) {
        LOGGER.debug("Updates post = [{}].", post);
        validator.checkPost(post);
        validator.validateTags(post.getTags());
        updatePostTags(post);
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

    private void updatePostTags(RequestPostDto post) {
        removeTagInPost(post.getId());
        addTagsToPost(post);
    }

    private void removeTagInPost(Long postId) {
        postDao.deleteAllTagsInPost(postId);
    }

    private void addTagsToPost(RequestPostDto post) {
        postDao.addTagsToPost(post.getId(), post.getTags());
    }

    private void addTagsToPosts(List<ResponsePostDto> posts) {
        Set<Long> postsId = new HashSet<>();
        posts.forEach(responsePostDto -> postsId.add(responsePostDto.getId()));
        List<TagDto> tagDtoList = tagService.getAllTagsByPostsId(postsId);
        posts.forEach(post -> {
            List<Tag> postTags = new ArrayList<>();
            tagDtoList.stream().filter(tagDto -> post.getId().equals(tagDto.getPostId())).forEach(tagDto -> postTags.add(tagDto.getTag()));
            post.setTags(postTags);
        });
    }
}