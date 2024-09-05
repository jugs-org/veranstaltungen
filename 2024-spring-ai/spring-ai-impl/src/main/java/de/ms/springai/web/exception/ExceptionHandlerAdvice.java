package de.ms.springai.web.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@ControllerAdvice
public class ExceptionHandlerAdvice {

	@ExceptionHandler(ResourceException.class)
	public ResponseEntity<ObjectNode> handleException(final ResourceException e) {
		final JsonNodeFactory factory = JsonNodeFactory.instance;
		final ObjectNode root = factory.objectNode();
		root.put(e.getHttpStatus().toString(), e.getMessage());

		return ResponseEntity.status(e.getHttpStatus()).body(root);
	}
}
