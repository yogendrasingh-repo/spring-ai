package org.springframework.ai.processor;

import org.springframework.ai.chain.AiInput;
import org.springframework.ai.chain.AiOutput;
import org.springframework.ai.client.AiClient;
import org.springframework.ai.parser.OutputParser;
import org.springframework.ai.prompt.Prompt;
import org.springframework.ai.prompt.PromptTemplate;

import java.util.List;
import java.util.Map;

public class AiRequestReplyProcessor implements AiProcessor {

	private final AiClient aiClient;

	private final PromptTemplate promptTemplate;

	private final OutputParser outputParser;

	private final String outputKey;

	public AiRequestReplyProcessor(AiClient aiClient, PromptTemplate promptTemplate, String outputKey,
			OutputParser outputParser) {
		this.aiClient = aiClient;
		this.promptTemplate = promptTemplate;
		this.outputKey = outputKey;
		this.outputParser = outputParser;
	}

	/**
	 * Returns the input keys that the prompt template requires.
	 * @return the input keys that the prompt template requires.
	 */
	@Override
	public List<String> getInputKeys() {
		return promptTemplate.getInputVariables().stream().toList();
	}

	/**
	 * Returns the output keys that the output parser will produce.
	 * @return the output keys that the output parser will produce.
	 */
	@Override
	public List<String> getOutputKeys() {
		return List.of(outputKey);
	}

	@Override
	public AiOutput apply(AiInput aiInput) {
		Prompt prompt = promptTemplate.create(aiInput.getInputData());
		String generationText = aiClient.generate(prompt).getGeneration().getText();
		return new AiOutput(Map.of(outputKey, outputParser.parse(generationText)));
	}

}
