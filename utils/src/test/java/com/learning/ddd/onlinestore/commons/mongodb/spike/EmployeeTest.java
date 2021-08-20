package com.learning.ddd.onlinestore.commons.mongodb.spike;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmployeeTest {

	@Autowired
	private EmployeeRepository repository;
	
	@Test
	void test() {
		
	  repository.deleteAll();
	
	  // save a couple of Employees
	  repository.save(new Employee("Alice", "Smith"));
	  repository.save(new Employee("Bob", "Smith"));
	
	  // fetch all Employees
	  System.out.println("Employees found with findAll():");
	  System.out.println("-------------------------------");
	  for (Employee employee : repository.findAll()) {
	    System.out.println(employee);
	  }
	  System.out.println();
	
	  // fetch an individual Employee
	  System.out.println("Employees found with findByFirstName('Alice'):");
	  System.out.println("--------------------------------");
	  System.out.println(repository.findByFirstName("Alice"));
	
	  System.out.println("Employees found with findByLastName('Smith'):");
	  System.out.println("--------------------------------");
	  for (Employee employee : repository.findByLastName("Smith")) {
	    System.out.println(employee);
	  }
	
	}

}
