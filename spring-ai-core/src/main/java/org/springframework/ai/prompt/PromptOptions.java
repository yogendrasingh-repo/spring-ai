package org.springframework.ai.prompt;

import java.util.HashMap;
import java.util.Map;

public class PromptOptions {

	protected Map<String, Object> options = new HashMap<>();

	public Map<String, Object> getOptions() {
		return options;
	}

	public <T> T getOrDefault(String key, T defaultValue, Class<T> expectedType) {
		Object value = options.getOrDefault(key, defaultValue);

		if (expectedType.isInstance(value)) {
			return expectedType.cast(value);
		}
		else {
			throw new ClassCastException("Value for key " + key + " is not of type " + expectedType.getSimpleName());
		}
	}

}
