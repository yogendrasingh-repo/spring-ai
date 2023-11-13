package org.springframework.ai.core;

import org.junit.jupiter.api.Test;
import org.springframework.ai.client.AiClient;
import org.springframework.ai.client.AiResponse;
import org.springframework.ai.client.Generation;
import org.springframework.ai.prompt.Prompt;
import org.springframework.ai.prompt.PromptTemplate;
import org.springframework.ai.prompt.messages.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.spy;

public class AiTemplateTest {

	private String jokeAnswer = "Why did the cow cross the road? To get to the udder side.";

	private String jokeQuestion = "Tell me a joke about cows";

	@Test
	void test() {
		AiClient aiClient = setupAiClient();
		AiTemplate aiTemplate = new AiTemplate(aiClient);
		String answer = aiTemplate.generate(jokeQuestion);
		assertThat(answer).isEqualTo(jokeAnswer);
	}

	private AiClient setupAiClient() {
		AiClient mockClient = mock(AiClient.class);

		Generation generation = spy(new Generation(this.jokeAnswer));
		AiResponse response = spy(new AiResponse(Collections.singletonList(generation)));
		doCallRealMethod().when(mockClient).generate(anyString());

		doAnswer(invocationOnMock -> {

			Prompt prompt = invocationOnMock.getArgument(0);

			assertThat(prompt).isNotNull();
			assertThat(prompt.getContents()).isEqualTo(jokeQuestion);

			return response;

		}).when(mockClient).generate(any(Prompt.class));
		// AiResponse response = new AiResponse(
		// List.of(new Generation(this.jokeAnswer)));
		// Prompt prompt = new Prompt(jokeQuestion);
		// when(aiClient.generate(prompt)).thenReturn(response);
		return mockClient;
	}

}
