package com.testo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.testo.payload.ApiResponse;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private transient ApiResponse apiResponse;
	private String resourceName;
	private String fieldName;
	private Object fieldValue;

	public ResourceNotFoundException(ApiResponse apiResponse) {
		super();
		this.apiResponse = apiResponse;
	}

	public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
		super();
		this.resourceName = resourceName;
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	public ApiResponse getApiResponse() {
		return apiResponse;
	}

	public void setApiResponse() {
		String message = String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue);
		this.apiResponse = new ApiResponse(Boolean.FALSE, message);
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public void setFieldValue(Object fieldValue) {
		this.fieldValue = fieldValue;
	}

}
