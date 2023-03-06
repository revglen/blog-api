package com.testo.service;

import com.testo.model.Comment;
import com.testo.payload.ApiResponse;
import com.testo.payload.CommentRequest;
import com.testo.payload.PagedResponse;
import com.testo.security.UserPrincipal;

public interface CommentService {
	PagedResponse<Comment> getAllComments(Long postId, int page, int size);

	Comment addComment(CommentRequest request, Long postId, UserPrincipal currentUser);

	Comment getComment(Long postId, Long id);

	Comment updateComment(Long postId, Long id, CommentRequest newCategory, UserPrincipal currentUser);

	ApiResponse deleteComment(Long postId, Long id, UserPrincipal currentUser);
}
