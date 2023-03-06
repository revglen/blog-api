package com.testo.exception;

import org.springframework.http.HttpStatus;

public class BlogapiException extends RuntimeException {

	private static final long serialVersionUID = 4450205512760452624L;
	private String message;
	private HttpStatus status;

	public BlogapiException(String message, HttpStatus status) {
		super();
		this.message = message;
		this.status = status;
	}

	public BlogapiException(String message, HttpStatus status, Throwable cause) {
		super(cause);
		this.message = message;
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public HttpStatus getStatus() {
		return status;
	}
}
