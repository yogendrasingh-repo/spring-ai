package org.springframework.ai.chat.agent.transformer;

import org.springframework.ai.chat.agent.AgentContext;

@FunctionalInterface
public interface PromptTransformer {

	AgentContext transform(AgentContext agentContext);

}
