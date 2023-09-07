package org.springframework.ai.huggingface.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.client.AiClient;
import org.springframework.ai.client.AiResponse;
import org.springframework.ai.client.Generation;
import org.springframework.ai.huggingface.api.TextGenerationInferenceApi;
import org.springframework.ai.huggingface.invoker.ApiClient;
import org.springframework.ai.huggingface.model.GenerateParameters;
import org.springframework.ai.huggingface.model.GenerateRequest;
import org.springframework.ai.huggingface.model.GenerateResponse;
import org.springframework.ai.prompt.Prompt;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link AiClient} backed by HuggingFace
 */
public class HuggingfaceAiClient implements AiClient {

	private static final Logger logger = LoggerFactory.getLogger(HuggingfaceAiClient.class);

	private static final String BASE_URL = "https://api-inference.huggingface.co";

	private final String apiToken;

	private ApiClient apiClient = new ApiClient();

	private TextGenerationInferenceApi textGenApi = new TextGenerationInferenceApi();

	public HuggingfaceAiClient(final String apiToken, String basePath) {
		this.apiToken = apiToken;
		this.apiClient.setBasePath(basePath);
		this.apiClient.addDefaultHeader("Authorization", "Bearer " + this.apiToken);
		this.textGenApi.setApiClient(this.apiClient);
	}

	@Override
	public AiResponse generate(Prompt prompt) {
		GenerateRequest generateRequest = new GenerateRequest();
		generateRequest.setInputs(prompt.getContents());
		generateRequest.setParameters(new GenerateParameters());
		GenerateResponse generateResponse = this.textGenApi.generate(generateRequest);
		String generatedText = generateResponse.getGeneratedText();
		List<Generation> generations = new ArrayList<>();
		Generation generation = new Generation(generatedText);
		generations.add(generation);
		return new AiResponse(generations);
	}

	public AiResponse generateOld(Prompt prompt) {
		String apiUrl = "https://api-inference.huggingface.co/models/gpt2";
		String requestBody = prompt.getContents();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(this.apiToken);

		HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

		if (response.getStatusCode() == HttpStatus.OK) {
			System.out.println("Response: " + response.getBody());
		}
		else {
			System.out.println("Request failed with status code: " + response.getStatusCode());
		}
		prompt.getContents();
		return null;
	}

}
