package org.springframework.ai.chat;

import org.springframework.ai.chat.messages.Media;
import org.springframework.util.MimeType;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class FluentChatClientLambda {

	private final ChatClient chatClient;

	public FluentChatClientLambda(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	public void chat(Consumer<FluentChatClientLambda.ChatSpec> chatSpecConsumer) {

	}

	public interface ChatSpec {

		ChatUserSpec user(Consumer<FluentChatClientLambda.ChatUserSpec> chatUserSpecConsumer);

		<T> T execute(T... varargsOfT);

	}

	public interface ChatUserSpec {

		ChatParamSpec text(String text, Consumer<ChatParamSpec> chatParamSpecConsumer);

		ChatMediaParamSpec media(Consumer<FluentChatClientLambda.ChatMediaParamSpec> chatMediaParamSpecConsumer);

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
