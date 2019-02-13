package com.blog.controller;

import com.blog.Comment;
import com.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/")
    @ResponseStatus(value = HttpStatus.OK)
    List<Comment> getListCommentsByPostIdWithPagination(
            @RequestParam(value = "page", required = false) Long page,
            @RequestParam(value = "size", required = false) Long size,
            @RequestParam(value = "postId") Long postId){
        if (page == null) {
            page = DEFAULT_RESPONSE_COMMENT_PAGE;
        }
        if (size == null) {
            size = DEFAULT_RESPONSE_COMMENT_SIZE;
        }
        return commentService.getListCommentsByPostIdWithPagination(page, size, postId);
    }
}
