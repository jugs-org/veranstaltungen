package de.ms.springai.configuration;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VectorStoreConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(VectorStoreConfig.class);

	@Bean
	SimpleVectorStore simpleVectorStore(final EmbeddingModel embeddingModel,
			final VectorStoreProperties vectorStoreProperties) {
		final SimpleVectorStore store = new SimpleVectorStore(embeddingModel);
		final File vectorStoreFile = new File(vectorStoreProperties.getVectorStorePath());
		if (vectorStoreFile.exists()) {
			store.load(vectorStoreFile);
		} else {
			LOGGER.debug("Loading documents into vector store");
			vectorStoreProperties.getDocumentsToLoad().forEach(document -> {
				try {
					System.out.println(document.getURL());
				} catch (final IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				LOGGER.debug("Loading document: " + document.getFilename());
				final TikaDocumentReader documentReader = new TikaDocumentReader(document);
				final List<Document> docs = documentReader.get();
				final TextSplitter textSplitter = new TokenTextSplitter();
				final List<Document> splitDocs = textSplitter.apply(docs);
				store.add(splitDocs);
			});

			store.save(vectorStoreFile);
		}
		return store;
	}
}
