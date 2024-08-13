package com.learning.ddd.onlinestore.productcatalog.application.dto;

import java.io.Serializable;
import java.util.List;

import com.learning.ddd.onlinestore.productcatalog.domain.Product;

public class GetProductsResponseDTO implements Serializable {

	private static final long serialVersionUID = 1897982486692848565L;

	private List<Product> products;

	private int productsQuantitiesTotal;
	
	public GetProductsResponseDTO() {
	}

	public GetProductsResponseDTO(List<Product> products, int productsQuantitiesTotal) {
		this.products = products;
		this.productsQuantitiesTotal = productsQuantitiesTotal;
	}
	
	public List<Product> getProducts() {
		return products;
	}
	
	public int getProductsQuantitiesTotal() {
		return productsQuantitiesTotal;
	}

	@Override
	public String toString() {
		return "GetProductsResponseDTO [products=" + products
				+ ", productsQuantitiesTotal=" + productsQuantitiesTotal + "]";
	}
	
	
}
