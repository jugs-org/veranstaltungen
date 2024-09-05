package de.ms.springai.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import de.ms.springai.dto.Answer;
import de.ms.springai.dto.CapitalResponse;
import de.ms.springai.dto.Embedding;
import de.ms.springai.dto.Question;

@RequestMapping(value = "/ask")
public interface QuestionAPIController {

	@PostMapping
	public ResponseEntity<Answer> askQuestion(@RequestBody final Question question);

	@PostMapping(value = "/simpleVector")
	ResponseEntity<Answer> askQuestionFromSimpleVector(Question question);

	@PostMapping(value = "/capital/{name}")
	public ResponseEntity<CapitalResponse> getCapital(@PathVariable(value = "name") final String name);

	@PostMapping(value = "/capital/{name}/info")
	public ResponseEntity<Answer> getCapitalWithInfo(@PathVariable(value = "name") final String name);

	@PostMapping(value = "/vector")
	ResponseEntity<Answer> askQuestionFromVector(Question question);

	@PostMapping(value = "/embed")
	ResponseEntity<Embedding> embedInput(@RequestBody Question question);

}
