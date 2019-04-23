package com.blog.controller;

import com.blog.model.Comment;
import com.blog.model.CommentListWrapper;
import com.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

/**
 * The Comment rest controller provides an interface to interact with our rest-API service to Comment model.
 *
 * @author Aliaksandr Yeutushenka
 * @see CommentService
 */
@RestController
@RequestMapping("/comments")
public class CommentRestController {

    @Value("${commentController.defaultResponseCommentPage}")
    private Long defaultResponseCommentPage;

    @Value("${commentController.defaultResponseCommentSize}")
    private Long defaultResponseCommentSize;

    private CommentService commentService;

    @Autowired
    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{commentId}")
    @ResponseStatus(value = HttpStatus.OK)
    public Comment getCommentById(
            @PathVariable(value = "commentId") Long commentId) {
        return commentService.getCommentById(commentId);
    }

    @GetMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public CommentListWrapper getListCommentsByPostIdWithPagination(
            @RequestParam(value = "page", required = false) Long page,
            @RequestParam(value = "size", required = false) Long size,
            @RequestParam(value = "postId") Long postId) {
        return commentService.getListCommentsByPostIdWithPagination(
                Optional.ofNullable(page).orElse(defaultResponseCommentPage),
                Optional.ofNullable(size).orElse(defaultResponseCommentSize),
                postId);
    }

    @GetMapping("/countPages")
    @ResponseStatus(value = HttpStatus.OK)
    public Long getCountOfCommentsByPostId(
            @RequestParam(value = "size") Long size,
            @RequestParam(value = "postId") Long postId) {
        return commentService.getCountOfPagesWithPagination(postId, size);
    }

    @PutMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateComment(@Valid @RequestBody Comment comment) {
        commentService.updateComment(comment);
    }
}