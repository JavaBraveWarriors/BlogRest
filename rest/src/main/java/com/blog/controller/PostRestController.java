package com.blog.controller;

import com.blog.Post;
import com.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;

/**
 * The type Post rest controller.
 *
 * @author Aliaksandr Yeutushenka
 * @see PostService
 */
@RestController
@RequestMapping("/posts")
public class PostRestController {
    private static final Long DEFAULT_RESPONSE_POST_SIZE = 10L;
    private static final Long DEFAULT_RESPONSE_POST_PAGE = 1L;
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
     * @return {List<Post>} is a list of posts.
     */
    @GetMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public List<Post> getPostsWithPagination(
            @RequestParam(value = "page", required = false) Long page,
            @RequestParam(value = "size", required = false) Long size) {
        if (page == null) {
            page = DEFAULT_RESPONSE_POST_PAGE;
        }
        if (size == null) {
            size = DEFAULT_RESPONSE_POST_SIZE;
        }
        return postService.getPostsWithPagination(page, size);
    }

    /**
     * Gets a list of post objects where is this tag.
     *
     * @param tagId is {Long} value tag ID
     * @return {List<Post>} is a list of posts.
     */
    @GetMapping("/tag/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public List<Post> getAllPostsByTagId(@PathVariable(value = "id") Long tagId) {
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
    public Long addPost(@Valid @RequestBody Post post, BindingResult validationResults) {
        if (validationResults.hasErrors()) {
            throw new ValidationException(validationResults.getFieldErrors().toString());
        } else {
            return postService.addPost(post);
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
    public void updatePost(@Valid @RequestBody Post post, BindingResult validationResults) {
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
}
