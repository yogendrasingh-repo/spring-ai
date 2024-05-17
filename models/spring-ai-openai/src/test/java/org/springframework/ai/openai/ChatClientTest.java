package org.springframework.ai.openai;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

class ChatClientTest {

	@Configuration
	static class ChatClientTestConfiguration {

		@Bean
		ChatClient client(OpenAiChatConnector openAiChatConnector) {
			return ChatClient.builder(openAiChatConnector).defaultSystemPrompt("""
					    you are customer service agent designed to answer questions
					    about a the user, {userName}'s, orders. Here are their outstanding orders.

					    {orders}

					""").defaultFunctions("cancelOrder", "refundOrder").build();
		}

	}

	private final ChatClient singularity;

	ChatClientTest(@Autowired ChatClient singularity) {
		this.singularity = singularity;
	}

	@Test
	void products() throws Exception {
		var product0 = this.client.userPrompt("tell me about this product from the merchant {merchant}")
			.userPromptParams(Map.of("merchant", "24u92"))
			.execute(Product.class);

		/*
		 * var product1 = this.client .build() .userPromptParam("a", "b")
		 * .functions("cancelOrder", "refundOrder") .execute(new
		 * ParameterizedTypeReference<Product>() { });
		 *
		 * var product2 = this.client
		 * .userPrompt("tell me about this product from the merchant {merchant}",
		 * Map.of("merchant", "232")) .execute(Product.class);
		 */

	}

	record Product(String sku) {
	}

}
