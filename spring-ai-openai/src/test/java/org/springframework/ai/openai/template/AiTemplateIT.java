package org.springframework.ai.openai.template;

import org.junit.jupiter.api.Test;
import org.springframework.ai.openai.testutils.AbstractIT;
import org.springframework.ai.parser.BeanOutputParser;
import org.springframework.ai.prompt.PromptTemplate;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AiTemplateIT extends AbstractIT {

	@Test
	void simpleJokeTest() {
		String jokeQuestion = "Tell me a joke about cows";
		String answer = aiTemplate.generate(jokeQuestion);
		System.out.println(answer);
		assertThat(answer).isNotEmpty();
	}

	@Test
	void nounJokeTest() {
		String jokeQuestion = "Tell me a joke about {noun}";
		String answer = aiTemplate.generate(jokeQuestion, Map.of("noun", "dogs"));
		System.out.println(answer);
		assertThat(answer).isNotEmpty();

		NounJoke nounJoke = new NounJoke("dogs");
		answer = aiTemplate.generate(jokeQuestion, nounJoke);
		System.out.println(answer);
		assertThat(answer).isNotEmpty();

	}

	@Test
	void promptTemplateJokeTest() {
		PromptTemplate promptTemplate = new PromptTemplate("Tell me a joke about {noun}");
		String answer = aiTemplate.generate(promptTemplate, Map.of("noun", "dogs"));
		System.out.println(answer);
		assertThat(answer).isNotEmpty();

		NounJoke nounJoke = new NounJoke("dogs");
		answer = aiTemplate.generate(promptTemplate, nounJoke);
		System.out.println(answer);
		assertThat(answer).isNotEmpty();
	}

	@Test
	void testParser() {
		var outputParser = new BeanOutputParser<>(ActorsFilms.class);

		String message = """
				Generate the filmography for the actor {actor}.
				{format}
				""";

		ActorsFilms actorsFilms = aiTemplate.generate(message, outputParser,
				Map.of("actor", "Tom Hanks", "format", outputParser.getFormat()));

		System.out.println(actorsFilms);
		assertThat(actorsFilms.getActor()).isEqualTo("Tom Hanks");
	}

	@Test
	void testParserAppendFormat() {
		ActorsFilms actorsFilms = aiTemplate.generate("Generate the filmography for the actor {actor}",
				new BeanOutputParser<>(ActorsFilms.class), Map.of("actor", "Tom Hanks"));

		System.out.println(actorsFilms);
		assertThat(actorsFilms.getActor()).isEqualTo("Tom Hanks");
	}

	@Test
	void testParserAndAppendFormatAndOnlyUseClazz() {
		ActorsFilms actorsFilms = aiTemplate.generate("Generate the filmography for the actor {actor}",
				ActorsFilms.class, Map.of("actor", "Tom Hanks"));
		System.out.println(actorsFilms);
		assertThat(actorsFilms.getActor()).isEqualTo("Tom Hanks");
	}

	@Test
	void functionalChains() {
		Function<String, String> combinedFunction = generateSynopsis.andThen(generateReview);

		System.out.println(combinedFunction.apply("Tragedy at sunset on the beach"));
	}

	private Function<String, String> generateSynopsis = title -> {
		String synopsisInput = """
				You are a playwright. Given the title of play, it is your job to write a synopsis for that title.

				Title: {title}
				Playwright: This is a synopsis for the above play:""";

		return aiTemplate.generate(synopsisInput, Map.of("title", title));
	};

	private Function<String, String> generateReview = synopsis -> {
		String synopsisInput = """
				You are a play critic from the New York Times. Given the synopsis of play, it is your job to write a review for that play.

				Play Synopsis:
				{synopsis}
				Review from a New York Times play critic of the above play:""";

		return aiTemplate.generate(synopsisInput, Map.of("synopsis", synopsis));
	};

	class NounJoke {

		private final String noun;

		public NounJoke(String noun) {
			this.noun = noun;
		}

		public String getNoun() {
			return noun;
		}

	}

}
