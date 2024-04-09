package org.springframework.ai.chat.engine2;

import org.springframework.ai.chat.history.ChatExchange;
import org.springframework.ai.chat.history.ChatHistory;
import org.springframework.ai.chat.messages.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatExchangeEngineListener implements EngineListener {

	private final ChatHistory chatHistory;

	public ChatExchangeEngineListener(ChatHistory chatHistory) {
		this.chatHistory = chatHistory;
	}

	@Override
	public void onComplete(EngineRequest2 engineRequest2, GenerateResponse generateResponse) {
		List<Message> historyMessages = new ArrayList<>(engineRequest2.getPrompt().getInstructions());
		historyMessages.add(generateResponse.getChatResponse().getResult().getOutput());
		ChatExchange chatExchange = new ChatExchange(engineRequest2.getConversationId(), historyMessages);
		this.chatHistory.add(chatExchange);
	}

}
