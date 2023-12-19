package org.springframework.ai.openai.client;

import org.springframework.ai.client.ChatOptions;

public class OpenAiChatOptions extends ChatOptions {

	public static final String FREQUENCY_PENALTY = "frequencyPenalty";

	public static final String PRESENCE_PENALTY = "presencePenalty";

	private Float frequencyPenalty;

	private Float presencePenalty;

	public void setFrequencyPenalty(Float frequencyPenalty) {
		// validation that it is between -2 and 2
		options.put(FREQUENCY_PENALTY, frequencyPenalty);
	}

	public void setPresencePenalty(Float presencePenalty) {
		// validation that it is between -2 and 2
		options.put(PRESENCE_PENALTY, presencePenalty);
	}

}
