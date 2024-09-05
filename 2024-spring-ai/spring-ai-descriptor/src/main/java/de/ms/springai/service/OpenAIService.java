package de.ms.springai.service;

import de.ms.springai.dto.Answer;
import de.ms.springai.dto.CapitalResponse;
import de.ms.springai.dto.Embedding;
import de.ms.springai.dto.Question;

public interface OpenAIService {

	Answer getAnswer(final Question question);

	Answer getAnswerFromSimpleVector(final Question question);

	CapitalResponse getCapital(final String name);

	Answer getCapitalWithInfo(final String name);

	Answer getAnswerFromVector(final Question question);

	Embedding getEmbeddingFromInput(final Question question);
}
