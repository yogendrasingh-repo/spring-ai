/*
 * Copyright 2023 - 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.ai.anthropic.metadata;

import org.springframework.ai.anthropic.api.AnthropicApi;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.metadata.EmptyRateLimit;
import org.springframework.ai.chat.metadata.EmptyUsage;
import org.springframework.ai.chat.metadata.RateLimit;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.HashMap;

/**
 * {@link ChatResponseMetadata} implementation for {@literal AnthropicApi}.
 *
 * @author Christian Tzolov
 * @author Thomas Vitale
 * @see ChatResponseMetadata
 * @see RateLimit
 * @see Usage
 * @since 1.0.0
 */
public abstract class AnthropicChatResponseMetadataUtils {

	public static ChatResponseMetadata from(AnthropicApi.ChatCompletionResponse result) {
		Assert.notNull(result, "Anthropic ChatCompletionResult must not be null");
		AnthropicUsage usage = AnthropicUsage.from(result.usage());
		return ChatResponseMetadata.builder()
			.withId(result.id())
			.withModel(result.model())
			.withUsage(usage)
			.withKeyValue("stop-reason", result.stopReason())
			.withKeyValue("stop-sequence", result.stopSequence())
			.withKeyValue("type", result.type())
			.build();
	}

}
