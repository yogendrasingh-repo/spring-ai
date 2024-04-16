package org.springframework.ai.chat.evaluation;

@FunctionalInterface
public interface Evaluator {

	EvaluationResponse evaluate(EvaluationRequest evaluationRequest);

}
