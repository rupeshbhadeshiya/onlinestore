package com.learning.ddd.onlinestore.product.application.dto;

import java.io.Serializable;
import java.util.List;

import com.learning.ddd.onlinestore.product.domain.Product;

public class GetProductsResponseDTO implements Serializable {

	private static final long serialVersionUID = -3917382665392832698L;

	private List<Product> products;
	
	public GetProductsResponseDTO() {
	}

	public GetProductsResponseDTO(List<Product> products) {
		this.products = products;
	}

	public List<Product> getProducts() {
		return products;
	}

	@Override
	public String toString() {
		return "GetProductsResponseDTO [products=" + products + "]";
	}
	
}
