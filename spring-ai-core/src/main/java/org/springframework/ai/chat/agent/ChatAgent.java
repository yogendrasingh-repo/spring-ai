package org.springframework.ai.chat.agent;

public interface ChatAgent {

	AgentResponse call(AgentRequest agentRequest);

}
