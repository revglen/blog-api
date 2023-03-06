package com.testo.service;

import org.springframework.http.ResponseEntity;

import com.testo.model.Album;
import com.testo.payload.AlbumResponse;
import com.testo.payload.ApiResponse;
import com.testo.payload.PagedResponse;
import com.testo.payload.request.AlbumRequest;
import com.testo.security.UserPrincipal;

public interface AlbumService {
	PagedResponse<AlbumResponse> getAllAlbums(int page, int size);

	ResponseEntity<Album> addAlbum(AlbumRequest request, UserPrincipal currentUser);

	ResponseEntity<Album> getAlbum(Long id);

	ResponseEntity<AlbumResponse> updateAlbum(Long id, AlbumRequest newAlbum, UserPrincipal currentUser);

	ResponseEntity<ApiResponse> deleteAlbum(Long id, UserPrincipal currentUser);

	PagedResponse<Album> getUserAlbums(String username, int page, int size);
}
