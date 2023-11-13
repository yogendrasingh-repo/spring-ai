package org.springframework.ai.openai.client;

import org.junit.jupiter.api.Test;
import org.springframework.ai.openai.testutils.AbstractIT;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

@SpringBootTest
public class RouteTestIT extends AbstractIT {

	private Function<String, String> classifyQuestion = question -> {
		String classifyQuestion = """
				"Classify the topic of the user question."

				 Here is a question:
				 {question}

				"Reply with the topic of the user question can only be one of 'math' or 'physics'.""";
		String response = aiTemplate.generate(classifyQuestion, Map.of("question", question));
		System.out.println("classifyQuestion: " + response);
		return response;
	};

	private Function<String, String> mathQuestion = question -> {

		String mathQuestion = """
				You are a very good mathematician. You are great at answering math questions. \\
				You are so good because you are able to break down hard problems into their component parts, \\
				answer the component parts, and then put them together to answer the broader question.

				Here is a question:
				{question}
				""";
		System.out.println("Running math function");
		String response = aiTemplate.generate(mathQuestion, Map.of("question", question));
		System.out.println("mathQuestion: " + response);
		return response;
	};

	private Function<String, String> physicsQuestion = question -> {

		String physicsQuestion = """
				You are a very smart physics professor.
				You are great at answering questions about physics in a concise and easy to understand manner. \\
				When you don't know the answer to a question you admit that you don't know.

				Here is a question:
				{question}
				  """;
		System.out.println("Running physics function");
		return aiTemplate.generate(physicsQuestion, Map.of("question", question));
	};

	@Test
	void testRoute() {

		String question = "What is the first prime number greater than 40 such that one plus the prime number is divisible by 3?";
		// String question = "Tell me about Albert Einstein's theory of relativity";

		RouteFunction<String, String> routeFunction = RouteBuilderImpl.<String, String>route()
			.onCondition(output -> output.startsWith("math"))
			.thenUse(mathQuestion)
			.orElseUse(physicsQuestion);

		String classifiedOutput = classifyQuestion.apply(question);
		String finalAnswer = routeFunction.apply(classifiedOutput);
		System.out.println(finalAnswer);
	}

	interface RouteBuilder<T, R> {

		ConditionRouteBuilder<T, R> onCondition(Predicate<T> condition);

	}

	interface ConditionRouteBuilder<T, R> {

		RouteFunction<T, R> thenUse(Function<T, R> function);

	}

	interface RouteFunction<T, R> {

		R apply(T input);

		RouteFunction<T, R> orElseUse(Function<T, R> function);

	}

	static class RouteBuilderImpl<T, R>
			implements RouteBuilder<T, R>, ConditionRouteBuilder<T, R>, RouteFunction<T, R> {

		private Predicate<T> condition;

		private Function<T, R> trueFunction;

		private Function<T, R> falseFunction = t -> null; // Default false function

		public static <T, R> RouteBuilder<T, R> route() {
			return new RouteBuilderImpl<>();
		}

		@Override
		public ConditionRouteBuilder<T, R> onCondition(Predicate<T> condition) {
			this.condition = condition;
			return this;
		}

		@Override
		public RouteFunction<T, R> thenUse(Function<T, R> function) {
			this.trueFunction = function;
			return this;
		}

		@Override
		public RouteFunction<T, R> orElseUse(Function<T, R> function) {
			this.falseFunction = function;
			return this;
		}

		@Override
		public R apply(T input) {
			return condition.test(input) ? trueFunction.apply(input) : falseFunction.apply(input);
		}

	}

}
