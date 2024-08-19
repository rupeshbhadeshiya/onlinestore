package com.learning.ddd.onlinestore.productcatalog.domain.exception;

import com.learning.ddd.onlinestore.productcatalog.domain.ProductCatalogItem;

public class ProductCatalogItemAlreadyExistsException extends Exception {

	private static final long serialVersionUID = -5489702924662484179L;

	private ProductCatalogItem productCatalogItem;

	public ProductCatalogItemAlreadyExistsException(ProductCatalogItem productCatalogItem) {
		this.productCatalogItem = productCatalogItem;
	}

	public ProductCatalogItem getProductCatalogItem() {
		return productCatalogItem;
	}

}
