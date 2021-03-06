package com.blog.controller;

import com.blog.model.Comment;
import com.blog.model.Post;
import com.blog.model.PostListWrapper;
import com.blog.model.RequestPostDto;
import com.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.Optional;

/**
 * The Post rest controller provides an interface to interact with our rest-API service to Post model.
 *
 * @author Aliaksandr Yeutushenka
 * @see PostService
 */
@RestController
@RequestMapping("/posts")
public class PostRestController {

    @Value("${postController.defaultResponsePostSize}")
    private Long DEFAULT_RESPONSE_POST_SIZE;

    @Value("${postController.defaultResponsePostPage}")
    private Long DEFAULT_RESPONSE_POST_PAGE;

    @Value("${postController.defaultResponsePostSort}")
    private String DEFAULT_RESPONSE_POST_SORT;

    private PostService postService;

    /**
     * Instantiates a new Post rest controller.
     *
     * @param postService the post service
     */
    @Autowired
    public PostRestController(PostService postService) {
        this.postService = postService;
    }

    /**
     * Gets a {Post} object where id is equal to argument parameter.
     *
     * @param id {Long} value the ID of the post you want to get.
     * @return {Post} is a object which has this ID.
     */
    @GetMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public Post getPostById(@PathVariable(value = "id") Long id) {
        return postService.getPostById(id);
    }

    /**
     * Gets a list of post objects from a specific page, a specific size. If page is null will be returned first page by default.
     * If page is not null and size is null - 10 items will be returned by default.
     *
     * @param page is {Long} value ID of the post from which you want to get objects.
     * @param size is {Long} value the number of required objects.
     * @param sort is {String} value which determines which field will be sorted, by default - by date.
     * @return {List<Post>} is a list of posts.
     */
    @GetMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public PostListWrapper getPostsWithPagination(
            @RequestParam(value = "page", required = false) Long page,
            @RequestParam(value = "size", required = false) Long size,
            @RequestParam(value = "sort", required = false) String sort) {
        return postService.getPostsWithPaginationAndSorting(
                Optional.ofNullable(page).orElse(DEFAULT_RESPONSE_POST_PAGE),
                Optional.ofNullable(size).orElse(DEFAULT_RESPONSE_POST_SIZE),
                Optional.ofNullable(sort).orElse(DEFAULT_RESPONSE_POST_SORT));
    }

    /**
     * Gets a list of post objects where is this tag.
     *
     * @param tagId is {Long} value tag ID
     * @return {List<Post>} is a list of posts.
     */
    @GetMapping("/tag/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public PostListWrapper getAllPostsByTagId(@PathVariable(value = "id") Long tagId) {
        return postService.getAllPostsByTagId(tagId);
    }

    /**
     * Add tag to post.
     *
     * @param postId is {Long} the value of post ID.
     * @param tagId  id is {Long} the value of tag ID.
     */
    @PutMapping("{postId}/tag/{tagId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void addTagToPost(@PathVariable(value = "postId") Long postId,
                             @PathVariable(value = "tagId") Long tagId) {
        postService.addTagToPost(postId, tagId);
    }

    /**
     * Delete tag in post using post ID and tag ID.
     *
     * @param postId is {Long} the value of post ID.
     * @param tagId  id is {Long} the value of tag ID.
     */
    @DeleteMapping("{postId}/tag/{tagId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteTagInPost(
            @PathVariable(value = "postId") Long postId,
            @PathVariable(value = "tagId") Long tagId) {
        postService.deleteTagInPost(postId, tagId);
    }

    /**
     * Add new post.
     *
     * @param post              {Post} to be added.
     * @param validationResults the validation results of post object.
     * @return {Long} is the value that is the id of the new post.
     */
    @PostMapping("")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Long addPost(@Valid @RequestBody RequestPostDto post, BindingResult validationResults) {
        if (validationResults.hasErrors()) {
            throw new ValidationException(validationResults.getFieldErrors().toString());
        } else {
            return postService.addPost(post);
        }
    }

    /**
     * Add comment to post.
     *
     * @param comment           {Comment} to be added.
     * @param validationResults the validation results
     */
    @PostMapping("/comment")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addCommentToPost(@Valid @RequestBody Comment comment, BindingResult validationResults) {
        if (validationResults.hasErrors()) {
            throw new ValidationException(validationResults.getFieldErrors().toString());
        } else {
            postService.addCommentToPost(comment);
        }
    }

    /**
     * Update post.
     *
     * @param post              {Post} to be updated.
     * @param validationResults the validation results of post object.
     */
    @PutMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public void updatePost(@Valid @RequestBody RequestPostDto post, BindingResult validationResults) {
        if (validationResults.hasErrors()) {
            throw new ValidationException(validationResults.getFieldErrors().toString());
        } else {
            postService.updatePost(post);
        }
    }

    /**
     * Deletes post using post ID.
     *
     * @param id is {Long} value which identifies the post ID.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deletePost(@PathVariable(value = "id") Long id) {
        postService.deletePost(id);
    }

    /**
     * Delete comment in post.
     *
     * @param id        is {Long} value which identifies the post ID.
     * @param commentId is {Long} value which identifies the comment ID.
     */
    @DeleteMapping("/{id}/comment/{commentId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteCommentInPost(
            @PathVariable(value = "id") Long id,
            @PathVariable(value = "commentId") Long commentId) {
        postService.deleteCommentInPost(id, commentId);
    }
}