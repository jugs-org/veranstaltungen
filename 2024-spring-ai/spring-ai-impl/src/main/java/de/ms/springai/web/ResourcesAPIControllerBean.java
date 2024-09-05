package de.ms.springai.web;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.restart.RestartEndpoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import de.ms.springai.dto.Answer;
import de.ms.springai.web.exception.ResourceException;

@RestController
public class ResourcesAPIControllerBean implements ResourcesAPIController {

	private final Path vectorTMP;
	private final RestartEndpoint restartEndpoint;

	ResourcesAPIControllerBean(@Value("${sfg.aiapp.vectorStorePath}") final Path vectorTMP,
			final RestartEndpoint restartEndpoint) {
		this.vectorTMP = checkNotNull(vectorTMP, "missing vectorTMP");
		this.restartEndpoint = checkNotNull(restartEndpoint, "missing restartEndpoint");
	}

	@Override
	public ResponseEntity<Answer> activateOriginal() {
//		final Path original = Paths.get("/home/maik/git/spring-ai/spring-ai-impl/src/main/resources/movies500.csv");
//		final Path modified = Paths
//				.get("/home/maik/git/spring-ai/spring-ai-impl/src/main/resources/movies500original.csv");
		final Path original = Paths.get(this.getClass().getClassLoader().getResource("movies500.csv").getFile());
		final Path modified = Paths
				.get(this.getClass().getClassLoader().getResource("movies500original.csv").getFile());

		try {
			Files.delete(original);
			Files.copy(modified, original);
			Files.delete(this.vectorTMP);
			final Thread restartThread = new Thread(() -> this.restartEndpoint.doRestart());
			restartThread.setDaemon(false);
			restartThread.start();
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (final IOException e) {
			throw new ResourceException(HttpStatus.INTERNAL_SERVER_ERROR,
					"failed to switch between modified and original");
		}
	}

	@Override
	public ResponseEntity<Void> activateModified() {

		final Path original = Paths.get(this.getClass().getClassLoader().getResource("movies500.csv").getFile());
		final Path modified = Paths
				.get(this.getClass().getClassLoader().getResource("movies500modified.csv").getFile());

		try {
			Files.delete(original);
			Files.copy(modified, original);
			Files.delete(this.vectorTMP);
			final Thread restartThread = new Thread(() -> this.restartEndpoint.doRestart());
			restartThread.setDaemon(false);
			restartThread.start();
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (final IOException e) {
			throw new ResourceException(HttpStatus.INTERNAL_SERVER_ERROR,
					"failed to switch between modified and original");
		}
	}
}
