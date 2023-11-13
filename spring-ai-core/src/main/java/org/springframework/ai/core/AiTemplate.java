package org.springframework.ai.core;

import org.springframework.ai.client.AiClient;
import org.springframework.ai.client.AiResponse;
import org.springframework.ai.parser.BeanOutputParser;
import org.springframework.ai.parser.OutputParser;
import org.springframework.ai.parser.Parser;
import org.springframework.ai.prompt.Prompt;
import org.springframework.ai.prompt.PromptTemplate;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

public class AiTemplate implements AiOperations {

	private final AiClient aiClient;

	public AiTemplate(AiClient aiClient) {
		this.aiClient = aiClient;
	}

	@Override
	public String generate(String message) {
		return generate(message, new HashMap<>());
	}

	@Override
	public String generate(String message, Map<String, Object> model) {
		PromptTemplate promptTemplate = new PromptTemplate(message, model);
		return generate(promptTemplate, model);
	}

	@Override
	public String generate(String message, Object modelObject) {
		Map<String, Object> propertyMap = extractProperties(modelObject);
		return generate(message, propertyMap);
	}

	@Override
	public String generate(PromptTemplate promptTemplate, Map<String, Object> model) {
		Prompt prompt = promptTemplate.create(model);
		AiResponse aiResponse = execute(prompt);
		return aiResponse.getGeneration().getText();
	}

	@Override
	public String generate(PromptTemplate promptTemplate, Object modelObject) {
		Map<String, Object> propertyMap = extractProperties(modelObject);
		return generate(promptTemplate, propertyMap);
	}

	@Override
	public <T> T generate(String message, Class<T> clazz, Map<String, Object> model) {
		var parser = new BeanOutputParser(clazz);
		return (T) generate(new PromptTemplate(message), parser, model);
	}

	@Override
	public <T> T generate(String message, OutputParser<T> parser, Map<String, Object> model) {
		return generate(new PromptTemplate(message), parser, model);
	}

	@Override
	public <T> T generate(PromptTemplate promptTemplate, OutputParser<T> parser, Map<String, Object> model) {
		PromptTemplate promptTemplateToUse;
		Map<String, Object> modelToUse = new HashMap<>();
		modelToUse.putAll(model);
		if (!promptTemplate.getInputVariables().contains("format") && parser.getFormat() != null) {
			promptTemplateToUse = new PromptTemplate(
					promptTemplate.getTemplate() + System.lineSeparator() + "{format}");
			modelToUse.put("format", parser.getFormat());
		}
		else {
			promptTemplateToUse = promptTemplate;
		}
		Prompt prompt = promptTemplateToUse.create(modelToUse);
		AiResponse aiResponse = execute(prompt);
		return parser.parse(aiResponse.getGeneration().getText());
	}

	protected AiResponse execute(Prompt prompt) {
		return this.aiClient.generate(prompt);
	}

	public static Map<String, Object> extractProperties(Object bean) {
		BeanWrapper beanWrapper = new BeanWrapperImpl(bean);
		PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();

		Map<String, Object> properties = new HashMap<>();
		for (PropertyDescriptor pd : propertyDescriptors) {
			String propertyName = pd.getName();
			if (beanWrapper.isReadableProperty(propertyName)) {
				Object value = beanWrapper.getPropertyValue(propertyName);
				properties.put(propertyName, value);
			}
		}
		return properties;
	}

}
