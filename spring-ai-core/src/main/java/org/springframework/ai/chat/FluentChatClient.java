package org.springframework.ai.chat;

import org.springframework.ai.chat.messages.Media;
import org.springframework.util.MimeType;

import java.util.List;
import java.util.Map;

public class FluentChatClient {

	private final ChatClient chatClient;

	public FluentChatClient(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	public ChatSpec chat() {
		return new DefaultChatSpec(this.chatClient);
	}

	public interface ChatSpec {

		ChatUserSpec user();

		<T> T execute(T... varargsOfT);

	}

	public interface ChatUserSpec {

		ChatParamSpec text(String text);

		ChatMediaParamSpec media();

		ChatSpec and();

	}

	public interface ChatMediaParamSpec {

		ChatMediaParamSpec param(List<Media> mediaList);

		ChatMediaParamSpec param(Media media);

		ChatMediaParamSpec param(MimeType mimeType, Object data);

		ChatUserSpec and();

	}

	public interface ChatParamSpec {

		ChatParamSpec param(String name, Object value);

		ChatParamSpec params(Map<String, ?> paramMap);

		ChatUserSpec and();

	}

}
