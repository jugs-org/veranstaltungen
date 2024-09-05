package de.ms.springai.web.exception;

import org.springframework.http.HttpStatus;

public class ResourceException extends RuntimeException {

	private static final long serialVersionUID = -806227168647240901L;

	private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

	public HttpStatus getHttpStatus() {
		return this.httpStatus;
	}

	public ResourceException(final HttpStatus httpStatus, final String message) {
		super(message);
		this.httpStatus = httpStatus;
	}
}
