/*
 * Copyright 2024 - 2024 the original author or authors.
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
package org.springframework.ai.openai.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.openai.metadata.OpenAiChatResponseMetadata;
import org.springframework.ai.openai.metadata.OpenAiRateLimit;
import org.springframework.ai.openai.metadata.OpenAiUsage;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

public class OpenAiChatResponseTests {

	@Test
	void serDeserChatResponseMetadata() throws JsonProcessingException {
		OpenAiUsage openAiUsage = new OpenAiUsage(new OpenAiApi.Usage(1, 2, 3));
		OpenAiRateLimit openAiRateLimit = new OpenAiRateLimit(1L, 2L, Duration.ZERO, 4L, 5L, Duration.ZERO);
		OpenAiChatResponseMetadata chatResponseMetadata = new OpenAiChatResponseMetadata("myid", openAiUsage,
				openAiRateLimit);

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		objectMapper.registerModule(new JavaTimeModule());

		String json = objectMapper.writeValueAsString(chatResponseMetadata);
		System.out.println("ChatResponseMetadata Ser: " + json);

		OpenAiChatResponseMetadata deserialized = objectMapper.readValue(json, OpenAiChatResponseMetadata.class);
		assertThat(chatResponseMetadata).usingRecursiveComparison().isEqualTo(deserialized);
	}

	@Test
	void promptSerialization() throws JsonProcessingException {
		Prompt prompt = new Prompt(new UserMessage("hello world"));

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

		String json = objectMapper.writeValueAsString(prompt);
		System.out.println("Prompt Ser: " + json);

		Prompt deserializedPrompt = objectMapper.readValue(json, Prompt.class);
		assertThat(prompt).usingRecursiveComparison().isEqualTo(deserializedPrompt);
	}

}
