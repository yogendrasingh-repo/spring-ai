package org.springframework.ai.chat.agent;

import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.node.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PromptContext {

	private Prompt prompt; // The most up-to-date prompt to use

	List<Node<?>> dataList; // The most up-to-date data to use for transforming the prompt

	private Prompt originalPrompt;

	private String conversationId;

	private Map<String, Object> processContext;

	public PromptContext(Prompt prompt) {
		this(prompt, new ArrayList<>());
	}

	public PromptContext(Prompt prompt, String conversationId) {
		this(prompt, new ArrayList<>());
		this.conversationId = conversationId;
	}

	public PromptContext(Prompt prompt, List<Node<?>> dataList) {
		this.prompt = prompt;
		this.originalPrompt = prompt;
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

	public Prompt getOriginalPrompt() {
		return originalPrompt;
	}

	public String getConversationId() {
		return conversationId;
	}

	public Map<String, Object> getProcessContext() {
		return processContext;
	}

	public List<Node<?>> getDataList() {
		return dataList;
	}

	public void setDataList(List<Node<?>> dataList) {
		this.dataList = dataList;
	}

	@Override
	public String toString() {
		return "PromptContext{" + "prompt=" + prompt + ", dataList=" + dataList + '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof PromptContext that))
			return false;
		return Objects.equals(prompt, that.prompt) && Objects.equals(dataList, that.dataList);
	}

	@Override
	public int hashCode() {
		return Objects.hash(prompt, dataList);
	}

}
