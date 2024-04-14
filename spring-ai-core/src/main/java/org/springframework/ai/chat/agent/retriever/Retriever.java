package org.springframework.ai.chat.agent.retriever;

import org.springframework.ai.chat.agent.AgentContext;

@FunctionalInterface
public interface Retriever {

	AgentContext retrieve(AgentContext agentContext);

}
