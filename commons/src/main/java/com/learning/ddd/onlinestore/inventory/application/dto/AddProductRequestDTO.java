package com.learning.ddd.onlinestore.inventory.application.dto;

import java.io.Serializable;

import com.learning.ddd.onlinestore.inventory.domain.Product;

public class AddProductRequestDTO implements Serializable {

	private static final long serialVersionUID = 6337292203450157702L;

	private Product product;

	public AddProductRequestDTO(Product product) {
		this.product = product;
	}

	public Product getProduct() {
		return product;
	}
	
	@Override
	public String toString() {
		return "AddProductRequestDTO [product=" + product + "]";
	}
	
}
