package com.testo.service;

import com.testo.model.Tag;
import com.testo.payload.ApiResponse;
import com.testo.payload.PagedResponse;
import com.testo.security.UserPrincipal;

public interface TagService {
	PagedResponse<Tag> getAllTags(int page, int size);

	Tag getTag(Long id);

	Tag addTag(Tag tag, UserPrincipal currentUser);

	Tag updateTag(Long id, Tag newTag, UserPrincipal currentUser);

	ApiResponse deleteTag(Long id, UserPrincipal currentUser);
}
