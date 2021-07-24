package com.learning.ddd.onlinestore.commons.onetomany.spike2.ab;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class AandB_APITest {

	private static final String B_NAME = "B1";
	private static final String A_NAME = "A1";
	
	@Autowired
	private ABService abService;
	
	@Test
	@Order(1)
	void createAandB() {
		CreateABDTO dto = new CreateABDTO();
		dto.setaName(A_NAME);
		dto.setbName(B_NAME);
		A a = abService.createAandB(dto);
		assertNotNull(a);
		assertEquals(A_NAME, a.getName());
		assertNotNull(a.getbList());
		assertEquals(1, a.getbList().size());
		assertEquals(B_NAME, a.getbList().get(0).getName());
	}
	
	@Test
	@Order(2)
	void getAandB() {
		List<A> aList = abService.getAandB();
		assertNotNull(aList);
		assertEquals(1, aList.size());
		A a = aList.get(0);
		assertNotNull(a);
		assertEquals(A_NAME, a.getName());
		assertNotNull(a.getbList());
		assertEquals(1, a.getbList().size());
		assertEquals(B_NAME, a.getbList().get(0).getName());
	}
	
	@Test
	@Order(3)
	void deleteAandB() {
		
		A a = abService.getAandB().get(0);
		
		abService.deleteAandB(a);
		
		List<A> aList = abService.getAandB();
		assertNotNull(aList);
		assertEquals(0, aList.size());
	}
	

}
