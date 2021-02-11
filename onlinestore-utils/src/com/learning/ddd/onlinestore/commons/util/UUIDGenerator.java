package com.learning.ddd.onlinestore.commons.util;

import java.util.UUID;

public class UUIDGenerator {

	public static String newUUID() {
		return UUID.randomUUID().toString();
	}
	
}
