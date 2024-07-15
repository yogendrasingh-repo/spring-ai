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
package org.springframework.ai.openai.metadata;

import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.metadata.RateLimit;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.util.Assert;

/**
 * {@link ChatResponseMetadata} implementation for {@literal OpenAI}.
 *
 * @author John Blum
 * @author Thomas Vitale
 * @see ChatResponseMetadata
 * @see RateLimit
 * @see Usage
 * @since 0.7.0
 */
public abstract class OpenAiChatResponseMetadataUtils {

	public static ChatResponseMetadata from(OpenAiApi.ChatCompletion result) {
		Assert.notNull(result, "OpenAI ChatCompletionResult must not be null");
		return ChatResponseMetadata.builder()
			.withId(result.id())
			.withUsage(OpenAiUsage.from(result.usage()))
			.withModel(result.model())
			.withKeyValue("created", result.created())
			.withKeyValue("system-fingerprint", result.systemFingerprint())
			.build();
	}

	public static ChatResponseMetadata from(OpenAiApi.ChatCompletion result, RateLimit rateLimit) {
		Assert.notNull(result, "OpenAI ChatCompletionResult must not be null");
		return ChatResponseMetadata.builder()
			.withId(result.id())
			.withUsage(OpenAiUsage.from(result.usage()))
			.withModel(result.model())
			.withRateLimit(rateLimit)
			.withKeyValue("created", result.created())
			.withKeyValue("system-fingerprint", result.systemFingerprint())
			.build();
	}

}
