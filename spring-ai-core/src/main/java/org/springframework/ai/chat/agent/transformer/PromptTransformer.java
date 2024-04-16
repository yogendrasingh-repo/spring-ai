package org.springframework.ai.chat.agent.transformer;

import org.springframework.ai.chat.agent.PromptContext;

@FunctionalInterface
public interface PromptTransformer {

	PromptContext transform(PromptContext promptContext);

}
