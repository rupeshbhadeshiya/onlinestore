package com.learning.ddd.onlinestore.commons.justtest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JustTest {

	public static void main(String[] args) {
		
		// 1
		
		int randomUUIDhadcode = UUID.randomUUID().hashCode();
		if (randomUUIDhadcode < 0)
			randomUUIDhadcode *= -1;
		System.out.println( randomUUIDhadcode );
		
		// 2
		
		List<Integer> exampleIntegerList = new ArrayList<Integer>();
		
		exampleIntegerList.add(11);
		exampleIntegerList.add(22);
		exampleIntegerList.add(33);
		exampleIntegerList.add(44);
		exampleIntegerList.add(55);
		
		Integer[] exampleIntegerArray = exampleIntegerList.toArray(new Integer[0]);
		
		for (int i = 0; i < exampleIntegerArray.length; i++) {
			System.out.println(exampleIntegerArray[i]);
		}
		
		System.out.println(exampleIntegerArray);
		
	}
	
}
