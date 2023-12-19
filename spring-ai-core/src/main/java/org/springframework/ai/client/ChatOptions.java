package org.springframework.ai.client;

import org.springframework.ai.prompt.PromptOptions;

public class ChatOptions extends PromptOptions {

	public static final String TEMPERATURE = "temperature";

	public static final String MAX_TOKENS = "maxTokens";

	private Float temperature;

	private Integer maxTokens;

	// Other common chat options TBD

	public void setTemperature(Float temperature) {
		options.put(TEMPERATURE, temperature);
	}

	public void setMaxTokens(Integer maxTokens) {
		options.put(MAX_TOKENS, maxTokens);
	}

	public static class Builder {

		private Float temperature;

		private Integer maxTokens;

		public Builder withTemperature(Float temperature) {
			this.temperature = temperature;
			return this;
		}

		public Builder withMaxTokens(Integer maxTokens) {
			this.maxTokens = maxTokens;
			return this;
		}

		public ChatOptions build() {
			ChatOptions chatOptions = new ChatOptions();
			chatOptions.setTemperature(this.temperature);
			chatOptions.setMaxTokens(this.maxTokens);
			return chatOptions;
		}

	}

}
