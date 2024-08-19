package com.learning.ddd.onlinestore.inventory.domain.exception;

import com.learning.ddd.onlinestore.inventory.domain.Product;

public class ProductAlreadyExistsException extends Exception {

	private static final long serialVersionUID = -2483135246410772119L;

	private Product product;

	public ProductAlreadyExistsException(Product product) {
		this.product = product;
	}

	public Product getProduct() {
		return product;
	}

}
