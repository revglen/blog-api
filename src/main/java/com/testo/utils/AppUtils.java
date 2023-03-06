package com.testo.utils;

import org.springframework.http.HttpStatus;

import com.testo.exception.BlogapiException;

public class AppUtils {
	public static void validatePageNumberAndSize(int page, int size) {

		if (page < 0)
			throw new BlogapiException("Page number cannot be less than zero.", HttpStatus.BAD_REQUEST);

		if (size < 0)
			throw new BlogapiException("Size number cannot be less than zero.", HttpStatus.BAD_REQUEST);

		if (size > AppConstants.MAX_PAGE_SIZE)
			throw new BlogapiException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE,
					HttpStatus.BAD_REQUEST);
	}
}
