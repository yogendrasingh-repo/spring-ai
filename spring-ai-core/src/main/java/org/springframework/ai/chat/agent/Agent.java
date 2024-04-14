package org.springframework.ai.chat.agent;

public interface Agent {

	AgentResponse call(AgentRequest agentRequest);

}
