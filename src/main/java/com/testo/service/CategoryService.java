package com.testo.service;

import org.springframework.http.ResponseEntity;

import com.testo.model.Category;
import com.testo.payload.ApiResponse;
import com.testo.payload.PagedResponse;
import com.testo.security.UserPrincipal;

public interface CategoryService {
	PagedResponse<Category> getAllCategories(int page, int size);

	ResponseEntity<Category> addCategory(Category request, UserPrincipal currentUser);

	ResponseEntity<Category> getCategory(Long id);

	ResponseEntity<Category> updateCategory(Long id, Category newCategory, UserPrincipal currentUser);

	ResponseEntity<ApiResponse> deleteCategory(Long id, UserPrincipal currentUser);
}
