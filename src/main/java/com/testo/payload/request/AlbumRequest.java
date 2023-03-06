package com.testo.payload.request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.testo.model.Photo;
import com.testo.model.user.User;
import com.testo.payload.UserDateAuditPayload;

import lombok.Data;

@Data
public class AlbumRequest extends UserDateAuditPayload {
	private Long id;
	private String title;
	private User user;
	private List<Photo> photo;

	public List<Photo> getPhoto() {
		return photo == null ? null : new ArrayList<>(photo);
	}

	public void setPhoto(List<Photo> photo) {
		if (photo == null)
			this.photo = null;
		else
			this.photo = Collections.unmodifiableList(photo);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
