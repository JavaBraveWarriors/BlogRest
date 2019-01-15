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

@RestController
@RequestMapping("/posts")
public class PostRestController {
    private static final Long DEFAULT_RETURNED_NUMBER = 10L;
    private PostService postService;

    @Autowired
    public PostRestController(PostService postService) {
        this.postService = postService;
    }


    @GetMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public Post getPostById(@PathVariable(value = "id") Long id) {
        return postService.getPostById(id);
    }

    @GetMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public List<Post> getPostsByInitialIdAndQuantity(
            @RequestParam(value = "from", required = false) Long initial,
            @RequestParam(value = "quantity", required = false) Long quantity) {
        if (initial == null) {
            return postService.getAllPosts();
        } else if (quantity == null) {
            return postService.getPostsByInitialIdAndQuantity(initial, DEFAULT_RETURNED_NUMBER);
        }
        return postService.getPostsByInitialIdAndQuantity(initial, quantity);
    }

    @GetMapping("/tag/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public List<Post> getAllPostsByTagId(@PathVariable(value = "id") Long tagId) {
        return postService.getAllPostsByTagId(tagId);
    }

    @PutMapping("{postId}/tag/{tagId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void addTagToPost(@PathVariable(value = "postId") Long postId,
                             @PathVariable(value = "tagId") Long tagId) {
        postService.addTagToPost(postId, tagId);
    }

    @DeleteMapping("{postId}/tag/{tagId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteTagInPost(
            @PathVariable(value = "postId") Long postId,
            @PathVariable(value = "tagId") Long tagId) {
        postService.deleteTagInPost(postId, tagId);
    }

    @PostMapping("")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Long addPost(@Valid @RequestBody Post post, BindingResult validationResults) {
        if (validationResults.hasErrors()) {
            throw new ValidationException(validationResults.getFieldErrors().toString());
        } else {
            return postService.addPost(post);
        }
    }

    @PutMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public void updatePost(@Valid @RequestBody Post post, BindingResult validationResults) {
        if (validationResults.hasErrors()) {
            throw new ValidationException(validationResults.getFieldErrors().toString());
        } else {
            postService.updatePost(post);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deletePost(@PathVariable(value = "id") Long id) {
        postService.deletePost(id);
    }
}
