package org.springframework.ai.chat.engine2;

public class RetrievalRequest {

	private final EngineRequest2 engineRequest2;

	private int tokenRunningTotal = 0; // TODO move elsewhere, a shared engine or
										// retrieval context?

	public RetrievalRequest(EngineRequest2 engineRequest2) {
		this.engineRequest2 = engineRequest2;
	}

	public EngineRequest2 getEngineRequest2() {
		return engineRequest2;
	}

	public String getConversationId() {
		return this.engineRequest2.getConversationId();
	}

	public int getTokenRunningTotal() {
		return tokenRunningTotal;
	}

	public void addTokenCount(int tokenCount) {
		this.tokenRunningTotal += tokenCount;
	}

}
