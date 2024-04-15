package org.springframework.ai.openai.chat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.call.ChatCall;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.ChatOptionsBuilder;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = OpenAiChatCallIT.Config.class)
@EnabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches = ".+")
public class OpenAiChatCallIT {

	private final ChatClient chatClient;

	@Autowired
	public OpenAiChatCallIT(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@Test
	void userMessage() {
		ChatCall chatCall = ChatCall.builder(chatClient)
			.withUserString("Tell me a {adjective} joke about {topic}")
			.build();
		String joke = chatCall.execute(Map.of("adjective", "silly", "topic", "cows"));
		System.out.println(joke);
	}

	@Test
	void testUserAndSystemMessage() {
		String userString = """
				Tell me about three famous {occupation} and what they did.
				Write at least three sentences for each person.
				""";
		String systemString = """
				 You are a helpful AI assistant.
				 You are an AI assistant that helps people find information.
				 Your name is {name}
				 You should reply to the user's request with your name and also in the style of a {voice}.
				""";

		ChatCall chatCall = ChatCall.builder(chatClient)
			.withUserString(userString)
			.withSystemString(systemString)
			.withSystemMap(Map.of("name", "Rick", "voice", "Rick Sanchez"))
			.build();

		Map<String, Object> userMap = new HashMap<>();
		userMap.put("occupation", "scientists");

		System.out.println("Using default temperature");
		String answer = chatCall.execute(userMap);
		System.out.println(answer);

		ChatOptions chatOptions = ChatOptionsBuilder.builder().withTemperature(1.0f).build();
		chatCall = ChatCall.builder(chatClient)
			.withUserString(userString)
			.withSystemString(systemString)
			.withChatOptions(chatOptions)
			.build();

		System.out.println("Using temperature 1.0");
		answer = chatCall.execute(userMap, Map.of("name", "Rick", "voice", "Rick Sanchez"));
		System.out.println(answer);
	}

	@SpringBootConfiguration
	static class Config {

		@Bean
		public OpenAiApi chatCompletionApi() {
			return new OpenAiApi(System.getenv("OPENAI_API_KEY"));
		}

		@Bean
		public ChatClient openAiClient(OpenAiApi openAiApi) {
			return new OpenAiChatClient(openAiApi);
		}

	}

}