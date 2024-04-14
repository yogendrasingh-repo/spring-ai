package org.springframework.ai.chat.agent;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.agent.retriever.Retriever;
import org.springframework.ai.chat.agent.transformer.PromptTransformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatAgent implements Agent {

	private ChatClient chatClient;

	private List<Retriever> retrievers;

	private List<PromptTransformer> transformers = new ArrayList<>();

	private ChatAgent(ChatClient chatClient, List<Retriever> retrievers, List<PromptTransformer> promptTransformers) {
		this.chatClient = chatClient;
		this.retrievers = retrievers;
		this.transformers = promptTransformers;
	}

	@Override
	public AgentResponse call(AgentRequest agentRequest) {
		var agentContext = new AgentContext(agentRequest.getPrompt());

		for (Retriever retriever : retrievers) {
			agentContext = retriever.retrieve(agentContext);
		}

		for (PromptTransformer transformer : transformers) {
			agentContext = transformer.transform(agentContext);
		}

		var chatResponse = this.chatClient.call(agentContext.getPrompt());

		return new AgentResponse(agentRequest, agentContext, chatResponse);

	}

	public static class Builder {

		private List<Retriever> retrievers = new ArrayList<>();

		private List<PromptTransformer> transformers = new ArrayList<>();

		private ChatClient chatClient;

		public Builder withChatClient(ChatClient chatClient) {
			this.chatClient = chatClient;
			return this;
		}

		public Builder withDataRetrievers(Retriever... retrievers) {
			this.retrievers.addAll(Arrays.asList(retrievers));
			return this;
		}

		public Builder withDataRetriever(Retriever retriever) {
			this.retrievers.add(retriever);
			return this;
		}

		public Builder withPromptTransformers(PromptTransformer... transformers) {
			this.transformers.addAll(Arrays.asList(transformers));
			return this;
		}

		public Builder withPromptTransformer(PromptTransformer promptTransformer) {
			this.transformers.add(promptTransformer);
			return this;
		}

		public ChatAgent build() {
			return new ChatAgent(this.chatClient, this.retrievers, this.transformers);
		}

	}

}