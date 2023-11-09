package org.springframework.ai.processor;

import org.springframework.ai.chain.AiInput;
import org.springframework.ai.chain.AiOutput;

import java.util.List;
import java.util.function.Function;

public interface AiProcessor extends Function<AiInput, AiOutput> {

    List<String> getInputKeys();

    List<String> getOutputKeys();
}
