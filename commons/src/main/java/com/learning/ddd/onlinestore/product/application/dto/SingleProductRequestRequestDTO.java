package com.learning.ddd.onlinestore.product.application.dto;

import java.io.Serializable;

import javax.validation.Valid;

import com.learning.ddd.onlinestore.product.domain.Product;

public class SingleProductRequestRequestDTO implements Serializable {

	private static final long serialVersionUID = 6337292203450157702L;

	@Valid
	private Product product;

	// This no-arg default constructor is required by Spring ApplicationContext
	// without this, it result in below error when a REST Controller method is
	// invoked having this object as parameter
	// ...
	// com.fasterxml.jackson.databind.exc.InvalidDefinitionException: 
	//	Cannot construct instance of `com.learning.ddd.onlinestore.inventory.application.dto.SingleProductRequestRequestDTO`
	//  (no Creators, like default construct, exist)
	// ...
	public SingleProductRequestRequestDTO() {
	}
	
	public SingleProductRequestRequestDTO(Product product) {
		this.product = product;
	}

	public Product getProduct() {
		return product;
	}
	
	@Override
	public String toString() {
		return "SingleProductRequestRequestDTO [product=" + product + "]";
	}
	
}
