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
package org.springframework.ai.ollama.metadata;

import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.util.Assert;

import java.util.HashMap;

/**
 * {@link ChatResponseMetadata} implementation for {@literal Ollama}
 *
 * @see ChatResponseMetadata
 * @author Fu Cheng
 */
public abstract class OllamaChatResponseMetadataUtils {

	public static ChatResponseMetadata from(OllamaApi.ChatResponse response) {
		Assert.notNull(response, "OllamaApi.ChatResponse must not be null");
		return ChatResponseMetadata.builder()
			.withUsage(OllamaUsage.from(response))
			.withModel(response.model())
			.withKeyValue("created-at", response.createdAt())
			.withKeyValue("eval-duration", response.evalDuration())
			.withKeyValue("eval-count", response.evalCount())
			.withKeyValue("load-duration", response.loadDuration())
			.withKeyValue("eval-duration", response.promptEvalDuration())
			.withKeyValue("eval-count", response.promptEvalCount())
			.withKeyValue("total-duration", response.totalDuration())
			.withKeyValue("done", response.done())
			.build();

	}

}
