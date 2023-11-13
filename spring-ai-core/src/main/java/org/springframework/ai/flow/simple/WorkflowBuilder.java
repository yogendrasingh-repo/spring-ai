package org.springframework.ai.flow.simple;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class WorkflowBuilder<I, O> {

	// Just handle sequential operations
	List<Function<I, O>> functions = new ArrayList<>();

	public WorkflowBuilder(Function<I, O> initialFunction) {
		this.functions.add(initialFunction);
	}

	public static <I, O> WorkflowBuilder<I, O> startWith(Function<I, O> initialFunction) {
		return new WorkflowBuilder<>(initialFunction);
	}

	public WorkflowBuilder<I, O> andThen(Function<I, O> nextFunction) {
		this.functions.add(nextFunction);
		return this;
	}

	public Workflow<I, O> build() {
		return new Workflow(functions);
	}

}
