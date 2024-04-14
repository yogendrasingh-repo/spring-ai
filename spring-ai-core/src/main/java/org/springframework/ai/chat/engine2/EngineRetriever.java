package org.springframework.ai.chat.engine2;

public interface EngineRetriever {

	RetrievalResponse retrieve(RetrievalRequest retrievalRequest);

}
