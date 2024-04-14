package org.springframework.ai.chat.agent;

import org.springframework.ai.chat.ChatResponse;

import java.util.Objects;

public class AgentResponse {

	private AgentRequest agentRequest;

	private AgentContext agentContext;

	private ChatResponse chatResponse;

	public AgentResponse(AgentRequest agentRequest, AgentContext agentContext, ChatResponse chatResponse) {
		this.agentRequest = agentRequest;
		this.agentContext = agentContext;
		this.chatResponse = chatResponse;
	}

	public AgentRequest getAgentRequest() {
		return agentRequest;
	}

	public AgentContext getAgentContext() {
		return agentContext;
	}

	public ChatResponse getChatResponse() {
		return chatResponse;
	}

	@Override
	public String toString() {
		return "AgentResponse{" + "agentRequest=" + agentRequest + ", agentContext=" + agentContext + ", chatResponse="
				+ chatResponse + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof AgentResponse that))
			return false;
		return Objects.equals(agentRequest, that.agentRequest) && Objects.equals(agentContext, that.agentContext)
				&& Objects.equals(chatResponse, that.chatResponse);
	}

	@Override
	public int hashCode() {
		return Objects.hash(agentRequest, agentContext, chatResponse);
	}

}
