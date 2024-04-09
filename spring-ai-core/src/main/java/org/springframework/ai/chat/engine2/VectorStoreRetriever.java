package org.springframework.ai.chat.engine2;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VectorStoreRetriever implements Retriever {

	private final VectorStore vectorStore;

	private final SearchRequest searchRequest;

	public VectorStoreRetriever(VectorStore vectorStore, SearchRequest searchRequest) {
		this.vectorStore = vectorStore;
		this.searchRequest = searchRequest;
	}

	@Override
	public RetrievalResponse retrieve(RetrievalRequest retrievalRequest) {
		return doRetrieve(retrievalRequest);
	}

	protected RetrievalResponse doRetrieve(RetrievalRequest retrievalRequest) {
		List<Message> instructions = retrievalRequest.getEngineRequest2().getPrompt().getInstructions();
		String userMessage = instructions.stream()
			.filter(m -> m.getMessageType() == MessageType.USER)
			.map(m -> m.getContent())
			.collect(Collectors.joining(System.lineSeparator()));
		SearchRequest updatedSearchRequest = this.searchRequest.withQuery(userMessage);
		List<Document> similarDocuments = vectorStore.similaritySearch(updatedSearchRequest);
		RetrievalResponse retrievalResponse = new RetrievalResponse(similarDocuments, new ArrayList<>());
		return retrievalResponse;
	}

}
