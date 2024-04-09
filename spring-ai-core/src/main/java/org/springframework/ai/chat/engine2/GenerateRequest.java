package org.springframework.ai.chat.engine2;

public class GenerateRequest {

	private EngineRequest2 engineRequest2; // Maybe pass around an 'engine context'
											// instead

	AugmentResponse augmentResponse;

	public GenerateRequest(EngineRequest2 engineRequest2, AugmentResponse augmentResponse) {
		this.engineRequest2 = engineRequest2;
		this.augmentResponse = augmentResponse;
	}

	public EngineRequest2 getEngineRequest2() {
		return engineRequest2;
	}

	public AugmentResponse getAugmentResponse() {
		return augmentResponse;
	}

}
