package com.testo.service;

import org.springframework.http.ResponseEntity;

import com.testo.payload.ApiResponse;
import com.testo.payload.PagedResponse;
import com.testo.payload.PhotoRequest;
import com.testo.payload.PhotoResponse;
import com.testo.security.UserPrincipal;

import jakarta.validation.Valid;

public interface PhotoService {
	PagedResponse<PhotoResponse> getAllPhotos(int page, int size);

	ResponseEntity<PhotoResponse> getPhoto(Long id);

	PhotoResponse updatePhoto(Long id, @Valid PhotoRequest photo, UserPrincipal currentUser);

	PhotoResponse addPhoto(PhotoRequest photoRequest, UserPrincipal currentUser);

	ResponseEntity<ApiResponse> deletePhoto(Long id, UserPrincipal currentUser);

	PagedResponse<PhotoResponse> getAllPhotosByAlbum(Long albumId, int page, int size);
}
