package org.springframework.ai.chat;

import org.springframework.ai.chat.connector.ChatConnector;
import org.springframework.ai.chat.messages.Media;
import org.springframework.ai.chat.prompt.Prompt;

import java.util.List;

/**
 * @author Mark Pollack
 * @author Christian Tzolov
 * @author Josh Long
 * @author Arjen Poutsma
 */
class DefaultChatClient implements ChatClient {

	private final ChatConnector connector;

	private final String userText, systemText;

	private final List<String> functionNames;

	private final List<Media> media;

	public DefaultChatClient(ChatConnector connector, String defaultSystemPrompt, String defaultUserPrompt,
			List<String> defaultFunctions, List<Media> defaultMedia) {
		this.connector = connector;
		this.userText = defaultUserPrompt;
		this.systemText = defaultSystemPrompt;
		this.functionNames = defaultFunctions;
		this.media = defaultMedia;

	}

	@Override
	public ChatClientRequest call() {
		return new ChatClientRequest(this.connector, this.userText, this.systemText, this.functionNames, this.media,
				null);
	}

	/**
	 * use the new fluid DSL starting in {@link #call()}
	 * @param prompt the {@link Prompt prompt} object
	 * @return a {@link ChatResponse chat response}
	 */
	@Deprecated(forRemoval = true, since = "1.0.0 M1")
	@Override
	public ChatResponse call(Prompt prompt) {
		return this.connector.call(prompt);
	}

}
