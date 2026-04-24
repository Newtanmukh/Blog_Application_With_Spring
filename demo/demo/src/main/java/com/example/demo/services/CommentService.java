package com.example.demo.services;

import com.example.demo.payloads.dtos.CommentDto;

public interface CommentService {

    CommentDto createComment(CommentDto commentDto, Long postId);

    void deleteComment(Long commentId);
}
