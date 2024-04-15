package org.springframework.ai.chat.call;

import java.util.Map;

public interface ChatCallOperations {

	String execute(Map<String, Object> userMap);

	String execute(Map<String, Object> userMap, Map<String, Object> systemMap);

	String execute(String userText, Map<String, Object> userMap);

	String execute(String userText, Map<String, Object> userMap, String systemText, Map<String, Object> systemMap);

}
