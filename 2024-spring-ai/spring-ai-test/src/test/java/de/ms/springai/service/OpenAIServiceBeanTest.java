package de.ms.springai.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import de.ms.springai.dto.Answer;
import de.ms.springai.dto.Question;
import de.ms.springai.web.WebPackageScanMarker;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, properties = {
		/*"spring.ai.openai.api-key:..."*/ })
@ContextConfiguration(classes = OpenAIServiceBeanTest.TestConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class OpenAIServiceBeanTest {

	@ComponentScan(basePackageClasses = { WebPackageScanMarker.class, ServicePackageScanMarker.class, })
	@Configuration
	@EnableAutoConfiguration
	static class TestConfiguration {

	}

	@Autowired
	OpenAIService openAIService;

	@Test
	void getAnswer() {
		final Answer answer = this.openAIService
				.getAnswer(new Question("Write a javaScript to output numbers von 0 to 99."));
		System.out.println("Got the answer");
		System.out.println(answer);
	}

}
