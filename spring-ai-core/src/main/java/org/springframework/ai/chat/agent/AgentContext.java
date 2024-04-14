package org.springframework.ai.chat.agent;

import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.node.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AgentContext {

	private Prompt prompt; // The most up-to-date prompt to use

	List<Node<?>> dataList; // The most up-to-date data to use for transforming the prompt

	public AgentContext(Prompt prompt) {
		this(prompt, new ArrayList<>());
	}

	public AgentContext(Prompt prompt, List<Node<?>> dataList) {
		this.prompt = prompt;
		this.dataList = dataList;
	}

	public void addData(Node<?> datum) {
		this.dataList.add(datum);
	}

	public Prompt getPrompt() {
		return prompt;
	}

	public void setPrompt(Prompt prompt) {
		this.prompt = prompt;
	}

	public List<Node<?>> getDataList() {
		return dataList;
	}

	public void setDataList(List<Node<?>> dataList) {
		this.dataList = dataList;
	}

	@Override
	public String toString() {
		return "AgentContext{" + "prompt=" + prompt + ", dataList=" + dataList + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof AgentContext that))
			return false;
		return Objects.equals(prompt, that.prompt) && Objects.equals(dataList, that.dataList);
	}

	@Override
	public int hashCode() {
		return Objects.hash(prompt, dataList);
	}

}
