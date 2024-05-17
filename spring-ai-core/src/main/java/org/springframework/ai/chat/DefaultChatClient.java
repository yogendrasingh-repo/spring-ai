package org.springframework.ai.chat;

import org.springframework.ai.chat.connector.ChatConnector;
import org.springframework.ai.chat.messages.Media;
import org.springframework.ai.chat.prompt.Prompt;

import java.util.*;
import java.util.function.Consumer;


/**
 * todo follow WebClient -> DefaultWebClient
 * todo make sure ChatConnector also supports call(Prompt) and then mark as deprecated
 *
 * @author Mark Pollack
 * @author Christian Tzolov
 * @author Josh Long
 */

public class DefaultChatClient implements ChatClient {

	private final ChatConnector connector;

	private final String userPrompt, systemPrompt;

	private final List<String> functions;

	private final List<Media> media;

	public DefaultChatClient(ChatConnector connector, String defaultSystemPrompt, String defaultUserPrompt,
							 List<String> defaultFunctions, List<Media> defaultMedia) {
		this.connector = connector;
		this.userPrompt = defaultUserPrompt;
		this.systemPrompt = defaultSystemPrompt;
		this.functions = defaultFunctions;
		this.media = defaultMedia;

	}

	@Override
	public ChatClientRequest build() {
		return new ChatClientRequest(this.userPrompt, this.systemPrompt, this.functions, this.media);
	}

	@Override
	public ChatResponse call(Prompt prompt) {
		return null;
	}


	@Override
	public ChatClientRequest user(Consumer<UserSpec> consumer) {
		return null;
	}

}