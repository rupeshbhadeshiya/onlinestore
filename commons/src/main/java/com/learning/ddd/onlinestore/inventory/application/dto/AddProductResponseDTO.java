package com.learning.ddd.onlinestore.inventory.application.dto;

import java.io.Serializable;

import com.learning.ddd.onlinestore.inventory.domain.Product;

public class AddProductResponseDTO implements Serializable {

	private static final long serialVersionUID = -1769621697376130698L;

	private Product product;

	public AddProductResponseDTO(Product product) {
		this.product = product;
	}

	public Product getProduct() {
		return product;
	}
	
	@Override
	public String toString() {
		return "AddProductResponseDTO [product=" + product + "]";
	}
	
}
