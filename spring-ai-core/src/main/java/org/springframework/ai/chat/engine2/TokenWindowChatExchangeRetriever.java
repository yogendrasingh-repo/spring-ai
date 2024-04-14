package org.springframework.ai.chat.engine2;

import org.springframework.ai.chat.history.ChatExchange;
import org.springframework.ai.chat.history.ChatHistory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.tokenizer.JTokkitTokenCountEstimator;
import org.springframework.ai.tokenizer.TokenCountEstimator;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class TokenWindowChatExchangeRetriever implements EngineRetriever {

	private final ChatHistory chatHistory;

	/**
	 * Token encoding used to estimate the token count.
	 */
	protected final TokenCountEstimator tokenCountEstimator;

	/**
	 * Maximum token size allowed in the chat history.
	 */
	private final int maxTokenSize;

	public TokenWindowChatExchangeRetriever(ChatHistory chatHistory, int maxTokenSize) {
		this(chatHistory, new JTokkitTokenCountEstimator(), maxTokenSize);
	}

	public TokenWindowChatExchangeRetriever(ChatHistory chatHistory, TokenCountEstimator tokenCountEstimator,
			int maxTokenSize) {
		this.chatHistory = chatHistory;
		this.tokenCountEstimator = tokenCountEstimator;
		this.maxTokenSize = maxTokenSize;
	}

	@Override
	public RetrievalResponse retrieve(RetrievalRequest retrievalRequest) {

		List<ChatExchange> nonSystemChatMessages = (this.chatHistory.get(retrievalRequest.getConversationId()) != null)
				? this.chatHistory.get(retrievalRequest.getConversationId())
					.stream()
					.map(g -> new ChatExchange(retrievalRequest.getConversationId(),
							g.getMessages().stream().filter(m -> m.getMessageType() != MessageType.SYSTEM).toList()))
					.toList()
				: List.of();

		var flatMessages = nonSystemChatMessages.stream().map(g -> g.getMessages()).flatMap(List::stream).toList();

		retrievalRequest.addTokenCount(
				this.tokenCountEstimator.estimate(retrievalRequest.getEngineRequest2().getPrompt().getInstructions()));
		int totalSize = this.tokenCountEstimator.estimate(flatMessages) - retrievalRequest.getTokenRunningTotal();

		if (totalSize <= this.maxTokenSize) {
			return new RetrievalResponse(List.of(), nonSystemChatMessages);
		}

		List<ChatExchange> newChatMessages = new ArrayList<>();

		for (ChatExchange chatMessage : nonSystemChatMessages) {
			List<Message> sessionMessages = chatMessage.getMessages();
			List<Message> newSessionMessages = this.purgeExcess(sessionMessages, totalSize);
			if (!CollectionUtils.isEmpty(newSessionMessages)) {
				newChatMessages.add(new ChatExchange(chatMessage.getSessionId(), newSessionMessages));
			}
		}
		return new RetrievalResponse(List.of(), newChatMessages);
	}

	protected List<Message> purgeExcess(List<Message> sessionMessages, int totalSize) {

		int index = 0;
		List<Message> newList = new ArrayList<>();

		while (index < sessionMessages.size() && totalSize > this.maxTokenSize) {
			Message oldMessage = sessionMessages.get(index++);
			int oldMessageTokenSize = this.tokenCountEstimator.estimate(oldMessage);
			totalSize = totalSize - oldMessageTokenSize;
		}

		if (index >= sessionMessages.size()) {
			return List.of();
		}

		// add the rest of the messages.
		newList.addAll(sessionMessages.subList(index, sessionMessages.size()));

		return newList;
	}

}
