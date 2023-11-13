package org.springframework.ai.core;

import org.springframework.ai.client.Generation;
import org.springframework.ai.parser.OutputParser;
import org.springframework.ai.parser.Parser;
import org.springframework.ai.prompt.PromptTemplate;

import java.util.List;
import java.util.Map;

public interface AiOperations {

	// remove default method in AiClient?, the helper string->string function is here.

	String generate(String message);

	String generate(String message, Map<String, Object> model);

	String generate(String message, Object modelObject);

	String generate(PromptTemplate promptTemplate, Map<String, Object> model);

	String generate(PromptTemplate promptTemplate, Object modelObject);
	//
	// Generation generation(PromptTemplate promptTemplate, Map<String, Object> model);
	// Generation generation(PromptTemplate promptTemplate, Object modelObject);

	// List<T> based on passed in 'RowMapper<T>, aka OutputParser
	// <T> List<T> queryForList(String sql, Class<T> elementType, @Nullable Object...
	// args) throws DataAccessException;

	// <T> T query(String sql, ResultSetExtractor<T> rse)

	<T> T generate(String message, Class<T> elementType, Map<String, Object> model);

	<T> T generate(String message, OutputParser<T> parser, Map<String, Object> model);

	<T> T generate(PromptTemplate promptTemplate, OutputParser<T> parser, Map<String, Object> model);

	// <T> List<T> generateList(String message, OutputParser<T> parser, Map<String,
	// Object> model);
	// <T> List<T> generateList(PromptTemplate promptTemplate, OutputParser<T> parser,
	// Map<String, Object> model);

	// if want this, just use AIClient?
	// AiResponse generateResponse(PromptTemplate promptTemplate, Map<String, Object>
	// model);
	// AiResponse generateResponse(PromptTemplate promptTemplate, Object modelObject);

	// String generate(Resource promptTemplateResource, Map<String, Object> model);
	// String generate(Resource promptTemplateResource, Object modelObject);

}
