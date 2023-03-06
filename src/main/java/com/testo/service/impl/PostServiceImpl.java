package com.testo.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.testo.exception.ResourceNotFoundException;
import com.testo.exception.UnauthorizedException;
import com.testo.model.Category;
import com.testo.model.Post;
import com.testo.model.Tag;
import com.testo.model.role.RoleName;
import com.testo.model.user.User;
import com.testo.payload.ApiResponse;
import com.testo.payload.PagedResponse;
import com.testo.payload.PostRequest;
import com.testo.payload.PostResponse;
import com.testo.repository.CategoryRepository;
import com.testo.repository.PostRepository;
import com.testo.repository.TagRepository;
import com.testo.repository.UserRepository;
import com.testo.security.UserPrincipal;
import com.testo.utils.AppConstants;
import com.testo.utils.AppUtils;

@Service
public class PostServiceImpl implements com.testo.service.PostService {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private TagRepository tagRepository;

	@Override
	public PagedResponse<PostResponse> getAllPosts(int page, int size) {
		AppUtils.validatePageNumberAndSize(page, size);
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, AppConstants.CREATED_AT);
		Page<Post> posts = postRepository.findAll(pageable);
		if (posts.getNumberOfElements() == 0)
			return new PagedResponse<>(Collections.emptyList(), posts.getNumber(), posts.getSize(),
					posts.getTotalElements(), posts.getTotalPages(), posts.isLast());

		// List<Post> postResponses = posts.getNumberOfElements() == 0 ?
		// Collections.emptyList() : posts.getContent();
		List<PostResponse> postResponses = new ArrayList<>();
		for (Post p : posts) {
			PostResponse pp = new PostResponse();
			pp.setPostId(p.getId());
			pp.setBody(p.getBody());
			pp.setTitle(p.getTitle());
			if (p.getCategory() != null) {
				pp.setCategoryName(p.getCategory().getName());
				pp.setCategory(p.getCategory().getId());
			}

			List<String> tagNames = new ArrayList<>(p.getTags().size());
			for (Tag tag : p.getTags()) {
				tagNames.add(tag.getName());
			}
			pp.setTags(tagNames);
			postResponses.add(pp);
		}

		return new PagedResponse<>(postResponses, posts.getNumber(), posts.getSize(), posts.getTotalElements(),
				posts.getTotalPages(), posts.isLast());
	}

	@Override
	public PagedResponse<PostResponse> getPostByCreatedBy(String username, int page, int size) {
		AppUtils.validatePageNumberAndSize(page, size);
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, AppConstants.CREATED_AT);
		Optional<User> userOptional = userRepository.findByUsername(username);
		User user = userOptional.get();

		Page<Post> posts = postRepository.findByCreatedBy(user.getId(), pageable);
		if (posts.getNumberOfElements() == 0)
			return new PagedResponse<>(Collections.emptyList(), posts.getNumber(), posts.getSize(),
					posts.getTotalElements(), posts.getTotalPages(), posts.isLast());

		// List<Post> postResponses = posts.getNumberOfElements() == 0 ?
		// Collections.emptyList() : posts.getContent();
		List<PostResponse> postResponses = new ArrayList<>();
		for (Post p : posts) {
			PostResponse pp = new PostResponse();
			pp.setPostId(p.getId());
			pp.setBody(p.getBody());
			pp.setTitle(p.getTitle());
			if (p.getCategory() != null) {
				pp.setCategoryName(p.getCategory().getName());
				pp.setCategory(p.getCategory().getId());
			}

			List<String> tagNames = new ArrayList<>(p.getTags().size());
			for (Tag tag : p.getTags()) {
				tagNames.add(tag.getName());
			}
			pp.setTags(tagNames);
			postResponses.add(pp);
		}

		return new PagedResponse<>(postResponses, posts.getNumber(), posts.getSize(), posts.getTotalElements(),
				posts.getTotalPages(), posts.isLast());
	}

	@Override
	public PagedResponse<PostResponse> getPostByCategory(Long id, int page, int size) {
		AppUtils.validatePageNumberAndSize(page, size);
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, AppConstants.CREATED_AT);

		Page<Post> posts = postRepository.findByCategoryId(id, pageable);
		if (posts.getNumberOfElements() == 0)
			return new PagedResponse<>(Collections.emptyList(), posts.getNumber(), posts.getSize(),
					posts.getTotalElements(), posts.getTotalPages(), posts.isLast());

		// List<Post> postResponses = posts.getNumberOfElements() == 0 ?
		// Collections.emptyList() : posts.getContent();
		List<PostResponse> postResponses = new ArrayList<>();
		for (Post p : posts) {
			PostResponse pp = new PostResponse();
			pp.setPostId(p.getId());
			pp.setBody(p.getBody());
			pp.setTitle(p.getTitle());
			if (p.getCategory() != null) {
				pp.setCategoryName(p.getCategory().getName());
				pp.setCategory(p.getCategory().getId());
			}

			List<String> tagNames = new ArrayList<>(p.getTags().size());
			for (Tag tag : p.getTags()) {
				tagNames.add(tag.getName());
			}
			pp.setTags(tagNames);
			postResponses.add(pp);
		}

		return new PagedResponse<>(postResponses, posts.getNumber(), posts.getSize(), posts.getTotalElements(),
				posts.getTotalPages(), posts.isLast());
	}

	@Override
	public PagedResponse<PostResponse> getPostByTag(Long id, int page, int size) {
		AppUtils.validatePageNumberAndSize(page, size);
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, AppConstants.CREATED_AT);

		Tag tag = tagRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.TAG, AppConstants.ID, id));
		Page<Post> posts = postRepository.findByTags(id, pageable);

		if (posts.getNumberOfElements() == 0)
			return new PagedResponse<>(Collections.emptyList(), posts.getNumber(), posts.getSize(),
					posts.getTotalElements(), posts.getTotalPages(), posts.isLast());

		List<PostResponse> postResponses = new ArrayList<>();
		for (Post p : posts) {
			PostResponse pp = new PostResponse();
			pp.setPostId(p.getId());
			pp.setBody(p.getBody());
			pp.setTitle(p.getTitle());
			if (p.getCategory() != null) {
				pp.setCategoryName(p.getCategory().getName());
				pp.setCategory(p.getCategory().getId());
			}

			List<String> tagNames = new ArrayList<>(p.getTags().size());
			for (Tag t : p.getTags()) {
				tagNames.add(t.getName());
			}
			pp.setTags(tagNames);
			postResponses.add(pp);
		}

		// List<PostResponse> postResponses = posts.getNumberOfElements() == 0 ?
		// Collections.emptyList() : posts.getContent();
		return new PagedResponse<>(postResponses, posts.getNumber(), posts.getSize(), posts.getTotalElements(),
				posts.getTotalPages(), posts.isLast());
	}

	@Override
	public Post updatePost(Long id, PostRequest newPost, UserPrincipal currentUser) {
		Post post = postRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.POST, AppConstants.ID, id));
		Category category = categoryRepository.findById(newPost.getCategoryId()).orElseThrow(
				() -> new ResourceNotFoundException(AppConstants.CATEGORY, AppConstants.ID, newPost.getCategoryId()));
		if (post.getUser().getId().equals(currentUser.getId())
				|| currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
			post.setTitle(newPost.getTitle());
			post.setBody(newPost.getBody());
			// post.setCategory(category);
			return postRepository.save(post);
		}

		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to edit this post");
		throw new UnauthorizedException(apiResponse);
	}

	@Override
	public ResponseEntity<ApiResponse> deletePost(Long id, UserPrincipal currentUser) {
		Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
		if (post.getCreatedBy().equals(currentUser.getId())
				|| currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
			postRepository.deleteById(id);
			return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, "You successfully deleted a post"),
					HttpStatus.OK);
		}

		throw new UnauthorizedException("You don't have permission to delete this post");
	}

	@Override
	public PostResponse addPost(PostRequest postRequest, UserPrincipal currentUser) {
		User user = userRepository.findById(currentUser.getId())
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER, AppConstants.ID, 1L));

		Category category = categoryRepository.findById(postRequest.getCategoryId())
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.CATEGORY, AppConstants.ID,
						postRequest.getCategoryId()));

		List<Tag> tags = new ArrayList<>(postRequest.getTags().size());
		for (Tag name : postRequest.getTags()) {
			Tag tag = tagRepository.findByName(name.getName());
			tag = tag == null ? tagRepository.save(new Tag(name.getName())) : tag;
			tags.add(tag);
		}

		Post post = new Post();
		post.setBody(postRequest.getBody());
		post.setTitle(postRequest.getTitle());
		post.setCategory(category);
		post.setUser(user);
		post.setTags(tags);
		Post newPost = postRepository.save(post);

		PostResponse postResponse = new PostResponse();
		postResponse.setPostId(newPost.getId());
		postResponse.setTitle(newPost.getTitle());
		postResponse.setBody(newPost.getBody());
		postResponse.setCategoryName(newPost.getCategory().getName());
		postResponse.setCategory(newPost.getCategory().getId());

		List<String> tagNames = new ArrayList<>(newPost.getTags().size());

		for (Tag tag : newPost.getTags()) {
			tagNames.add(tag.getName());
		}

		postResponse.setTags(tagNames);
		return postResponse;
	}

	@Override
	public PostResponse getPost(Long id) {
		Post p = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

		PostResponse pp = new PostResponse();
		pp.setPostId(p.getId());
		pp.setBody(p.getBody());
		pp.setTitle(p.getTitle());
		if (p.getCategory() != null) {
			pp.setCategoryName(p.getCategory().getName());
			pp.setCategory(p.getCategory().getId());
		}

		List<String> tagNames = new ArrayList<>(p.getTags().size());
		for (Tag tag : p.getTags()) {
			tagNames.add(tag.getName());
		}
		pp.setTags(tagNames);

		return pp;
	}
}
