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
package org.springframework.ai.openai.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.openai.metadata.OpenAiChatResponseMetadata;

public class ChatResponseJsonTests {

	@Test
	void serDeserChatResponse() throws JsonProcessingException {

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.registerModule(new OpenAiChatResponseMetadata.Module());

		String json = """
				{
				  "results" : [ {
				    "assistantMessage" : {
				      "messageType" : "ASSISTANT",
				      "content" : "Why couldn't the bicycle find its way home? Because it lost its bearings!",
				      "metadata" : {
				        "finishReason" : "STOP",
				        "role" : "ASSISTANT",
				        "id" : "chatcmpl-9XBoXJwhroi7d6vD8ncbNUl2vIOyk",
				        "messageType" : "ASSISTANT"
				      },
				      "messageType" : "ASSISTANT",
				      "media" : [ ]
				    },
				    "chatGenerationMetadata" : {
				      "type" : "default",
				      "finishReason" : "STOP",
				      "contentFilterMetadata" : null
				    }
				  } ],
				  "advisorContext" : { },
				  "metadata" : {
				    "type" : "openai",
				    "id" : "chatcmpl-9XBoXJwhroi7d6vD8ncbNUl2vIOyk",
				    "usage" : {
				      "promptTokens" : 11,
				      "generationTokens" : 16,
				      "totalTokens" : 27
				    },
				    "rateLimit" : {
				      "requestsLimit" : 5000,
				      "requestsRemaining" : 4999,
				      "requestsReset" : 0.012000000,
				      "tokensLimit" : 160000,
				      "tokensRemaining" : 159979,
				      "tokensReset" : 0.007000000
				    }
				  }
				}

				""";

		ChatResponse deserializedChatResponse = objectMapper.readValue(json, ChatResponse.class);
		System.out.println(deserializedChatResponse);
		// assertThat(advisedRequest).usingRecursiveComparison().isEqualTo(deserialized);

	}

}
