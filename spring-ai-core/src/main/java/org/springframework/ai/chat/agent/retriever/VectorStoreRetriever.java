package org.springframework.ai.chat.agent.retriever;

import org.springframework.ai.chat.agent.PromptContext;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.List;
import java.util.stream.Collectors;

public class VectorStoreRetriever implements Retriever {

	private VectorStore vectorStore;

	private SearchRequest searchRequest;

	public VectorStoreRetriever(VectorStore vectorStore, SearchRequest searchRequest) {
		this.vectorStore = vectorStore;
		this.searchRequest = searchRequest;
	}

	public VectorStore getVectorStore() {
		return vectorStore;
	}

	public SearchRequest getSearchRequest() {
		return searchRequest;
	}

	@Override
	public PromptContext retrieve(PromptContext promptContext) {
		List<Message> instructions = promptContext.getPrompt().getInstructions();
		String userMessage = instructions.stream()
			.filter(m -> m.getMessageType() == MessageType.USER)
			.map(m -> m.getContent())
			.collect(Collectors.joining(System.lineSeparator()));
		List<Document> documents = vectorStore.similaritySearch(searchRequest.withQuery(userMessage));
		for (Document document : documents) {
			promptContext.addData(document);
		}
		return promptContext;
	}

}
