package de.ms.springai.configuration;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadVectorStore implements CommandLineRunner {

	private static Logger LOGGER = LoggerFactory.getLogger(LoadVectorStore.class);

	@Autowired
	private final VectorStore vectorStore;
	private final VectorStoreProperties vectorStoreProperties;

	LoadVectorStore(final VectorStore vectorStore, final VectorStoreProperties vectorStoreProperties) {
		this.vectorStore = checkNotNull(vectorStore, "missing vectorStore");
		this.vectorStoreProperties = checkNotNull(vectorStoreProperties, "missing vectorStoreProperties");
	}

	@Override
	public void run(final String... args) throws Exception {
		if (this.vectorStore.similaritySearch("Sportsman").isEmpty()) {
			LOGGER.info("Loading documents into vector store");

			this.vectorStoreProperties.getDocumentsToLoad().forEach(document -> {
				System.out.println("Loading document: " + document.getFilename());

				final TikaDocumentReader documentReader = new TikaDocumentReader(document);
				final List<Document> documents = documentReader.get();

				final TextSplitter textSplitter = new TokenTextSplitter();

				final List<Document> splitDocuments = textSplitter.apply(documents);

				this.vectorStore.add(splitDocuments);
			});
		}

		LOGGER.info("Vector store loaded");
	}
}
