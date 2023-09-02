package org.springframework.ai.huggingface.client;

import org.junit.jupiter.api.Test;
import org.springframework.ai.huggingface.testutils.AbstractIntegrationTest;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ClientIntegrationTests extends AbstractIntegrationTest {

	@Test
	void foo() {

	}

	void helloWorldCompletion() {
		String question = """
				Please answer the following question.  Tell me about Captain Kidd, a famous pirate in the golden age of pirates?  Write three sentences please.""";
		String response = huggingfaceAiClient.generate(question);
		System.out.println("response: " + response);
	}

}
