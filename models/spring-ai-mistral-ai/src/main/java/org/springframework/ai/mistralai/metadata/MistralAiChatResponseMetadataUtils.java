package org.springframework.ai.mistralai.metadata;

import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.metadata.EmptyUsage;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.mistralai.api.MistralAiApi;
import org.springframework.util.Assert;

import java.util.HashMap;

/**
 * {@link ChatResponseMetadata} implementation for {@literal Mistral AI}.
 *
 * @author Thomas Vitale
 * @see ChatResponseMetadata
 * @see Usage
 * @since 1.0.0
 */
public abstract class MistralAiChatResponseMetadataUtils {

	public static ChatResponseMetadata from(MistralAiApi.ChatCompletion result) {
		Assert.notNull(result, "Mistral AI ChatCompletion must not be null");
		MistralAiUsage usage = MistralAiUsage.from(result.usage());
		return ChatResponseMetadata.builder()
			.withId(result.id())
			.withModel(result.model())
			.withUsage(usage)
			.withKeyValue("created", result.created())
			.build();
	}

}
