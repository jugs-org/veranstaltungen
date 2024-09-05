package de.ms.springai.service;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.parser.BeanOutputParser;
import org.springframework.ai.retry.NonTransientAiException;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import de.ms.springai.dto.Answer;
import de.ms.springai.dto.CapitalResponse;
import de.ms.springai.dto.Embedding;
import de.ms.springai.dto.Question;
import de.ms.springai.web.exception.ResourceException;

@Service
public class OpenAIServiceBean implements OpenAIService {

	private final ChatModel chatClient;
	private final Resource capitalResource;
	private final Resource capitalResourceWithInfo;
	private final Resource systemMessageTemplate;
	private final SimpleVectorStore simpleVectorStore;
	private final VectorStore vectorStore;
	private final Resource ragPromptResource;
	private final Resource ragPromptResourceDefault;
	private final OpenAiEmbeddingModel embeddingClient;

	OpenAIServiceBean(final ChatModel chatClient,
			@Value("classpath:templates/getCapitalPrompt.st") final Resource capitalResource,
			@Value("classpath:templates/getCapitalWithInfoPrompt.st") final Resource capitalResourceWithInfo,
			@Value("classpath:templates/system-message.st") final Resource systemMessageTemplate,
			final SimpleVectorStore simpleVectorStore, final VectorStore vectorStore,
			@Value("classpath:templates/rag-prompt-template.st") final Resource ragPromptResource,
			@Value("classpath:templates/rag-prompt-template-default.st") final Resource ragPromptResourceDefault,
			final OpenAiEmbeddingModel embeddingClient) {
		this.chatClient = checkNotNull(chatClient, "missing chatClient");
		this.capitalResource = checkNotNull(capitalResource, "missing capitalResource");
		this.capitalResourceWithInfo = checkNotNull(capitalResourceWithInfo, "missing capitalResourceWithInfo");
		this.systemMessageTemplate = checkNotNull(systemMessageTemplate, "missing systemMessageTemplate");
		this.simpleVectorStore = checkNotNull(simpleVectorStore, "missing simpleVectorStore");
		this.vectorStore = checkNotNull(vectorStore, "missing vectorStore");
		this.ragPromptResource = checkNotNull(ragPromptResource, "missing ragPromptResource");
		this.ragPromptResourceDefault = checkNotNull(ragPromptResourceDefault, "missing ragPromptResourceDefault");
		this.embeddingClient = checkNotNull(embeddingClient, "missing embeddingClient");
	}

	@Override
	public Answer getAnswer(final Question question) {
		try {
			final PromptTemplate promptTemplate = new PromptTemplate(question.question());
			final Prompt promt = promptTemplate.create();
			final org.springframework.ai.chat.model.ChatResponse response = this.chatClient.call(promt);
			return new Answer(response.getResult().getOutput().getContent());
		} catch (final NonTransientAiException e) {
			throw new ResourceException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@Override
	public Answer getAnswerFromSimpleVector(final Question question) {
		try {
			final List<Document> documents = this.simpleVectorStore
					.similaritySearch(SearchRequest.query(question.question()).withTopK(4));

			final List<String> contentList = documents.stream().map(Document::getContent).toList();

			final PromptTemplate promptTemplate = new PromptTemplate(this.ragPromptResource);
			final Prompt promt = promptTemplate
					.create(Map.of("input", question.question(), "documents", String.join("\n", contentList)));

			contentList.forEach(System.out::println);

			final org.springframework.ai.chat.model.ChatResponse response = this.chatClient.call(promt);
			return new Answer(response.getResult().getOutput().getContent());
		} catch (final NonTransientAiException e) {
			throw new ResourceException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@Override
	public CapitalResponse getCapital(final String name) {
		try {
			final BeanOutputParser<CapitalResponse> parser = new BeanOutputParser<>(CapitalResponse.class);
			final String format = parser.getFormat();

			final PromptTemplate promptTemplate = new PromptTemplate(this.capitalResource);
			final Prompt promt = promptTemplate.create(Map.of("stateOrCountry", name, "format", format));
			final org.springframework.ai.chat.model.ChatResponse response = this.chatClient.call(promt);

			return parser.parse(response.getResult().getOutput().getContent());
		} catch (final NonTransientAiException e) {
			throw new ResourceException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@Override
	public Answer getCapitalWithInfo(final String name) {
		try {
			final PromptTemplate promptTemplate = new PromptTemplate(this.capitalResourceWithInfo);
			final Prompt prompt = promptTemplate.create(Map.of("stateOrCountry", name));
			final org.springframework.ai.chat.model.ChatResponse response = this.chatClient.call(prompt);
			return new Answer(response.getResult().getOutput().getContent());
		} catch (final NonTransientAiException e) {
			throw new ResourceException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@Override
	public Answer getAnswerFromVector(final Question question) {
		try {
			final PromptTemplate systemMessagePromptTemplate = new SystemPromptTemplate(this.systemMessageTemplate);
			final Message systemMessage = systemMessagePromptTemplate.createMessage();

			final List<Document> documents = this.vectorStore
					.similaritySearch(SearchRequest.query(question.question()).withTopK(5));
			final List<String> contentList = documents.stream().map(Document::getContent).toList();

			final PromptTemplate promptTemplate = new PromptTemplate(this.ragPromptResourceDefault);
			final Message userMessage = promptTemplate
					.createMessage(Map.of("input", question.question(), "documents", String.join("\n", contentList)));

			// contentList.forEach(System.out::println);

			final org.springframework.ai.chat.model.ChatResponse response = this.chatClient
					.call(new Prompt(List.of(systemMessage, userMessage)));

			return new Answer(response.getResult().getOutput().getContent());
		} catch (final NonTransientAiException e) {
			throw new ResourceException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@Override
	public Embedding getEmbeddingFromInput(final Question question) {
		return new Embedding(this.embeddingClient.embed(question.question()));
	}
}
