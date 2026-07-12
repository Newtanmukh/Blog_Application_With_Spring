package com.example.demo.controller;


import com.example.demo.payloads.ApiResponse;
import com.example.demo.payloads.dtos.CommentDto;
import com.example.demo.services.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@Tag(name = "Comments", description = "Comment management APIs")
@PreAuthorize("hasRole('USER')")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Operation(summary = "Create comment", description = "Add a comment to a post")
    @PostMapping("/post/{postId}/comments")
    public ResponseEntity<?> createComment(@RequestBody CommentDto commentDto,
                                           @PathVariable Long postId) {
        return new ResponseEntity<>(commentService.createComment(commentDto, postId), HttpStatus.CREATED);
    }

    @Operation(summary = "Delete comment", description = "Delete a comment by ID")
    @PostMapping("/deleteComment/{commentId}")
    public ResponseEntity<?> deleteCommtns(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return new ResponseEntity<>(new ApiResponse("Comment deleted successfully", true), HttpStatus.OK);
    }

}
