package com.testo.payload;

import lombok.Data;

@Data
public class JwtAuthenticationResponse {

	private String accessToken;
	private String tokenType = "Bearer";

	public String getAccessToken() {
		return accessToken;
	}

	public JwtAuthenticationResponse(String accessToken) {
		super();
		this.accessToken = accessToken;
	}
}
