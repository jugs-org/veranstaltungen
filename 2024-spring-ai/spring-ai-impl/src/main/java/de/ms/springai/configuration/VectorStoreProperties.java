package de.ms.springai.configuration;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@ConfigurationProperties(prefix = "sfg.aiapp")
public class VectorStoreProperties {

	private String vectorStorePath;
	private List<Resource> documentsToLoad;

	public String getVectorStorePath() {
		return this.vectorStorePath;
	}

	public void setVectorStorePath(final String vectorStorePath) {
		this.vectorStorePath = vectorStorePath;
	}

	public List<Resource> getDocumentsToLoad() {
		return this.documentsToLoad;
	}

	public void setDocumentsToLoad(final List<Resource> documentsToLoad) {
		this.documentsToLoad = documentsToLoad;
	}
}
