package com.example.demo.services;

import com.example.demo.entities.Post;
import com.example.demo.payloads.dtos.PostDto;
import com.example.demo.payloads.dtos.PostResponse;

import java.util.List;

public interface PostService {

    //Create
    PostDto createPost(PostDto postDto,  Long userId, Long categoryId);

    //update
    PostDto updatePost(PostDto postDto, Long id, Long userId, Long categoryId);

    //delete post
    void deletePost(Long id);

    //get all post
    PostResponse getAllPosts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    //get post by id.
    PostDto getPostById(Long id);

    List<PostDto> getPostsByCategory(Long categoryId);

    List<PostDto> getPostsByUser(Long userId);

    List<PostDto> searchPosts(String keyword);
}
