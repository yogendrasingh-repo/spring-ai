package org.springframework.ai.chat;

import org.springframework.ai.chat.connector.ChatConnector;
import org.springframework.ai.chat.messages.Media;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.model.function.FunctionCallbackWrapper;
import org.springframework.ai.model.function.FunctionCallingOptionsBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.MimeType;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
import java.util.function.Consumer;

// todo support plugging in a outputConverter at runtime
// todo use the new BeanOutputConverter that supports a ParameterizedTypeReference
// todo figure out stream and list methods
// todo rebase to use the latest code
// todo can the fluid DSL be designed in such a way that calling .user() or .system()
//  	returns an object that wont let u call .messages(), and vice versa?

/*
 * @author Mark Pollack
 * @author Christian Tzolov
 * @author Josh Long
 * @author Arjen Poutsma
 */
public interface ChatClient {

	static ChatClientBuilder builder(ChatConnector connector) {
		return new ChatClientBuilder(connector);
	}

	ChatResponse call(Prompt prompt);

	ChatClientRequest call();

	interface PromptSpec<T> {

		T text(String text);

		T text(Resource text, Charset charset);

		T text(Resource text);

		T params(Map<String, Object> p);

		T param(String k, String v);

	}

	abstract class AbstractPromptSpec<T extends AbstractPromptSpec<T>> implements PromptSpec<T> {

		private String text = "";

		private final Map<String, Object> params = new HashMap<>();

		@Override
		public T text(String text) {
			this.text = text;
			return self();
		}

		@Override
		public T text(Resource text, Charset charset) {
			try {
				this.text(text.getContentAsString(charset));
			}
			catch (IOException e) {
				throw new RuntimeException(e);
			}
			return self();
		}

		@Override
		public T text(Resource text) {
			this.text(text, Charset.defaultCharset());
			return self();
		}

		@Override
		public T param(String k, String v) {
			this.params.put(k, v);
			return self();
		}

		@Override
		public T params(Map<String, Object> p) {
			this.params.putAll(p);
			return self();
		}

		protected abstract T self();

		protected String text() {
			return this.text;
		}

		protected Map<String, Object> params() {
			return this.params;
		}

	}

	class UserSpec extends AbstractPromptSpec<UserSpec> implements PromptSpec<UserSpec> {

		private final List<Media> media = new ArrayList<>();

		public UserSpec media(Media... media) {
			this.media.addAll(Arrays.asList(media));
			return self();
		}

		public UserSpec media(MimeType mimeType, URL url) {
			this.media.add(new Media(mimeType, url));
			return self();
		}

		public UserSpec media(MimeType mimeType, Resource resource) {
			this.media.add(new Media(mimeType, resource));
			return self();
		}

		protected List<Media> media() {
			return this.media;
		}

		@Override
		protected UserSpec self() {
			return this;
		}

	}

	class SystemSpec extends AbstractPromptSpec<SystemSpec> implements PromptSpec<SystemSpec> {

		@Override
		protected SystemSpec self() {
			return this;
		}

	}

	class ChatClientRequest {

		private final ChatConnector connector;

		private String userText = "";

		private String systemText = "";

		private ChatOptions chatOptions;

		private final List<Media> media = new ArrayList<>();

		private final Set<String> functionNames = new HashSet<>();

		private final List<FunctionCallback> functionCallbacks = new ArrayList<>();

		private final Map<String, Object> userParams = new HashMap<>();

		private final List<Message> messages = new ArrayList<>();

		private final Map<String, Object> systemParams = new HashMap<>();

		public ChatClientRequest(ChatConnector connector, String userText, String systemText,
				List<String> functionNames, List<Media> media, ChatOptions chatOptions) {
			this.userText = userText;
			this.systemText = systemText;
			this.connector = connector;
			this.functionNames.addAll(functionNames);
			this.media.addAll(media);
			this.chatOptions = chatOptions;
		}

		public ChatClientRequest messages(Message... messages) {
			this.messages.addAll(List.of(messages));
			return this;
		}

		public <T extends ChatOptions> ChatClientRequest options(T options) {
			this.chatOptions = options;
			return this;
		}

		public <I, O> ChatClientRequest function(String name, String description,
				java.util.function.Function<I, O> function) {
			var fcw = FunctionCallbackWrapper.builder(function)
				.withDescription(description)
				.withName(name)
				.withResponseConverter(Object::toString)
				.build();
			this.functionCallbacks.add(fcw);
			return this;
		}

		public ChatClientRequest functions(String... functions) {
			this.functionNames.addAll(List.of(functions));
			return this;
		}

		public ChatClientRequest system(Consumer<SystemSpec> consumer) {
			var ss = new SystemSpec();
			consumer.accept(ss);
			this.systemText = ss.text();
			this.systemParams.putAll(ss.params());
			return this;
		}

		public ChatClientRequest user(Consumer<UserSpec> consumer) {
			var us = new UserSpec();
			consumer.accept(us);
			this.userText = us.text();
			this.userParams.putAll(us.params());
			this.media.addAll(us.media());
			return this;
		}

		public static class ChatResponseSpec {

			private final ChatClientRequest request;

			private final ChatConnector chatConnector;

			public ChatResponseSpec(ChatConnector chatConnector, ChatClientRequest request) {
				this.chatConnector = chatConnector;
				this.request = request;
			}

			public <T> T single(ParameterizedTypeReference<T> t) {
				// todo once rebased make sure to use the {BeanOutputConverter} that now
				// accepts a ParameterizedTypeReference<T>
				return doSingleWithBeanOutputConverter(
						new BeanOutputConverter<T>(null /* todo */));
			}

			private <T> T doSingleWithBeanOutputConverter(BeanOutputConverter<T> boc) {
				var processedUserText = this.request.userText + System.lineSeparator() + System.lineSeparator()
						+ boc.getFormat();
				var chatResponse = doGetChatResponse(processedUserText);
				var stringResponse = chatResponse.getResult().getOutput().getContent();
				return boc.convert(stringResponse);
			}

			public <T> T single(Class<T> clzz) {
				Assert.notNull(clzz, "the class must be non-null");
				var boc = new BeanOutputConverter<T>(clzz);
				return doSingleWithBeanOutputConverter(boc);
			}

			private ChatResponse doGetChatResponse(String processedUserText) {


				var messages = new ArrayList<Message>();
				var textsAreValid = (StringUtils.hasText(processedUserText) || StringUtils.hasText(this.request.systemText));
				var messagesAreValid = !this.request.messages.isEmpty();

				Assert.state(!(messagesAreValid && textsAreValid),
						"you must specify either " + Message.class.getName() +
								" instances or user/system texts, but not both");

				if (textsAreValid) {

					var userMessage = new UserMessage(new PromptTemplate(processedUserText, this.request.userParams).render(),
							this.request.media);

					var systemMessage = new SystemMessage(
							new PromptTemplate(this.request.systemText, this.request.systemParams).render());

					messages.add(systemMessage);
					messages.add(userMessage);

				} else {
					messages.addAll(this.request.messages);
				}
				if (this.request.chatOptions instanceof FunctionCallingOptionsBuilder.PortableFunctionCallingOptions functionCallingOptions) {
					if (!this.request.functionNames.isEmpty()) {
						functionCallingOptions.setFunctions(this.request.functionNames);
					}
					if (!this.request.functionCallbacks.isEmpty()) {
						functionCallingOptions.setFunctionCallbacks(this.request.functionCallbacks);
					}
				}
				var prompt = new Prompt(messages, this.request.chatOptions);
				return this.chatConnector.call(prompt);
			}

			public ChatResponse chatResponse() {
				return doGetChatResponse(this.request.userText);
			}

			public <T> Flux<T> stream(Class<T> t) {
				notSupported();
				return null;
			}

			public <T> Flux<T> stream(ParameterizedTypeReference<T> t) {
				notSupported();
				return Flux.empty();
			}

			public String content() {
				return doGetChatResponse(this.request.userText).getResult().getOutput().getContent();
			}

			public <T> Collection<T> list(Class<T> clzz) {
				// todo move to the new ParameterizedTypeReference ready BeanOutputConverter
				notSupported();
				return null;
			}

			public <T> Collection<T> list(ParameterizedTypeReference<Collection<T>> ptr) {
				notSupported();
				return List.of();
			}


			private static void notSupported() {
				throw new RuntimeException("this operation is not supported");
			}
		}

		public ChatResponseSpec chat() {
			return new ChatResponseSpec(this.connector, this);
		}

	}

	class ChatClientBuilder {

		private final ChatConnector connector;

		private final List<Media> defaultMedia = new ArrayList<>();

		private final List<String> defaultFunctions = new ArrayList<>();

		private String defaultSystem;

		private String defaultUser;

		ChatClientBuilder(ChatConnector connector) {
			Assert.notNull(connector, "the " + ChatConnector.class.getName() + " must be non-null!");
			this.connector = connector;
		}

		public ChatClient build() {
			return new DefaultChatClient(this.connector, this.defaultSystem, this.defaultUser, this.defaultFunctions,
					this.defaultMedia);
		}

		public ChatClientBuilder defaultSystem(String systemPrompt) {
			this.defaultSystem = systemPrompt;
			return this;
		}

		public ChatClientBuilder defaultFunctions(String... functionNames) {
			this.defaultFunctions.addAll(List.of(functionNames));
			return this;
		}

		public ChatClientBuilder defaultUser(String userPrompt) {
			this.defaultUser = userPrompt;
			return this;
		}

	}

	@Deprecated(since = "1.0.0 M1", forRemoval = true)
	default String call(String message) {
		Prompt prompt = new Prompt(new UserMessage(message));
		Generation generation = call(prompt).getResult();
		return (generation != null) ? generation.getOutput().getContent() : "";
	}

	@Deprecated(since = "1.0.0 M1", forRemoval = true)
	default String call(Message... messages) {
		Prompt prompt = new Prompt(Arrays.asList(messages));
		Generation generation = call(prompt).getResult();
		return (generation != null) ? generation.getOutput().getContent() : "";
	}

}
