package org.springframework.ai.core.chain;

import org.springframework.ai.core.memory.Memory;

import java.util.List;

public abstract class AbstractChain implements Chain {

	private Memory memory;

	private boolean returnOnlyOutputs;

	private List<String> inputKeys;

	private List<String> outputKeys;

	/**
	 * @return A string that uniquely identifies the type of chain
	 */
	protected abstract String getType();

	protected abstract Memory getMemory();

	public void setReturnOnlyOutputs(boolean returnOnlyOutputs) {
		this.returnOnlyOutputs = returnOnlyOutputs;
	}

	public boolean isReturnOnlyOutputs() {
		return this.returnOnlyOutputs;
	}

	@Override
	public List<String> getInputKeys() {
		return this.inputKeys;
	}

	@Override
	public List<String> getOutputKeys() {
		return this.outputKeys;
	}

	// TODO validation of input/outputs

}
