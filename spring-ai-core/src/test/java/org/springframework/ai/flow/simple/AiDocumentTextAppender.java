package org.springframework.ai.flow.simple;

import org.springframework.ai.document.Document;
import org.springframework.ai.processor.AiDocumentProcessor;

import java.util.ArrayList;
import java.util.List;

public class AiDocumentTextAppender implements AiDocumentProcessor {

    private String textToAppend;

    public AiDocumentTextAppender(String textToAppend) {
        this.textToAppend = textToAppend;
    }

    @Override
    public List<Document> apply(List<Document> documents) {
        List<Document> newDocuments = new ArrayList<>();
        for (Document document : documents) {
            newDocuments.add(new Document(document.getContent() + " - " + textToAppend ));
        }
        return newDocuments;
    }
}
