package com.testo.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.testo.exception.ResourceNotFoundException;
import com.testo.exception.UnauthorizedException;
import com.testo.model.Tag;
import com.testo.model.role.RoleName;
import com.testo.payload.ApiResponse;
import com.testo.payload.PagedResponse;
import com.testo.repository.TagRepository;
import com.testo.security.UserPrincipal;
import com.testo.service.TagService;
import com.testo.utils.AppUtils;

@Service
public class TagServiceImpl implements TagService {
	private static final String CREATED_AT = "createdAt";

	@Autowired
	private TagRepository tagRepository;

	@Override
	public PagedResponse<Tag> getAllTags(int page, int size) {
		AppUtils.validatePageNumberAndSize(page, size);
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);
		Page<Tag> tags = tagRepository.findAll(pageable);
		if (tags.getNumberOfElements() == 0)
			return new PagedResponse<>(Collections.emptyList(), tags.getNumber(), tags.getSize(),
					tags.getTotalElements(), tags.getTotalPages(), tags.isLast());

		List<Tag> tagResponses = tags.getNumberOfElements() == 0 ? Collections.emptyList() : tags.getContent();
		return new PagedResponse<>(tagResponses, tags.getNumber(), tags.getSize(), tags.getTotalElements(),
				tags.getTotalPages(), tags.isLast());
	}

	@Override
	public Tag getTag(Long id) {
		Tag tag = tagRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tag", "id", id));
		return tag;
	}

	@Override
	public Tag updateTag(Long id, Tag newTag, UserPrincipal currentUser) {
		Tag tag = tagRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tag", "id", id));
		if (tag.getCreatedBy().equals(currentUser.getId())
				|| currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
			tag.setName(newTag.getName());
			return tagRepository.save(tag);
		}
		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to edit this tag");

		throw new UnauthorizedException(apiResponse);
	}

	@Override
	public ApiResponse deleteTag(Long id, UserPrincipal currentUser) {
		Tag tag = tagRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tag", "id", id));
		if (tag.getCreatedBy().equals(currentUser.getId())
				|| currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
			tagRepository.deleteById(id);
			return new ApiResponse(Boolean.TRUE, "You successfully deleted a tag");
		}
		throw new UnauthorizedException("You don't have permission to delete this tag");
	}

	@Override
	public Tag addTag(Tag tag, UserPrincipal currentUser) {
		return tagRepository.save(tag);
	}
}