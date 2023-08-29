package org.springframework.ai.autoconfigure.huggingface;

import org.springframework.boot.context.properties.ConfigurationProperties;

import static org.springframework.ai.autoconfigure.openai.OpenAiProperties.CONFIG_PREFIX;

@ConfigurationProperties(CONFIG_PREFIX)
public class HuggingfaceProperties {

	public static final String CONFIG_PREFIX = "spring.ai.huggingface";

}
