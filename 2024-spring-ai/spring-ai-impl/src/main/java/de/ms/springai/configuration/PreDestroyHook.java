package de.ms.springai.configuration;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;

@Component
public class PreDestroyHook {

	private final Path vectorTMP;

	PreDestroyHook(@Value("${sfg.aiapp.vectorStorePath}") final Path vectorTMP) {
		this.vectorTMP = checkNotNull(vectorTMP, "missing vectorTMP");
	}

	@PreDestroy
	public void onShutdown() {
		try {
			Files.delete(this.vectorTMP);
		} catch (final IOException e) {

		}
	}
}