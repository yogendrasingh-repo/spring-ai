package org.springframework.ai.core.chain;

import java.util.List;
import java.util.function.Function;

public interface Chain extends Function<ChainValues, ChainValues> {

	List<String> getInputKeys();

	List<String> getOutputKeys();

}
