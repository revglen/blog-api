package com.testo.payload;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.testo.model.Tag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostRequest {
	@NotBlank
	@Size(min = 10)
	private String title;

	@NotBlank
	@Size(min = 5)
	private String body;

	@NotNull
	private Long categoryId;

	private List<Tag> tags;

	public List<Tag> getTags() {

		return tags == null ? Collections.emptyList() : new ArrayList<>(tags);
	}

	public void setTags(List<Tag> tags) {

		if (tags == null) {
			this.tags = null;
		} else {
			this.tags = Collections.unmodifiableList(tags);
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
}
