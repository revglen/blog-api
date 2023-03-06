package com.testo.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.testo.exception.BlogapiException;
import com.testo.exception.ResourceNotFoundException;
import com.testo.model.Album;
import com.testo.model.role.RoleName;
import com.testo.model.user.User;
import com.testo.payload.AlbumResponse;
import com.testo.payload.ApiResponse;
import com.testo.payload.PagedResponse;
import com.testo.payload.request.AlbumRequest;
import com.testo.repository.AlbumRepository;
import com.testo.repository.UserRepository;
import com.testo.security.UserPrincipal;
import com.testo.service.AlbumService;
import com.testo.utils.AppUtils;

@Service
public class AlbumServiceImpl implements AlbumService {

	private static final String CREATED_AT = "createdAt";
	private static final String ALBUM_STR = "Album";
	private static final String YOU_DON_T_HAVE_PERMISSION_TO_MAKE_THIS_OPERATION = "You don't have permission to make this operation";

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AlbumRepository albumRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public PagedResponse<AlbumResponse> getAllAlbums(int page, int size) {
		AppUtils.validatePageNumberAndSize(page, size);
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);
		Page<Album> albums = albumRepository.findAll(pageable);
		if (albums.getNumberOfElements() == 0)
			return new PagedResponse<>(Collections.emptyList(), albums.getNumber(), albums.getSize(),
					albums.getTotalElements(), albums.getTotalPages(), albums.isLast());

		List<AlbumResponse> albumResponses = Arrays.asList(modelMapper.map(albums.getContent(), AlbumResponse[].class));
		return new PagedResponse<>(albumResponses, albums.getNumber(), albums.getSize(), albums.getTotalElements(),
				albums.getTotalPages(), albums.isLast());
	}

	@Override
	public ResponseEntity<Album> addAlbum(AlbumRequest request, UserPrincipal currentUser) {
		User user = userRepository.getUser(currentUser);
		Album album = new Album();
		modelMapper.map(request, album);
		album.setUser(user);
		return new ResponseEntity<>(albumRepository.save(album), HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<Album> getAlbum(Long id) {
		Album album = albumRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Album", "id", id));
		return new ResponseEntity<>(album, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<AlbumResponse> updateAlbum(Long id, AlbumRequest newAlbum, UserPrincipal currentUser) {
		User user = userRepository.getUser(currentUser);
		Album album = albumRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Album", "id", id));

		if (album.getCreatedBy().equals(currentUser.getId())
				|| currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
			album.setTitle(newAlbum.getTitle());
			Album updatedAlbum = albumRepository.save(album);
			AlbumResponse response = new AlbumResponse();
			modelMapper.map(updatedAlbum, response);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		throw new BlogapiException(YOU_DON_T_HAVE_PERMISSION_TO_MAKE_THIS_OPERATION, HttpStatus.UNAUTHORIZED);
	}

	@Override
	public ResponseEntity<ApiResponse> deleteAlbum(Long id, UserPrincipal currentUser) {
		Album album = albumRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Albuk", "id", id));
		if (album.getCreatedBy().equals(currentUser.getId())
				|| currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
			albumRepository.deleteById(id);
			return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, "You successfully deleted an album"),
					HttpStatus.OK);
		}

		throw new BlogapiException(YOU_DON_T_HAVE_PERMISSION_TO_MAKE_THIS_OPERATION, HttpStatus.UNAUTHORIZED);
	}

	@Override
	public PagedResponse<Album> getUserAlbums(String username, int page, int size) {
		User user = userRepository.getUserByName(username);
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);
		Page<Album> albums = albumRepository.findByCreatedBy(user.getId(), pageable);
		List<Album> content = albums.getNumberOfElements() > 0 ? albums.getContent() : Collections.emptyList();
		return new PagedResponse<>(content, albums.getNumber(), albums.getSize(), albums.getTotalElements(),
				albums.getTotalPages(), albums.isLast());
	}
}
