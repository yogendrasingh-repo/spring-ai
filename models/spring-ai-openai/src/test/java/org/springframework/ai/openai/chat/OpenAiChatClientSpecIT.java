package org.springframework.ai.openai.chat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.FluentChatClient;
import org.springframework.ai.openai.OpenAiTestConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = OpenAiTestConfiguration.class)
@EnabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches = ".+")
public class OpenAiChatClientSpecIT {


    @Autowired
    private ChatClient chatClient;
    private static final Logger logger = LoggerFactory.getLogger(OpenAiChatClientSpecIT.class);


    @Test
    void simpleTest() {
        FluentChatClient fluentChatClient = new FluentChatClient(chatClient);

        ActorsFilmsRecord actorsFilms = fluentChatClient.chat()
                .user()
                    .text("Generate the filmography of 5 movies for {actor}.")
                        .param("actor", "Tom Hanks")
                        .and()
                    .and()
                .execute();


//        ActorsFilmsRecord actorsFilms = new FluentChatClient().chat(
//                chatSpec -> chatSpec
//                        .user(userSpec -> userSpec.text("Generate the filmography of 5 movies for {actor}.",
//                                textSpec -> textSpec.param("actor", "Tom Hanks"))
//                )
//                .execute());

//        ActorsFilmsRecord actorsFilms = new FluentChatClient().chat(chatSpec ->
//                chatSpec.user(userSpec ->
//                        userSpec.text("Generate the filmography of 5 movies for {actor}.",
//                                textSpec -> textSpec.param("actor", "Tom Hanks")
//                        )
//                )
//        ).execute();



//        ActorsFilmsRecord actorsFilms = fluentChatClient.chat()
//                .user()
//                .text("Generate the filmography of 5 movies for {actor}.")
//                .param("actor", "Tom Hanks")
//                .and()
//                .and()
//                .execute();
//
//
//
//        ActorsFilmsRecord actorsFilms = new FluentChatClient().chat()
//                .user(userSpec -> userSpec
//                        .text("Generate the filmography of 5 movies for {actor}.",
//                                textSpec -> textSpec.param("actor", "Tom Hanks").params(Map.of()) )                             })
//                        .media(mediaSpec -> {
//                            mediaSpec.param(myImage, MimeTypeUtils.IMAGE_PNG);
//                            })
//                )
//                .execute();
//
//        System.out.println(actorsFilms);

    }

    record ActorsFilmsRecord(String actor, List<String> movies) {
    }
}
