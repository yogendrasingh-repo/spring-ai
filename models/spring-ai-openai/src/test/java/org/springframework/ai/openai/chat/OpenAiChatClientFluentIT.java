package org.springframework.ai.openai.chat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.FluentChatClientLambda;
import org.springframework.ai.openai.OpenAiTestConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = OpenAiTestConfiguration.class)
@EnabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches = ".+")
public class OpenAiChatClientSpecLambdaIT {


    @Autowired
    private ChatClient chatClient;
    private static final Logger logger = LoggerFactory.getLogger(OpenAiChatClientSpecLambdaIT.class);


    @Test
    void simpleTest() {
        var liquidChatClient = new FluentChatClientLambda.LiquidChatClient(this.chatClient);
        var actors = liquidChatClient
                .chat(s -> s.system(sys -> sys.text("""
						You're a non user hostile chatbot from cyberdyne systems.
						your primary objective is {primaryObjective}
					""").params(Map.of("primaryObjective", "No PHP")))
                        .functions(fn -> fn.functions((FunctionCallback) null)
                                .functions("createReservation", "cancelReservations"))
                        .user(user -> user.text("tell me a joke about {topic}")
                                .params(Map.of("topic", "PHP"))
                                .media(new Media[0])))
                .call(ActorsFilmsRecord2.class);

    }

    record ActorsFilmsRecord2(String actor, List<String> movies) {
    }
}
