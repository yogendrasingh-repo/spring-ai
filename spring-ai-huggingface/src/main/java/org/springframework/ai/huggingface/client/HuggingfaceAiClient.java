package org.springframework.ai.huggingface.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.client.AiClient;
import org.springframework.ai.client.AiResponse;
import org.springframework.ai.prompt.Prompt;

/**
 * Implementation of {@link AiClient} backed by HuggingFace
 */
public class HuggingfaceAiClient implements AiClient {

	private static final Logger logger = LoggerFactory.getLogger(HuggingfaceAiClient.class);
	private static final String BASE_URL = "https://api-inference.huggingface.co";

	public HuggingfaceAiClient(final String token)

	@Override
	public AiResponse generate(Prompt prompt) {

		prompt.getContents();
		return null;
	}

}
