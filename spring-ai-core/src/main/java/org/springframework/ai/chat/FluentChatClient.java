package org.springframework.ai.chat;

import org.springframework.ai.chat.messages.Media;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.core.io.Resource;
import org.springframework.util.MimeType;

import java.util.*;
import java.util.function.Consumer;

/**
 * @author Josh Long
 */
public class FluentChatClient {

	private final ChatClient chatClient;

	public FluentChatClient(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	public static class AbstractSystemMessageBuilder {

		private final Map<String, Object> paramsMap = new HashMap<>();

		public AbstractSystemMessageBuilder params(Map<String, Object> paramsMap) {
			this.paramsMap.putAll(paramsMap);
			return this;
		}

	}

	public static class AbstractUserMessageBuilder {

		private final List<Media> media = new ArrayList<>();

		private final Map<String, Object> paramsMap = new HashMap<>();

		public AbstractUserMessageBuilder params(Map<String, Object> paramsMap) {
			this.paramsMap.putAll(paramsMap);
			return this;
		}

		public AbstractUserMessageBuilder media(Media... media) {
			this.media.addAll(Arrays.asList(media));
			return this;
		}

	}

	public static class TextUserMessageBuilder extends AbstractUserMessageBuilder {

		private final String text;

		public TextUserMessageBuilder(String text) {
			this.text = text;
		}

	}

	public static class ResourceUserMessageBuilder extends AbstractUserMessageBuilder {

		private final Resource resource;

		public ResourceUserMessageBuilder(Resource resource) {
			this.resource = resource;
		}

	}

	public static class ResourceSystemMessageBuilder extends AbstractSystemMessageBuilder {

		private final Resource resource;

		public ResourceSystemMessageBuilder(Resource resource) {
			this.resource = resource;
		}

	}

	public static class TextSystemMessageBuilder extends AbstractSystemMessageBuilder {

		private final String text;

		public TextSystemMessageBuilder(String text) {
			this.text = text;
		}

	}

	public static class UserMessageBuilderSpec {

		public TextUserMessageBuilder text(String text) {
			return new TextUserMessageBuilder(text);
		}

		public ResourceUserMessageBuilder resource(Resource resource) {
			return new ResourceUserMessageBuilder(resource);
		}

	}

	public static class SystemMessageBuilderSpec {

		public TextSystemMessageBuilder text(String text) {
			return new TextSystemMessageBuilder(text);
		}

		public ResourceSystemMessageBuilder resource(Resource resource) {
			return new ResourceSystemMessageBuilder(resource);
		}

	}

	public static class FunctionBuilderSpec {

		public FunctionBuilderSpec functions(String... functionNames) {
			return this;
		}

		public FunctionBuilderSpec functions(FunctionCallback... functionCallbacks) {
			return this;
		}

	}

	public static class ChatBuilderSpec {

		public ChatBuilderSpec functions(Consumer<FunctionBuilderSpec> functions) {
			return this;
		}

		public ChatBuilderSpec system(Consumer<SystemMessageBuilderSpec> system) {
			return this;
		}

		public ChatBuilderSpec user(Consumer<UserMessageBuilderSpec> user) {
			return this;
		}

	}

	public static class LiquidChatCallSpec {

		public <T> T call(T... ts) {
			return null;
		}

		public <T> T call(Class<T> tClass) {
			return null;
		}

		public ChatResponse call() {
			return null;
		}

	}

	public static class LiquidChatClient {

		private final ChatClient chatClient;

		public LiquidChatClient(ChatClient chatClient) {
			this.chatClient = chatClient;
		}

		// put this in ChatClient
		public LiquidChatCallSpec chat(Consumer<ChatBuilderSpec> chatSpec) {
			return null;
		}

	}

}