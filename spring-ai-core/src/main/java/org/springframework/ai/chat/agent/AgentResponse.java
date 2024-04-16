package org.springframework.ai.chat.agent;

import org.springframework.ai.chat.ChatResponse;

import java.util.Objects;

public class AgentResponse {

	private AgentRequest agentRequest;

	private PromptContext promptContext;

	private ChatResponse chatResponse;

	public AgentResponse(AgentRequest agentRequest, PromptContext promptContext, ChatResponse chatResponse) {
		this.agentRequest = agentRequest;
		this.promptContext = promptContext;
		this.chatResponse = chatResponse;
	}

	public AgentRequest getAgentRequest() {
		return agentRequest;
	}

	public PromptContext getPromptContext() {
		return promptContext;
	}

	public ChatResponse getChatResponse() {
		return chatResponse;
	}

	@Override
	public String toString() {
		return "AgentResponse{" + "agentRequest=" + agentRequest + ", promptContext=" + promptContext
				+ ", chatResponse=" + chatResponse + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof AgentResponse that))
			return false;
		return Objects.equals(agentRequest, that.agentRequest) && Objects.equals(promptContext, that.promptContext)
				&& Objects.equals(chatResponse, that.chatResponse);
	}

	@Override
	public int hashCode() {
		return Objects.hash(agentRequest, promptContext, chatResponse);
	}

}
