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
public class FluentChatClientLambda {

	private final ChatClient chatClient;

	public FluentChatClientLambda(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	public static class AbstractSystemMessageBuilder {

		private final Map<String, Object> paramsMap = new HashMap<>();

		AbstractSystemMessageBuilder params(Map<String, Object> paramsMap) {
			this.paramsMap.putAll(paramsMap);
			return this;
		}

	}

	static class AbstractUserMessageBuilder {

		private final List<Media> media = new ArrayList<>();

		private final Map<String, Object> paramsMap = new HashMap<>();

		AbstractUserMessageBuilder params(Map<String, Object> paramsMap) {
			this.paramsMap.putAll(paramsMap);
			return this;
		}

		AbstractUserMessageBuilder media(Media... media) {
			this.media.addAll(Arrays.asList(media));
			return this;
		}

	}

	static class TextUserMessageBuilder extends AbstractUserMessageBuilder {

		private final String text;

		TextUserMessageBuilder(String text) {
			this.text = text;
		}

	}

	static class ResourceUserMessageBuilder extends AbstractUserMessageBuilder {

		private final Resource resource;

		ResourceUserMessageBuilder(Resource resource) {
			this.resource = resource;
		}

	}

	static class ResourceSystemMessageBuilder extends AbstractSystemMessageBuilder {

		private final Resource resource;

		ResourceSystemMessageBuilder(Resource resource) {
			this.resource = resource;
		}

	}

	static class TextSystemMessageBuilder extends AbstractSystemMessageBuilder {

		private final String text;

		TextSystemMessageBuilder(String text) {
			this.text = text;
		}

	}

	static class UserMessageBuilderSpec {

		TextUserMessageBuilder text(String text) {
			return new TextUserMessageBuilder(text);
		}

		ResourceUserMessageBuilder resource(Resource resource) {
			return new ResourceUserMessageBuilder(resource);
		}

	}

	static class SystemMessageBuilderSpec {

		TextSystemMessageBuilder text(String text) {
			return new TextSystemMessageBuilder(text);
		}

		ResourceSystemMessageBuilder resource(Resource resource) {
			return new ResourceSystemMessageBuilder(resource);
		}

	}

	static class FunctionBuilderSpec {

		FunctionBuilderSpec functions(String... functionNames) {
			return this;
		}

		FunctionBuilderSpec functions(FunctionCallback... functionCallbacks) {
			return this;
		}

	}

	static class ChatBuilderSpec {

		ChatBuilderSpec functions(Consumer<FunctionBuilderSpec> functions) {
			return this;
		}

		ChatBuilderSpec system(Consumer<SystemMessageBuilderSpec> system) {
			return this;
		}

		ChatBuilderSpec user(Consumer<UserMessageBuilderSpec> user) {
			return this;
		}

	}

	static class LiquidChatCallSpec {

		<T> T call(T... ts) {
			return null;
		}

		<T> T call(Class<T> tClass) {
			return null;
		}

		ChatResponse call() {
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

/*
	void demo(ApplicationContext applicationContext, ChatClient cc) throws Exception {

		record ActorsFilmsRecord2(String actor, List<String> movies) {
		}

		var liquidChatClient = new LiquidChatClient(cc);
		var actors = liquidChatClient
			.chat(s -> s.system(sys -> sys.text("""
						You're a non user hostile chatbot from cyberdyne systems.
						your primary objective is {primaryObjective}
					""").params(Map.of("primaryObjective", "No PHP")))
				.functions(fn -> fn.functions((FunctionCallback) null)
					.functions("createReservation", "cancelReservations"))
				.user(user -> user.text("tell me a joke about {topic}")
					.params(Map.of("topic", "PHP"))
					.media(new Media[0])))
			.call(ActorsFilmsRecord2.class);
	}
*/

	public interface ChatUserSpec {

		ChatParamSpec text(String text, Map<String, Object> params);

		ChatMediaParamSpec media(Consumer<ChatMediaParamSpec> chatMediaParamSpecConsumer);

	}

	public interface ChatMediaParamSpec {

		ChatMediaParamSpec param(List<Media> mediaList);

		ChatMediaParamSpec param(Media media);

		ChatMediaParamSpec param(MimeType mimeType, Object data);

	}

	public interface ChatParamSpec {

		ChatParamSpec param(String name, Object value);

		ChatParamSpec params(Map<String, ?> paramMap);

	}

}