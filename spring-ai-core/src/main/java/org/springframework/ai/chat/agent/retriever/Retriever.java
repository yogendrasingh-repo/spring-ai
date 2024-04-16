package org.springframework.ai.chat.agent.retriever;

import org.springframework.ai.chat.agent.PromptContext;

@FunctionalInterface
public interface Retriever {

	PromptContext retrieve(PromptContext promptContext);

}
