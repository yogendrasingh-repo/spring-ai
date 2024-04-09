package org.springframework.ai.chat.engine2;

public interface Generator {

	GenerateResponse generate(GenerateRequest generateRequest);

}
