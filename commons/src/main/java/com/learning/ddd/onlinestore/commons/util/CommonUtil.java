package com.learning.ddd.onlinestore.commons.util;

import java.util.UUID;

public class CommonUtil {

	public static int generateUUID() {
		int randomUUIDhadcode = UUID.randomUUID().hashCode();
		if (randomUUIDhadcode < 0)
			randomUUIDhadcode *= -1;
		return randomUUIDhadcode;
	}

}
