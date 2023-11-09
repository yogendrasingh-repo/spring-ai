package org.springframework.ai.processor;

import org.springframework.ai.document.Document;

import java.util.List;
import java.util.function.Function;

public interface AiDocumentProcessor extends Function<List<Document>, List<Document>> {
}
