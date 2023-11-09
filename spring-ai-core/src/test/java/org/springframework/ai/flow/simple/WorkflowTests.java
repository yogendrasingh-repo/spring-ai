package org.springframework.ai.flow.simple;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chain.AiInput;
import org.springframework.ai.chain.AiOutput;
import org.springframework.ai.client.AiClient;
import org.springframework.ai.client.AiResponse;
import org.springframework.ai.client.Generation;
import org.springframework.ai.document.Document;
import org.springframework.ai.processor.AiRequestReplyProcessor;
import org.springframework.ai.parser.AbstractConversionServiceOutputParser;
import org.springframework.ai.parser.OutputParser;
import org.springframework.ai.prompt.Prompt;
import org.springframework.ai.prompt.PromptTemplate;
import org.springframework.core.convert.support.DefaultConversionService;

import org.springframework.ai.prompt.messages.UserMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WorkflowTests {

    @Test
    void testDocuments() {
        AiDocumentTextAppender appender1 = new AiDocumentTextAppender("append1");
        AiDocumentTextAppender appender2 = new AiDocumentTextAppender("append2");

        var builder = WorkflowBuilder.startWith(appender1).andThen(appender2);

        Workflow<List<Document>, List<Document>> workflow = builder.build();

        Document document = new Document("first document");
        List<Document> documents = new ArrayList<>();
        documents.add(document);

        List<Document> updatedDocs = workflow.apply(documents);

        for (Document updatedDoc : updatedDocs) {
            System.out.println(updatedDoc.getContent());
        }

    }

    @Test
    void testAIInteractions() {
        AiRequestReplyProcessor requestReplyFunction1 = createRR1();
        AiRequestReplyProcessor requestReplyFunction2 = createRR2();

        var builder = WorkflowBuilder.startWith(requestReplyFunction1);

        Workflow<AiInput, AiOutput> workflow = builder.build();

        AiInput aiInput = new AiInput(Map.of("subject", "cows"));

        AiOutput aiOutput = workflow.apply(aiInput);
        System.err.println(" --> " + aiOutput.getOutputData());
        assertThat(aiOutput.getOutputData().get("outdata"))
                .isEqualTo("Why did the cow cross the road? To get to the udder side.");
    }

    private AiRequestReplyProcessor createRR1() {
        AiClient aiClient = mock(AiClient.class);
        Prompt prompt = new Prompt(new UserMessage("Tell me a joke about cows"));
        AiResponse response = new AiResponse(
                List.of(new Generation("Why did the cow cross the road? To get to the udder side.")));

        when(aiClient.generate(prompt)).thenReturn(response);


        PromptTemplate promptTemplate = new PromptTemplate("Tell me a joke about {subject}");
        OutputParser outputParser = new StringOutputParser();

        return new AiRequestReplyProcessor(aiClient, promptTemplate, "outdata", outputParser);
    }

    private AiRequestReplyProcessor createRR2() {
        AiClient aiClient = mock(AiClient.class);
        Prompt prompt = new Prompt(new UserMessage("Tell me a joke about ducks"));
        AiResponse response = new AiResponse(
                List.of(new Generation("Why did the duck cross the road? To get to the quack side.")));
        when(aiClient.generate(prompt)).thenReturn(response);
        PromptTemplate promptTemplate = new PromptTemplate("Tell me a joke about {subject}");
        OutputParser outputParser = new StringOutputParser();

        return new AiRequestReplyProcessor(aiClient, promptTemplate, "outdata", outputParser);
    }

    public class StringOutputParser extends AbstractConversionServiceOutputParser<String> {

        public StringOutputParser() {
            this(new DefaultConversionService());
        }

        public StringOutputParser(DefaultConversionService conversionService) {
            super(conversionService);
        }

        @Override
        public String getFormat() {
            return """
				Your response should be a string
				eg: `foo`
				""";
        }

        @Override
        public String parse(String text) {
            return getConversionService().convert(text, String.class);
        }

    }
}
