package org.springframework.ai.core.chain;

import java.util.Map;

public class ChainValues {

	private final Map<String, Object> values;

	public ChainValues(Map<String, Object> values) {
		this.values = values;
	}

	public Map<String, Object> getValues() {
		return values;
	}

}
