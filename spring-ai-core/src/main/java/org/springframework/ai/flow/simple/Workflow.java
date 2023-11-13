package org.springframework.ai.flow.simple;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Workflow<I, O> implements Function<I, O> {

	List<Function<I, O>> functions = new ArrayList<>();

	private Function<O, I> converter = new TypeCastingConverter<>();

	public Workflow(List<Function<I, O>> functions) {
		if (functions != null) {
			this.functions.addAll(functions);
		}
	}

	public Workflow<I, O> setConverter(Function<O, I> converter) {
		this.converter = converter;
		return this;
	}

	public O apply(I input) {
		O result = null;
		for (Function<I, O> function : functions) {
			result = function.apply(input);
			// If a converter is set, convert the result back to I for the next function
			if (converter != null) {
				input = converter.apply(result);
			}
		}
		return result;
	}

	public class TypeCastingConverter<O, I> implements Function<O, I> {

		@SuppressWarnings("unchecked")
		@Override
		public I apply(O output) {
			// This cast is inherently unsafe and should be avoided if possible.
			// It will throw a ClassCastException at runtime if O and I are not the same
			// type.
			return (I) output;
		}

	}

}
