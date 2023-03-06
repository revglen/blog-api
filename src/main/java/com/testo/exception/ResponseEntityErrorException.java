package com.testo.exception;

import org.springframework.http.ResponseEntity;

import com.testo.payload.ApiResponse;

public class ResponseEntityErrorException extends RuntimeException {

	private static final long serialVersionUID = 6450205512760452624L;
	private transient ResponseEntity<ApiResponse> apiResponse;

	public ResponseEntity<ApiResponse> getApiResponse() {
		return apiResponse;
	}

	public void setApiResponse(ResponseEntity<ApiResponse> apiResponse) {
		this.apiResponse = apiResponse;
	}
}
