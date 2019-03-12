package com.blog.controller;

import com.blog.model.Comment;
import com.blog.model.CommentListWrapper;
import com.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/comments")
public class CommentRestController {

    private static Long DEFAULT_RESPONSE_COMMENT_PAGE = 1L;
    private static Long DEFAULT_RESPONSE_COMMENT_SIZE = 10L;

    private CommentService commentService;

    @Autowired
    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{commentId}")
    @ResponseStatus(value = HttpStatus.OK)
    public Comment getCommentById(
            @PathVariable(value = "commentId") Long commentId){
        return commentService.getCommentById(commentId);
    }

    @GetMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public CommentListWrapper getListCommentsByPostIdWithPagination(
            @RequestParam(value = "page", required = false) Long page,
            @RequestParam(value = "size", required = false) Long size,
            @RequestParam(value = "postId") Long postId) {
        if (page == null) {
            page = DEFAULT_RESPONSE_COMMENT_PAGE;
        }
        if (size == null) {
            size = DEFAULT_RESPONSE_COMMENT_SIZE;
        }
        return commentService.getListCommentsByPostIdWithPagination(page, size, postId);
    }

    @GetMapping("/count")
    @ResponseStatus(value = HttpStatus.OK)
    public Long getCountOfCommentsByPostId(
            @RequestParam(value = "size") Long size,
            @RequestParam(value = "postId") Long postId){
        return commentService.getCountOfPagesWithPagination(postId, size);
    }

    @PutMapping("")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateComment(@Valid @RequestBody Comment comment){
        commentService.updateComment(comment);
    }
}
