package org.springframework.ai.chat.engine2;

import org.springframework.ai.chat.ChatResponse;

public class GenerateResponse {

	private ChatResponse chatResponse; // TODO introduce common 'node' return super-type

	public GenerateResponse(ChatResponse chatResponse) {
		this.chatResponse = chatResponse;
	}

	public ChatResponse getChatResponse() {
		return chatResponse;
	}

	@Override
	public String toString() {
		return "GenerateResponse{" + "chatResponse=" + chatResponse + '}';
	}

}
