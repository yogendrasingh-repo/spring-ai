package org.springframework.ai.chat;

import org.springframework.ai.chat.connector.ChatConnector;
import org.springframework.ai.chat.messages.Media;
import org.springframework.core.ParameterizedTypeReference;

import java.util.*;


/**
 * @author Mark Pollack
 * @author Christian Tsolov
 * @author Josh Long
 */
public class ChatClient {

	private final ChatConnector connector;

	private final String userPrompt, systemPrompt;

	private final List<String> functions;

	private final List<Media> media;

	public ChatClient(ChatConnector connector, String defaultSystemPrompt, String defaultUserPrompt,
			List<String> defaultFunctions, List<Media> defaultMedia) {
		this.connector = connector;
		this.userPrompt = defaultUserPrompt;
		this.systemPrompt = defaultSystemPrompt;
		this.functions = defaultFunctions;
		this.media = defaultMedia;

	}

	public ChatClientRequest build() {
		return new ChatClientRequest(this.userPrompt, this.systemPrompt, this.functions, this.media);
	}

	public ChatClientRequest userPrompt(String userPrompt, Map<String, String> params) {
		var ccr = new ChatClientRequest(userPrompt, this.systemPrompt, this.functions, this.media);
		ccr.userPromptParams(params);
		return ccr;
	}

	public ChatClientRequest userPrompt(String userPrompt) {
		return new ChatClientRequest(userPrompt, this.systemPrompt, this.functions, this.media);
	}

	public static class ChatClientRequest {

		private String userPrompt = "";

		private String systemPrompt = "";

		private final List<Media> media = new ArrayList<>();

		private final List<String> functions = new ArrayList<>();

		private final Map<String, String> userPromptParams = new HashMap<>();

		private final Map<String, String> systemPromptParams = new HashMap<>();

		List<Media> media() {
			return this.media;
		}

		String systemPrompt() {
			return this.systemPrompt;
		}

		String userPrompt() {
			return this.userPrompt;
		}

		List<String> functions() {
			return this.functions;
		}

		public ChatClientRequest(String userPrompt, String systemPrompt, List<String> functions, List<Media> media) {
			this.userPrompt = userPrompt;
			this.systemPrompt = systemPrompt;
			this.functions.addAll(functions);
			this.media.addAll(media);
		}

		public ChatClientRequest userPromptParam(String key, String value) {
			this.userPromptParams.put(key, value);
			return this;
		}

		public ChatClientRequest systemPromptParam(String key, String value) {
			this.systemPromptParams.put(key, value);
			return this;
		}

		public ChatClientRequest systemPromptParams(Map<String, String> systemPromptParams) {
			this.systemPromptParams.putAll(systemPromptParams);
			return this;
		}

		public ChatClientRequest userPromptParams(Map<String, String> userPromptParams) {
			this.userPromptParams.putAll(userPromptParams);
			return this;
		}

		public ChatClientRequest userPrompt(String userPrompt) {
			this.userPrompt = userPrompt;
			return this;
		}

		public ChatClientRequest systemPrompt(String systemPrompt) {
			this.systemPrompt = systemPrompt;
			return this;
		}

		public ChatClientRequest media(Media... media) {
			this.media.addAll(Arrays.asList(media));
			return this;
		}

		public ChatClientRequest functions(String... functions) {
			this.functions.addAll(Arrays.asList(functions));
			return this;
		}

		public <T> T chat(Class<T> clzz) {
			return null;
		}

		public <T> T chat(ParameterizedTypeReference<T> clzz) {
			return null;
		}

	}

	public static class ChatClientBuilder {

		private final ChatConnector connector;

		private final List<Media> defaultMedia = new ArrayList<>();

		private final List<String> defaultFunctions = new ArrayList<>();

		private String defaultSystemPrompt;

		private String defaultUserPrompt;

		ChatClientBuilder(ChatConnector connector) {
			this.connector = connector;
		}

		public ChatClient build() {
			return new ChatClient(this.connector, this.defaultSystemPrompt, this.defaultUserPrompt,
					this.defaultFunctions, this.defaultMedia);
		}

		public ChatClientBuilder defaultSystemPrompt(String systemPrompt) {
			return this;
		}

		public ChatClientBuilder defaultFunctions(String... functionNames) {
			return this;
		}

		public ChatClientBuilder defaultUserPrompt(String userPrompt) {
			return this;
		}

	}

	public static ChatClientBuilder builder(ChatConnector connector) {
		return new ChatClientBuilder(connector);
	}

}
