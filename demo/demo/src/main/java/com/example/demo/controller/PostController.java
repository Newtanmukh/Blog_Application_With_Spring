package com.example.demo.controller;

import com.example.demo.payloads.ApiResponse;
import com.example.demo.payloads.dtos.PostDto;
import com.example.demo.payloads.dtos.PostResponse;
import com.example.demo.services.FileService;
import com.example.demo.services.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "Posts", description = "Post management APIs")
@PreAuthorize("hasRole('USER')")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    //create post
    @Operation(summary = "Create post", description = "Create a new post for a user in a category")
    @PostMapping("/user/{userId}/category/{categoryId}/posts")
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto,
                                              @PathVariable Long userId,
                                              @PathVariable Long categoryId) {

        return new ResponseEntity<>(postService.createPost(postDto, userId, categoryId), HttpStatus.CREATED);
    }



    // get by user
    @Operation(summary = "Get post by user", description = "Retrieve posts for a specific user")
    @GetMapping("/user/{userId}/posts")
    public ResponseEntity<PostDto> getPostByUserId(@PathVariable Long userId) {
        return new ResponseEntity<>(postService.getPostById(userId), HttpStatus.OK);
    }

    // get by category
    @Operation(summary = "Get posts by category", description = "Retrieve posts belonging to a category")
    @GetMapping("/category/{categoryId}/posts")
    public ResponseEntity<List<PostDto>> getPostsByCategory(@PathVariable Long categoryId) {

        List<PostDto> posts = this.postService.getPostsByCategory(categoryId);
        return new ResponseEntity<>(posts, HttpStatus.OK);

    }

    // get all posts
    @Operation(summary = "Get all posts", description = "Retrieve all posts with pagination and sorting")
    @GetMapping("/")
    public ResponseEntity<PostResponse> getAllPost(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "100", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "updatedAt", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {

        PostResponse postResponse = this.postService.getAllPosts(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<PostResponse>(postResponse, HttpStatus.OK);
    }

    // get post details by id
    @Operation(summary = "Get post by ID", description = "Retrieve a specific post by ID")
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDto> getPostByPostId(@PathVariable Long postId) {

        PostDto postDto = this.postService.getPostById(postId);
        return new ResponseEntity<PostDto>(postDto, HttpStatus.OK);
    }


    // delete post
    @Operation(summary = "Delete post", description = "Delete a post by ID")
    @DeleteMapping("/posts/{postId}")
    public ApiResponse deletePost(@PathVariable Long postId) {
        this.postService.deletePost(postId);
        return new ApiResponse("Post is successfully deleted !!", true);
    }

    // update post
    @Operation(summary = "Update post", description = "Update an existing post")
    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto,
                                              @PathVariable Long postId,
                                              @PathVariable Long categoryId,
                                              @PathVariable Long userId) {
        PostDto updatePost = postService.updatePost(postDto, postId, categoryId, userId);
        return new ResponseEntity<PostDto>(updatePost, HttpStatus.OK);
    }

    // search
    @Operation(summary = "Search posts", description = "Search posts by keyword")
    @GetMapping("/posts/search/{keywords}")
    public ResponseEntity<List<PostDto>> searchPostByTitle(@PathVariable("keywords") String keywords) {
        List<PostDto> result = this.postService.searchPosts(keywords);
        return new ResponseEntity<List<PostDto>>(result, HttpStatus.OK);
    }


    //file service
    @Operation(summary = "Upload post image", description = "Upload an image for a post")
    @PostMapping("/post/image/upload/{postId}")
    public ResponseEntity<PostDto> uploadPostImage(@RequestParam("image") MultipartFile image,
                                                   @PathVariable Long postId) throws IOException {

        PostDto postDto = postService.getPostById(postId);

        String fileName = fileService.uploadImage(path, image);
        postDto.setImageName(fileName);
        PostDto updatePost = postService.updatePost(postDto, postId);
        return new ResponseEntity<>(updatePost, HttpStatus.OK);
    }

    //method to serve files
    @Operation(summary = "Download post image", description = "Serve an uploaded post image")
    @GetMapping(value = "/post/image/{imageName}",produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(
            @PathVariable("imageName") String imageName,
            HttpServletResponse response
    ) throws IOException {

        InputStream resource = this.fileService.getResource(path, imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream())   ;
    }
}
