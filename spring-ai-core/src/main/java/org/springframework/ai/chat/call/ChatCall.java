package org.springframework.ai.chat.call;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.parser.OutputParser;
import org.springframework.util.StringUtils;

import java.util.*;

public class ChatCall implements ChatCallOperations {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final ChatClient chatClient;

	private final Optional<String> systemString;

	private Map<String, Object> systemMap = new HashMap<>();

	private final Optional<String> userString;

	private Map<String, Object> userMap = new HashMap<>();

	private final Optional<ChatOptions> chatOptions;

	private final Optional<OutputParser> outputParser;

	public ChatCall(ChatClient chatClient, Optional<String> systemString, Map<String, Object> systemMap,
			Optional<String> userString, Map<String, Object> userMap, Optional<ChatOptions> chatOptions,
			Optional<OutputParser> outputParser) {
		Objects.requireNonNull(chatClient, "ChatClient cannot be null.");
		this.chatClient = chatClient;
		this.systemString = systemString;
		this.systemMap = systemMap != null ? Collections.unmodifiableMap(new HashMap<>(systemMap))
				: Collections.unmodifiableMap(new HashMap<>());
		this.userString = userString;
		this.userMap = userMap != null ? Collections.unmodifiableMap(new HashMap<>(userMap))
				: Collections.unmodifiableMap(new HashMap<>());
		this.chatOptions = chatOptions;
		this.outputParser = outputParser;
	}

	public static ChatCallBuilder builder(ChatClient chatClient) {
		return new ChatCallBuilder(chatClient);
	}

	@Override
	public String execute(Map<String, Object> userMap) {
		return execute(userMap, new HashMap<>());
	}

	@Override
	public String execute(Map<String, Object> runtimeUserMap, Map<String, Object> runtimeSystemMap) {
		return execute("", runtimeUserMap, "", runtimeSystemMap);
		// List<Message> messageList = new ArrayList<>();
		// if (this.systemString.isPresent()) {
		// SystemPromptTemplate systemPromptTemplate = new
		// SystemPromptTemplate(this.systemString.get());
		// Map systemMapToUse = new HashMap(this.systemMap);
		// systemMapToUse.putAll(runtimeSystemMap);
		// messageList.add(systemPromptTemplate.createMessage(systemMapToUse));
		// }
		// if (this.userString.isPresent()) {
		// PromptTemplate userPromptTemplate = new PromptTemplate(this.userString.get());
		// Map userMapToUse = new HashMap(this.userMap);
		// userMapToUse.putAll(runtimeUserMap);
		// messageList.add(userPromptTemplate.createMessage(userMapToUse));
		// } else {
		// logger.warn("No user message set.");
		// }
		// Prompt prompt;
		// if (this.chatOptions.isPresent()) {
		// prompt = new Prompt(messageList, this.chatOptions.get());
		// } else {
		// prompt = new Prompt(messageList);
		// }
		// logger.debug("Created Prompt: {}", prompt);
		// ChatResponse chatResponse = this.chatClient.call(prompt);
		// return chatResponse.getResult().getOutput().getContent();
	}

	@Override
	public String execute(String userText, Map<String, Object> userMap) {
		return execute(userText, userMap, "", Collections.emptyMap());
	}

	@Override
	public String execute(String userText, Map<String, Object> runtimeUserMap, String systemText,
			Map<String, Object> runtimeSystemMap) {
		List<Message> messageList = new ArrayList<>();
		doCreateUserMessage(userText, runtimeUserMap, messageList);
		doCreateSystemMessage(systemText, runtimeSystemMap, messageList);
		Prompt prompt = doCreatePrompt(messageList);
		ChatResponse chatResponse = doExecute(prompt);
		String response = doCreateStringResponse(chatResponse);
		return response;
	}

	protected void doCreateUserMessage(String userText, Map<String, Object> runtimeUserMap, List<Message> messageList) {
		PromptTemplate userPromptTemplate = null;

		if (StringUtils.hasText(userText)) {
			userPromptTemplate = new PromptTemplate(userText);
		}
		else if (this.userString.isPresent()) {
			userPromptTemplate = new PromptTemplate(this.userString.get());
		}
		if (userPromptTemplate != null) {
			Map userMapToUse = new HashMap(this.userMap);
			userMapToUse.putAll(runtimeUserMap); // Merge the maps
			messageList.add(userPromptTemplate.createMessage(userMapToUse));
		}
		else {
			logger.warn("No user message set.");
		}
	}

	protected void doCreateSystemMessage(String systemText, Map<String, Object> runtimeSystemMap,
			List<Message> messageList) {
		SystemPromptTemplate systemPromptTemplate = null;
		if (StringUtils.hasText(systemText)) {
			systemPromptTemplate = new SystemPromptTemplate(systemText);
		}
		else if (this.systemString.isPresent()) {
			systemPromptTemplate = new SystemPromptTemplate(this.systemString.get());
		}
		if (systemPromptTemplate != null) {
			Map systemMapToUse = new HashMap(this.systemMap);
			systemMapToUse.putAll(runtimeSystemMap);
			messageList.add(systemPromptTemplate.createMessage(systemMapToUse));
		}
		else {
			logger.trace("No system message set");
		}
	}

	protected Prompt doCreatePrompt(List<Message> messageList) {
		Prompt prompt;
		if (this.chatOptions.isPresent()) {
			prompt = new Prompt(messageList, this.chatOptions.get());
		}
		else {
			prompt = new Prompt(messageList);
		}
		logger.debug("Created Prompt: {}", prompt);
		return prompt;
	}

	protected ChatResponse doExecute(Prompt prompt) {
		ChatResponse chatResponse = this.chatClient.call(prompt);
		return chatResponse;
	}

	protected static String doCreateStringResponse(ChatResponse chatResponse) {
		String response = chatResponse.getResult().getOutput().getContent();
		return response;
	}

	public static class ChatCallBuilder {

		private final ChatClient chatClient;

		private Optional<String> systemString = Optional.empty();

		private Map<String, Object> systemMap = new HashMap<>();

		private Optional<String> userString = Optional.empty();

		private Map<String, Object> userMap = new HashMap<>();

		private Optional<ChatOptions> chatOptions = Optional.empty();

		private Optional<OutputParser> outputParser = Optional.empty();

		private ChatCallBuilder(ChatClient chatClient) {
			Objects.requireNonNull(chatClient, "ChatClient cannot be null.");
			this.chatClient = chatClient;
		}

		public ChatCallBuilder withSystemString(String systemString) {
			this.systemString = Optional.ofNullable(systemString);
			return this;
		}

		public ChatCallBuilder withSystemMap(Map<String, Object> systemMap) {
			this.systemMap = systemMap;
			return this;
		}

		public ChatCallBuilder withUserString(String userString) {
			this.userString = Optional.ofNullable(userString);
			return this;
		}

		public ChatCallBuilder withUserMap(Map<String, Object> userMap) {
			this.userMap = userMap;
			return this;
		}

		public ChatCallBuilder withChatOptions(ChatOptions chatOptions) {
			this.chatOptions = Optional.ofNullable(chatOptions);
			return this;
		}

		public ChatCallBuilder withOutputParser(OutputParser outputParser) {
			this.outputParser = Optional.ofNullable(outputParser);
			return this;
		}

		public ChatCall build() {
			return new ChatCall(chatClient, systemString, systemMap, userString, userMap, chatOptions, outputParser);
		}

	}

}