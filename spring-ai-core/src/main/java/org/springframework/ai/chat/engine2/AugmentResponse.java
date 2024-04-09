package org.springframework.ai.chat.engine2;

import org.springframework.ai.chat.prompt.Prompt;

public class AugmentResponse {

	private Prompt prompt;

	public AugmentResponse(Prompt prompt) {
		this.prompt = prompt;
	}

	public Prompt getPrompt() {
		return prompt;
	}

	@Override
	public String toString() {
		return "AugmentResponse{" + "prompt=" + prompt + '}';
	}

}
