package de.ms.springai.web;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import de.ms.springai.dto.Answer;
import de.ms.springai.dto.CapitalResponse;
import de.ms.springai.dto.Embedding;
import de.ms.springai.dto.Question;
import de.ms.springai.service.OpenAIService;

@RestController
public class QuestionAPIControllerBean implements QuestionAPIController {

	private final OpenAIService openAIService;

	QuestionAPIControllerBean(final OpenAIService openAIService) {
		this.openAIService = checkNotNull(openAIService, "missing openAIService");
	}

	@Override
	public ResponseEntity<Answer> askQuestion(@RequestBody final Question question) {
		return ResponseEntity.status(HttpStatus.CREATED).body(this.openAIService.getAnswer(question));
	}

	@Override
	public ResponseEntity<Answer> askQuestionFromSimpleVector(@RequestBody final Question question) {
		return ResponseEntity.status(HttpStatus.CREATED).body(this.openAIService.getAnswerFromSimpleVector(question));
	}

	@Override
	public ResponseEntity<CapitalResponse> getCapital(@PathVariable(value = "name") final String name) {
		return ResponseEntity.status(HttpStatus.CREATED).body(this.openAIService.getCapital(name));
	}

	@Override
	public ResponseEntity<Answer> getCapitalWithInfo(@PathVariable(value = "name") final String name) {
		return ResponseEntity.status(HttpStatus.CREATED).body(this.openAIService.getCapitalWithInfo(name));
	}

	@Override
	public ResponseEntity<Answer> askQuestionFromVector(@RequestBody final Question question) {
		return ResponseEntity.status(HttpStatus.CREATED).body(this.openAIService.getAnswerFromVector(question));
	}

	@Override
	public ResponseEntity<Embedding> embedInput(final Question question) {
		return ResponseEntity.status(HttpStatus.CREATED).body(this.openAIService.getEmbeddingFromInput(question));
	}
}
