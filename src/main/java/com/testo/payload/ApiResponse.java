package com.testo.payload;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({ "success", "message" })
public class ApiResponse implements Serializable {

	@JsonIgnore
	private static final long serialVersionUID = -4973470545587328668L;

	@JsonProperty("success")
	private boolean success;

	@JsonProperty("message")
	private String message;

	@JsonIgnore
	private HttpStatus status;

	public ApiResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ApiResponse(boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}

	public ApiResponse(boolean success, String message, HttpStatus status) {
		super();
		this.success = success;
		this.message = message;
		this.status = status;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
