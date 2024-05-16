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

public class DefaultChatSpec implements FluentChatClient.ChatSpec {

	private String userText;

	private ChatClient chatClient;

	private Map<String, Object> textParameters = new HashMap<>();

	public DefaultChatSpec(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@Override
	public FluentChatClient.ChatUserSpec user() {
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

	private class DefaultChatUserSpec implements FluentChatClient.ChatUserSpec {

		private DefaultChatSpec chatSpec;

		private String text;

		private Map<String, Object> textParameters = new HashMap<>();

		public DefaultChatUserSpec(DefaultChatSpec chatSpec) {
			this.chatSpec = chatSpec;
		}

		@Override
		public FluentChatClient.ChatParamSpec text(String text) {
			this.text = text;
			return new DefaultChatParamSpec(this);
		}

		@Override
		public FluentChatClient.ChatMediaParamSpec media() {
			return new DefaultChatMediaParamSpec(this);
		}

		public void addTextParams(Map<String, Object> textParameters) {
			this.textParameters.putAll(textParameters);
		}

		public FluentChatClient.ChatSpec and() {
			this.chatSpec.addUser(this.text, this.textParameters);
			return this.chatSpec;
		}

	}

	private class DefaultChatMediaParamSpec implements FluentChatClient.ChatMediaParamSpec {

		private final DefaultChatUserSpec chatUserSpec;

		public DefaultChatMediaParamSpec(DefaultChatUserSpec chatUserSpec) {
			this.chatUserSpec = chatUserSpec;
		}

		@Override
		public FluentChatClient.ChatMediaParamSpec param(List<Media> mediaList) {
			return null;
		}

		@Override
		public FluentChatClient.ChatMediaParamSpec param(Media media) {
			return null;
		}

		@Override
		public FluentChatClient.ChatMediaParamSpec param(MimeType mimeType, Object data) {
			return null;
		}

		@Override
		public FluentChatClient.ChatUserSpec and() {
			return null;
		}

	}

	private class DefaultChatParamSpec implements FluentChatClient.ChatParamSpec {

		private final DefaultChatUserSpec chatUserSpec;

		private Map<String, Object> parameters = new HashMap<>();

		public DefaultChatParamSpec(DefaultChatUserSpec chatUserSpec) {
			this.chatUserSpec = chatUserSpec;
		}

		@Override
		public FluentChatClient.ChatParamSpec param(String name, Object value) {
			this.parameters.put(name, value);
			return this;
		}

		@Override
		public FluentChatClient.ChatParamSpec params(Map<String, ?> paramMap) {
			this.parameters.putAll(paramMap);
			return this;
		}

		@Override
		public FluentChatClient.ChatUserSpec and() {
			chatUserSpec.addTextParams(this.parameters);
			return this.chatUserSpec;
		}

	}

}
