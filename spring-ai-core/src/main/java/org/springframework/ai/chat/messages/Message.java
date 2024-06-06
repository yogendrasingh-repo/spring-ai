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
package org.springframework.ai.chat.messages;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.ai.model.Content;

/**
 * The Message interface represents a message that can be sent or received in a chat
 * application. Messages can have content, media attachments, properties, and message
 * types.
 *
 * @see Media
 * @see MessageType
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "messageType")
@JsonSubTypes({ @JsonSubTypes.Type(value = UserMessage.class, name = "USER"),
		@JsonSubTypes.Type(value = SystemMessage.class, name = "SYSTEM"),
		@JsonSubTypes.Type(value = AssistantMessage.class, name = "ASSISTANT"),
		@JsonSubTypes.Type(value = FunctionMessage.class, name = "FUNCTION") })
public interface Message extends Content {

	MessageType getMessageType();

}
