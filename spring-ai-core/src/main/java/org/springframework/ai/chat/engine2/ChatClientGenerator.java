package org.springframework.ai.chat.engine2;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;

public class ChatClientGenerator implements Generator {

	private final ChatClient chatClient;

	public ChatClientGenerator(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@Override
	public GenerateResponse generate(GenerateRequest generateRequest) {
		ChatResponse chatResponse = this.chatClient.call(generateRequest.augmentResponse.getPrompt());
		return new GenerateResponse(chatResponse);
	}

}
