package de.ms.springai.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.ms.springai.dto.Answer;

@RequestMapping(value = "/resources")
public interface ResourcesAPIController {

	@PostMapping(value = "/originalCSV")
	ResponseEntity<Answer> activateOriginal();

	@PostMapping(value = "/modifiedCSV")
	public ResponseEntity<Void> activateModified();

}
