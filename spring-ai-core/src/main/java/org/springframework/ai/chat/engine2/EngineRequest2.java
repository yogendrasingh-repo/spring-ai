package org.springframework.ai.chat.engine2;

import org.springframework.ai.chat.prompt.Prompt;

public class EngineRequest2 {

	private String conversationId; // discuss, this seems to make sense to have here as
									// each request is for a 'conversation'

	private final Prompt prompt;

	public EngineRequest2(String conversationId, Prompt prompt) {
		this.conversationId = conversationId;
		this.prompt = prompt;
	}

	public Prompt getPrompt() {
		return prompt;
	}

	public String getConversationId() {
		return conversationId;
	}

	@Override
	public String toString() {
		return "EngineRequest2{" + "conversationId='" + conversationId + '\'' + ", prompt=" + prompt + '}';
	}

}
