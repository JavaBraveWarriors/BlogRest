package com.blog.controller;

import com.blog.Post;
import com.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostRestController {
    private PostService postService;

    @Autowired
    public PostRestController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/")
    @ResponseStatus(value = HttpStatus.OK)
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public Post getPostById(@PathVariable(value = "id") Long id) {
        return postService.getPostById(id);
    }

    @GetMapping("/{initial}-{quantity}")
    @ResponseStatus(value = HttpStatus.OK)
    public List<Post> getPostsByInitialIdAndQuantity(
            @PathVariable(value = "initial") Long initial,
            @PathVariable(value = "quantity") Long quantity) {
        return postService.getPostsByInitialIdAndQuantity(initial, quantity);
    }

    @PostMapping("/")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Long addPost(@RequestBody Post post) {
        return postService.addPost(post);
    }

    @PutMapping("/")
    @ResponseStatus(value = HttpStatus.OK)
    public void updatePost(@RequestBody Post post) {
        postService.updatePost(post);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deletePost(@PathVariable(value = "id") Long id) {
        postService.deletePost(id);
    }
}
