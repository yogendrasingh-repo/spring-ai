package org.springframework.ai.chat.engine2;

import org.springframework.ai.chat.history.ChatExchange;
import org.springframework.ai.document.Document;

import java.util.ArrayList;
import java.util.List;

public class RetrievalResponse {

	private final List<Document> retrievedDocuments; // TODO have a node supertype to
														// unify these two fields. The
														// supertype can have a 'type'
														// field it identify which type of
														// node it is...

	private final List<ChatExchange> chatExchanges; // TODO see above

	public RetrievalResponse(List<Document> retrievedDocuments, List<ChatExchange> chatExchanges) {
		this.retrievedDocuments = retrievedDocuments;
		this.chatExchanges = chatExchanges;
	}

	public List<Document> getRetrievedDocuments() {
		return retrievedDocuments;
	}

	public List<ChatExchange> getChatExchanges() {
		return chatExchanges;
	}

	@Override
	public String toString() {
		return "RetrievalResponse{" + "retrievedDocuments=" + retrievedDocuments + ", chatExchanges=" + chatExchanges
				+ '}';
	}

	public RetrievalResponse merge(RetrievalResponse retrievalResponse) {
		List<Document> mergedDocuments = new ArrayList<>(retrievedDocuments);
		mergedDocuments.addAll(retrievalResponse.getRetrievedDocuments());

		List<ChatExchange> mergedChatExchanges = new ArrayList<>(chatExchanges);
		mergedChatExchanges.addAll(retrievalResponse.getChatExchanges());

		return new RetrievalResponse(mergedDocuments, mergedChatExchanges);
	}

}
