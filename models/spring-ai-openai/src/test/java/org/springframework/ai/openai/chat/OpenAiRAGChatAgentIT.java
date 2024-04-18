package org.springframework.ai.openai.chat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.agent.AgentRequest;
import org.springframework.ai.chat.agent.DefaultChatAgent;
import org.springframework.ai.chat.agent.retriever.VectorStoreRetriever;
import org.springframework.ai.chat.agent.transformer.QAUserPromptTransformer;
import org.springframework.ai.chat.evaluation.EvaluationRequest;
import org.springframework.ai.chat.evaluation.EvaluationResponse;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
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

@SpringBootTest(classes = OpenAiRAGChatAgentIT.Config.class)
@EnabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches = ".+")
public class OpenAiRAGChatAgentIT {

	private final ChatClient chatClient;

	private final VectorStore vectorStore;

	@Value("classpath:/data/acme/bikes.json")
	private Resource bikesResource;

	@Autowired
	public OpenAiRAGChatAgentIT(ChatClient chatClient, VectorStore vectorStore) {
		this.chatClient = chatClient;
		this.vectorStore = vectorStore;
	}

	@Test
	void simpleChat() {
		loadData();
		var chatAgent = new DefaultChatAgent.Builder().withChatClient(chatClient)
			.withDataRetriever(new VectorStoreRetriever(vectorStore, SearchRequest.defaults()))
			.withPromptTransformer(new QAUserPromptTransformer())
			.build();

		var agentRequest = new AgentRequest("What bike is good for city commuting?");
		var agentResponse = chatAgent.call(agentRequest);
		System.out.println(agentResponse);

		RelevancyEvaluator relevancyEvaluator = new RelevancyEvaluator(this.chatClient);
		EvaluationRequest evaluationRequest = new EvaluationRequest(
				agentResponse.getPromptContext().getOriginalPrompt(), agentResponse.getPromptContext().getDataList(),
				agentResponse.getChatResponse());

		EvaluationResponse evaluationResponse = relevancyEvaluator.evaluate(evaluationRequest);
		System.out.println(evaluationResponse);

	}

	void loadData() {
		JsonReader jsonReader = new JsonReader(bikesResource, "name", "price", "shortDescription", "description");
		var textSplitter = new TokenTextSplitter();
		vectorStore.accept(textSplitter.apply(jsonReader.get()));
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

		@Bean
		public EmbeddingClient embeddingClient(OpenAiApi openAiApi) {
			return new OpenAiEmbeddingClient(openAiApi);
		}

		@Bean
		public VectorStore vectorStore(EmbeddingClient embeddingClient) {
			return new SimpleVectorStore(embeddingClient);
		}

	}

}
