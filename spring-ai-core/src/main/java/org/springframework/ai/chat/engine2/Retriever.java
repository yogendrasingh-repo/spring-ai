package org.springframework.ai.chat.engine2;

public interface Retriever {

	RetrievalResponse retrieve(RetrievalRequest retrievalRequest);

}
