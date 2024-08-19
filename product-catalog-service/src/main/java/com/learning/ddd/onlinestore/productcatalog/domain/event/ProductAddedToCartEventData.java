package com.learning.ddd.onlinestore.productcatalog.domain.event;

import java.io.Serializable;

import com.learning.ddd.onlinestore.inventory.domain.Product;

public class ProductAddedToCartEventData implements Serializable {

	private static final long serialVersionUID = 3647688856049525452L;
	
	private Product product;

	public ProductAddedToCartEventData(Product product) {
		this.product = product;
	}

	public Product getProduct() {
		return product;
	}
	
	@Override
	public String toString() {
		return "ProductAddedToCartEventData [product=" + product + "]";
	}

}
