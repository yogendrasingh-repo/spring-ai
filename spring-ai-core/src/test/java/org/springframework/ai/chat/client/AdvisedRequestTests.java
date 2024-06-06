package org.springframework.ai.chat.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AdvisedRequestTests {

	@Test
	void serDeserAdvisedRequest() throws JsonProcessingException {

		AdvisedRequest.Builder builder = AdvisedRequest.builder();
		AdvisedRequest advisedRequest = builder.withSystemText("This is system text")
			.withUserText("This is user text")
			.build();

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		objectMapper.registerModule(new JavaTimeModule());

		String json = objectMapper.writeValueAsString(advisedRequest);
		System.out.println("AdvisedRequest Ser: " + json);

		AdvisedRequest deserialized = objectMapper.readValue(json, AdvisedRequest.class);
		assertThat(advisedRequest).usingRecursiveComparison().isEqualTo(deserialized);
	}

}
