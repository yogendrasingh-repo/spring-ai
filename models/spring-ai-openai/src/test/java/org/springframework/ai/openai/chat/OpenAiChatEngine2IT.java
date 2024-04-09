package org.springframework.ai.openai.chat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.engine2.*;
import org.springframework.ai.chat.history.ChatHistory;
import org.springframework.ai.chat.history.InMemoryChatHistory;
import org.springframework.ai.chat.history.TokenWindowChatHistoryRetriever;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.OpenAiEmbeddingClient;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = OpenAiChatEngine2IT.Config.class)
@EnabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches = ".+")
public class OpenAiChatEngine2IT {

	private final ChatClient chatClient;

	private final VectorStore vectorStore;

	private final List<Retriever> retrievers;

	private final List<Augmentor> augmentors;

	private final Generator generator;

	private final EngineListener engineListener;

	@Value("classpath:/data/acme/bikes.json")
	private Resource bikesResource;

	@Autowired
	OpenAiChatEngine2IT(OpenAiChatClient openAiChatClient, VectorStore vectorStore, List<Retriever> retrievers,
			List<Augmentor> augmentors, Generator generator, EngineListener engineListener) {
		this.chatClient = openAiChatClient;
		this.vectorStore = vectorStore;
		this.retrievers = retrievers;
		this.augmentors = augmentors;
		this.generator = generator;
		this.engineListener = engineListener;
	}

	@Test
	void loadAndChat() {
		JsonReader jsonReader = new JsonReader(bikesResource, "name", "price", "shortDescription", "description");
		var textSplitter = new TokenTextSplitter();
		vectorStore.accept(textSplitter.apply(jsonReader.get()));

		ChatEngine2 chatEngine2 = new ChatEngine2(retrievers, augmentors, generator, engineListener);

		String systemMessageText = """
				You're assisting with questions about products in a bicycle catalog.
				The the answer involves referring to the price or the dimension of the bicycle, include the bicycle name in the response.
				If unsure, simply state that you don't know.
				""";
		SystemMessage systemMessage = new SystemMessage(systemMessageText);
		UserMessage userMessage = new UserMessage("What bike is good for city commuting?");
		Prompt prompt = new Prompt(List.of(userMessage, systemMessage));

		EngineRequest2 engineRequest2 = new EngineRequest2("test-session-1", prompt);

		EngineResponse2 engineResponse2 = chatEngine2.call(engineRequest2);

		System.out.println(engineResponse2);

	}

	@SpringBootConfiguration
	static class Config {

		@Bean
		public OpenAiApi chatCompletionApi() {
			return new OpenAiApi(System.getenv("OPENAI_API_KEY"));
		}

		@Bean
		public OpenAiChatClient openAiClient(OpenAiApi openAiApi) {
			return new OpenAiChatClient(openAiApi);
		}

		@Bean
		public OpenAiEmbeddingClient embeddingClient(OpenAiApi openAiApi) {
			return new OpenAiEmbeddingClient(openAiApi);
		}

		@Bean
		public VectorStore vectorStore(EmbeddingClient embeddingClient) {
			return new SimpleVectorStore(embeddingClient);
		}

		@Bean
		public ChatHistory chatHistory() {
			return new InMemoryChatHistory();
		}

		@Bean
		public List<Retriever> retrievers(VectorStore vectorStore, ChatHistory chatHistory) {
			VectorStoreRetriever vectorStoreRetriever = new VectorStoreRetriever(vectorStore, SearchRequest.defaults());
			TokenWindowChatExchangeRetriever tokenWindowChatExchangeRetriever = new TokenWindowChatExchangeRetriever(
					chatHistory, 100);
			List<Retriever> retrievers = List.of(vectorStoreRetriever, tokenWindowChatExchangeRetriever);
			return retrievers;
		}

		@Bean
		public List<Augmentor> augmentors() {
			UserMessageAugmentor userMessageAugmentor = new UserMessageAugmentor();
			ChatExchangeAugmenter chatExchangeAugmenter = new ChatExchangeAugmenter();
			List<Augmentor> augmentors = List.of(userMessageAugmentor, chatExchangeAugmenter);
			return augmentors;
		}

		@Bean
		public Generator generator(ChatClient chatClient) {
			return new ChatClientGenerator(chatClient);
		}

		@Bean
		public EngineListener engineListener(ChatHistory chatHistory) {
			return new ChatExchangeEngineListener(chatHistory);
		}

	}

}
