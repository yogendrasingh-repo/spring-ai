package org.springframework.ai.chat.engine2;

public interface EngineListener {

	void onComplete(EngineRequest2 engineRequest2, GenerateResponse generateResponse);

}
