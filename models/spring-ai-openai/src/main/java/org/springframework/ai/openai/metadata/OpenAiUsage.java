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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * {@link Usage} implementation for {@literal OpenAI}.
 *
 * @author John Blum
 * @since 0.7.0
 * @see <a href=
 * "https://platform.openai.com/docs/api-reference/completions/object">Completion
 * Object</a>
 */
public class OpenAiUsage implements Usage {

	public static OpenAiUsage from(OpenAiApi.Usage usage) {
		return new OpenAiUsage(usage);
	}

	private Long promptTokens;

	private Long generationTokens;

	private Long totalTokens;

	public OpenAiUsage(OpenAiApi.Usage usage) {
		Assert.notNull(usage, "OpenAiApi.Usage must not be null");
		this.promptTokens = usage.promptTokens().longValue();
		this.generationTokens = usage.completionTokens().longValue();
		this.totalTokens = usage.totalTokens().longValue();
	}

	@JsonCreator
	public OpenAiUsage(@JsonProperty("promptTokens") Long promptTokens,
			@JsonProperty("generationTokens") Long generationTokens, @JsonProperty("totalTokens") Long totalTokens) {
		this.promptTokens = promptTokens;
		this.generationTokens = generationTokens;
		this.totalTokens = totalTokens;
	}

	@Override
	public Long getPromptTokens() {
		return this.promptTokens;
	}

	@Override
	public Long getGenerationTokens() {
		return this.generationTokens;
	}

	@Override
	public Long getTotalTokens() {
		return this.totalTokens;
	}

	@Override
	public String toString() {
		return "OpenAiUsage{" + "promptTokens=" + promptTokens + ", generationTokens=" + generationTokens
				+ ", totalTokens=" + totalTokens + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof OpenAiUsage that))
			return false;
		return Objects.equals(promptTokens, that.promptTokens)
				&& Objects.equals(generationTokens, that.generationTokens)
				&& Objects.equals(totalTokens, that.totalTokens);
	}

	@Override
	public int hashCode() {
		return Objects.hash(promptTokens, generationTokens, totalTokens);
	}

}
