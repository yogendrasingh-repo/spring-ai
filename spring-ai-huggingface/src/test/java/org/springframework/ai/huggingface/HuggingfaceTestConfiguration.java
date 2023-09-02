package org.springframework.ai.huggingface;

import org.springframework.ai.huggingface.client.HuggingfaceAiClient;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

@SpringBootConfiguration
public class HuggingfaceTestConfiguration {

	@Bean
	public HuggingfaceAiClient huggingfaceAiClient() {
		String apiKey = System.getenv("HUGGINGFACE_API_KEY");
		if (!StringUtils.hasText(apiKey)) {
			throw new IllegalArgumentException(
					"You must provide an API key.  Put it in an environment variable under the name HUGGINGFACE_API_KEY");
		}
		HuggingfaceAiClient huggingfaceAiClient = new HuggingfaceAiClient(apiKey,
				"https://d02ram24jaa0nufz.us-east-1.aws.endpoints.huggingface.cloud");
		return huggingfaceAiClient;
	}

}
