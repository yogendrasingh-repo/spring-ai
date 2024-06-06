package org.springframework.ai.chat.metadata;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class DefaultChatGenerationMetadata implements ChatGenerationMetadata {

	private final String finishReason;

	private final Object contentFilterMetadata;

	@JsonCreator
	public DefaultChatGenerationMetadata(@JsonProperty("finishReason") String finishReason,
			@JsonProperty("contentFilterMetadata") Object contentFilterMetadata) {
		this.finishReason = finishReason;
		this.contentFilterMetadata = contentFilterMetadata;
	}

	@Override
	@JsonProperty("contentFilterMetadata")
	public <T> T getContentFilterMetadata() {
		return (T) contentFilterMetadata;
	}

	@Override
	@JsonProperty("finishReason")
	public String getFinishReason() {
		return finishReason;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof DefaultChatGenerationMetadata))
			return false;
		DefaultChatGenerationMetadata that = (DefaultChatGenerationMetadata) o;
		return Objects.equals(finishReason, that.finishReason)
				&& Objects.equals(contentFilterMetadata, that.contentFilterMetadata);
	}

	@Override
	public int hashCode() {
		return Objects.hash(finishReason, contentFilterMetadata);
	}

	@Override
	public String toString() {
		return "DefaultChatGenerationMetadata{" + "finishReason='" + finishReason + '\'' + ", contentFilterMetadata="
				+ contentFilterMetadata + '}';
	}

}
