package org.springframework.ai.chat;

import org.springframework.ai.chat.messages.Media;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.util.Assert;
import org.springframework.util.MimeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class DefaultChatSpecLambda implements FluentChatClientLambda.ChatSpec {

	private String userText;

	private ChatClient chatClient;

	private Map<String, Object> textParameters = new HashMap<>();

	public DefaultChatSpecLambda(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@Override
	public FluentChatClientLambda.ChatUserSpec user() {
		return new DefaultChatUserSpec(this);
	}

	public void addUser(String userText, Map<String, Object> textParameters) {
		this.userText = userText;
		this.textParameters.putAll(textParameters);
	}

	public <T> T execute(T... varargsOfT) {

		Assert.state(varargsOfT.length == 0, "you should not provide any values for T!");
		Assert.state(varargsOfT.getClass().isArray(), "this needs to be an array parameter");
		Class<?> componentClass = varargsOfT.getClass().getComponentType();

		List<Message> messageList = new ArrayList<>();
		String userTextToUse = userText + System.lineSeparator() + "{format}";
		var converter = new BeanOutputConverter(componentClass);

		this.textParameters.put("format", converter.getFormat());
		doCreateUserMessage(messageList, userTextToUse, this.textParameters);

		Prompt prompt = new Prompt(messageList);
		ChatResponse chatResponse = this.chatClient.call(prompt);
		String stringResponse = chatResponse.getResult().getOutput().getContent();

		T convertedResponse = (T) converter.convert(stringResponse);
		return convertedResponse;
	}

	private void doCreateUserMessage(List<Message> messageList, String userText, Map<String, Object> textParameters) {
		PromptTemplate userPromptTemplate = new PromptTemplate(userText);
		messageList.add(userPromptTemplate.createMessage(textParameters));

	}

	private class DefaultChatUserSpec implements FluentChatClientLambda.ChatUserSpec {

		private DefaultChatSpecLambda chatSpec;

		private String text;

		private Map<String, Object> textParameters = new HashMap<>();

		public DefaultChatUserSpec(DefaultChatSpecLambda chatSpec) {
			this.chatSpec = chatSpec;
		}

		// @Override
		// public FluentChatClientLambda.ChatParamSpec text(String text) {
		// this.text = text;
		// return new DefaultChatParamSpec(this);
		// }
		//
		// @Override
		// public FluentChatClientLambda.ChatMediaParamSpec media() {
		// return new DefaultChatMediaParamSpec(this);
		// }

		public void addTextParams(Map<String, Object> textParameters) {
			this.textParameters.putAll(textParameters);
		}

		@Override
		public FluentChatClientLambda.ChatParamSpec text(String text,
				Consumer<FluentChatClientLambda.ChatParamSpec> chatParamSpecConsumer) {
			this.text = text;
			return chatParamSpecConsumer;
		}

		@Override
		public FluentChatClientLambda.ChatMediaParamSpec media(
				Consumer<FluentChatClientLambda.ChatMediaParamSpec> chatMediaParamSpecConsumer) {
			return null;
		}

	}

	private class DefaultChatMediaParamSpec implements FluentChatClientLambda.ChatMediaParamSpec {

		private final DefaultChatUserSpec chatUserSpec;

		public DefaultChatMediaParamSpec(DefaultChatUserSpec chatUserSpec) {
			this.chatUserSpec = chatUserSpec;
		}

		@Override
		public FluentChatClientLambda.ChatMediaParamSpec param(List<Media> mediaList) {
			return null;
		}

		@Override
		public FluentChatClientLambda.ChatMediaParamSpec param(Media media) {
			return null;
		}

		@Override
		public FluentChatClientLambda.ChatMediaParamSpec param(MimeType mimeType, Object data) {
			return null;
		}

	}

	private class DefaultChatParamSpec implements FluentChatClientLambda.ChatParamSpec {

		private final DefaultChatUserSpec chatUserSpec;

		private Map<String, Object> parameters = new HashMap<>();

		public DefaultChatParamSpec(DefaultChatUserSpec chatUserSpec) {
			this.chatUserSpec = chatUserSpec;
		}

		@Override
		public FluentChatClientLambda.ChatParamSpec param(String name, Object value) {
			this.parameters.put(name, value);
			return this;
		}

		@Override
		public FluentChatClientLambda.ChatParamSpec params(Map<String, ?> paramMap) {
			this.parameters.putAll(paramMap);
			return this;
		}

	}

}
