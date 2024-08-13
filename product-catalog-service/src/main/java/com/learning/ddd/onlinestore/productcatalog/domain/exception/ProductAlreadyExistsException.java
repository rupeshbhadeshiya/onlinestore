package com.learning.ddd.onlinestore.productcatalog.domain.exception;

import com.learning.ddd.onlinestore.productcatalog.domain.Product;

public class ProductAlreadyExistsException extends Exception {

	private static final long serialVersionUID = 4876005318043570779L;

	private Product product;

	public ProductAlreadyExistsException(Product product) {
		this.product = product;
	}

	public Product getProduct() {
		return product;
	}

}
