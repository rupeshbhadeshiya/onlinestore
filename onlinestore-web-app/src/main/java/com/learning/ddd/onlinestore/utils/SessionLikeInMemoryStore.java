package com.learning.ddd.onlinestore.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class SessionLikeInMemoryStore {

	private Map<String, Object> inMemoryStore = new HashMap<>();
	
	public SessionLikeInMemoryStore() {
	}
	
	public Object getAttribute(String attributeName) {
		return inMemoryStore.get(attributeName);
	}
	
	public void setAttribute(String attributeName, Object attributeValue) {
		inMemoryStore.put(attributeName, attributeValue);
	}

	public void removeAttribute(String attributeName) {
		inMemoryStore.remove(attributeName);
	}
	
	@Override
	public String toString() {
		return "SessionLikeInMemoryStore: inMemoryStore = " + inMemoryStore;
	}
	
}
