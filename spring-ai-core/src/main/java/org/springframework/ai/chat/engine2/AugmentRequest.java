package org.springframework.ai.chat.engine2;

public class AugmentRequest {

	private EngineRequest2 engineRequest2; // Maybe pass around an 'engine context'
											// instead

	private RetrievalResponse retrievalResponse;

	public AugmentRequest(EngineRequest2 engineRequest2, RetrievalResponse retrievalResponse) {
		this.engineRequest2 = engineRequest2;
		this.retrievalResponse = retrievalResponse;
	}

	public RetrievalResponse getRetrievalResponse() {
		return retrievalResponse;
	}

	public EngineRequest2 getEngineRequest2() {
		return engineRequest2;
	}

}
