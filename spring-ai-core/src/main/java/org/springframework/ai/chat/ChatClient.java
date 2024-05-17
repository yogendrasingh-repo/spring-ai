package org.springframework.ai.chat;

import org.springframework.ai.chat.connector.ChatConnector;
import org.springframework.ai.chat.messages.Media;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.util.MimeType;
import reactor.core.publisher.Flux;

import java.net.URL;
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

	public ChatResponse call(Prompt prompt) {
		return null;
	}

	public static class UserSpec {


		public UserSpec media(List<Media> media) {
			return this;
		}

		public UserSpec media(URL url, MimeType mimeType) {
			return this;
		}

		public UserSpec media(Resource resource, MimeType type) {
			return this;
		}

		public UserSpec media(Media... m) {
			return this;
		}

		public UserSpec params(Map<String, Object> p) {
			return this;
		}

		public UserSpec param(String k, String v) {
			return this;
		}
	}


	public ChatClientRequest user(Consumer<UserSpec> consumer) {
		return null;
	}

	public static class ChatClientRequest {

		private String userPrompt = "";

		private String systemPrompt = "";

		private final List<Media> media = new ArrayList<>();

		private final List<String> functions = new ArrayList<>();

		private final Map<String, String> userPromptParams = new HashMap<>();

		private final Map<String, String> systemPromptParams = new HashMap<>();

		List<Media> userMedia() {
			return this.media;
		}

		String systemText() {
			return this.systemPrompt;
		}

		String userText() {
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

		public ChatClientRequest messages(Message... messages) {
			return null;
		}
//
//		public ChatClientRequest userParam(String key, String value) {
//			this.userPromptParams.put(key, value);
//			return this;
//		}
//
//		public ChatClientRequest systemParam(String key, String value) {
//			this.systemPromptParams.put(key, value);
//			return this;
//		}

		public <T extends ChatOptions> ChatClientRequest options(T options) {
			return this;
		}
//
//		public ChatClientRequest systemParams(Map<String, String> systemPromptParams) {
//			this.systemPromptParams.putAll(systemPromptParams);
//			return this;
//		}
//
//		public ChatClientRequest userParams(Map<String, String> userPromptParams) {
//			this.userPromptParams.putAll(userPromptParams);
//			return this;
//		}
//
//		public ChatClientRequest userText(Resource resource) {
//			return userText(resource, Charset.defaultCharset());
//		}

//		public ChatClientRequest userText(Resource resource, Charset charset) {
//			try {
//				this.userText(resource.getContentAsString(charset));
//			} catch (IOException e) {
//				throw new RuntimeException(e);
//			}
//			return this;
//		}
//
//
//		public ChatClientRequest userText(String userPrompt) {
//			this.userPrompt = userPrompt;
//			return this;
//		}
//
//		public ChatClientRequest systemText(Resource systemPrompt) {
//			return systemText(systemPrompt, Charset.defaultCharset());
//		}
//
//		public ChatClientRequest systemText(Resource systemPrompt, Charset charset) {
//			try {
//				this.systemText(systemPrompt.getContentAsString(charset));
//			} catch (IOException e) {
//				throw new RuntimeException(e);
//			}
//			return this;
//		}
//
//		public ChatClientRequest systemText(String systemPrompt) {
//			this.systemPrompt = systemPrompt;
//			return this;
//		}
//
//		public ChatClientRequest userMedia(Media... media) {
//			this.media.addAll(Arrays.asList(media));
//			return this;
//		}

		public ChatClientRequest functions(String... functions) {
			this.functions.addAll(Arrays.asList(functions));
			return this;
		}


		public static class ChatResponseSpec {

			public <T> T single(ParameterizedTypeReference<T> t) {
				return null;
			}


			public <T> T single(Class<T> clzz) {
				return null;
			}

			public ChatResponse chatResponse() {
				return null;
			}

			public <T> Flux<T> stream(Class<T> t) {
				return null;
			}

			public <T> Flux<T> stream(ParameterizedTypeReference<T> t) {
				return Flux.empty();
			}

			public <T> Collection<T> list(Class<T> clzz) {
				return null;
			}

			public <T> Collection<T> list(ParameterizedTypeReference<Collection<T>> ptr) {
				return List.of();
			}

		}

		public ChatResponseSpec chat() {
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

		public ChatClientBuilder defaultSystem(String systemPrompt) {
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
