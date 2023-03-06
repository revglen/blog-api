package com.testo.service;

import org.springframework.http.ResponseEntity;

import com.testo.model.Post;
import com.testo.payload.ApiResponse;
import com.testo.payload.PagedResponse;
import com.testo.payload.PostRequest;
import com.testo.payload.PostResponse;
import com.testo.security.UserPrincipal;

public interface PostService {
	PagedResponse<PostResponse> getAllPosts(int page, int size);

	PagedResponse<PostResponse> getPostByCreatedBy(String username, int page, int size);

	PagedResponse<PostResponse> getPostByCategory(Long id, int page, int size);

	PagedResponse<PostResponse> getPostByTag(Long id, int page, int size);

	Post updatePost(Long id, PostRequest newPost, UserPrincipal currentUser);

	ResponseEntity<ApiResponse> deletePost(Long id, UserPrincipal currentUser);

	PostResponse addPost(PostRequest postRequest, UserPrincipal currentUser);

	PostResponse getPost(Long id);
}
