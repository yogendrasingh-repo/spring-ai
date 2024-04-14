package org.springframework.ai.chat.agent;

import org.springframework.ai.chat.prompt.Prompt;

import java.util.Objects;
import java.util.UUID;

public class AgentRequest {

	private final String conversationId;

	private final Prompt prompt;

	public AgentRequest(String message) {
		this(new Prompt(message));
	}

	public AgentRequest(Prompt prompt) {
		this(prompt, UUID.randomUUID().toString());
	}

	public AgentRequest(Prompt prompt, String conversationId) {
		this.prompt = prompt;
		this.conversationId = conversationId;
	}

	public String getConversationId() {
		return conversationId;
	}

	public Prompt getPrompt() {
		return prompt;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof AgentRequest that))
			return false;
		return Objects.equals(conversationId, that.conversationId) && Objects.equals(prompt, that.prompt);
	}

	@Override
	public int hashCode() {
		return Objects.hash(conversationId, prompt);
	}

	@Override
	public String toString() {
		return "AgentRequest{" + "conversationId='" + conversationId + '\'' + ", prompt=" + prompt + '}';
	}

}
