package com.learning.ddd.onlinestore.cart.domain;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.learning.ddd.onlinestore.product.domain.Product;

/* This is just a DTO object, to transfer important details from cart-service to other services */

@Entity
//@Table(schema="carts")
public class CartInfo implements Serializable {

	private static final long serialVersionUID = 3298071952190656216L;

	@Id
	private int cartId;				// unique id of the Cart holding the Products
	
	private String consumerId;		// Consumer currently associated with this Cart
	
	private int productCount;			// count of the Products in the Cart (for easy access)
	
	@Embedded
	private List<Product> products;	// Products in the Cart
	
	public CartInfo() {
	}
	
	public CartInfo(int cartId, String consumerId, int itemCount, List<Product> products) {
		super();
		this.cartId = cartId;
		this.consumerId = consumerId;
		this.productCount = itemCount;
		this.products = products;
	}

	public CartInfo(CartInfo CartInfo) {
		super();
		this.cartId = CartInfo.cartId;
		this.consumerId = CartInfo.consumerId;
		this.productCount = CartInfo.productCount;
		this.products = new ArrayList<>();
		for (Product product : CartInfo.products) {
			this.products.add(new Product(product));
		}
	}

	public Double computeAmount() {
		double totalAmount = 0.0; 
		for (Product product : this.products) {
			totalAmount += product.getPrice() * product.getQuantity();
		}
		return totalAmount;
	}
	
	public int getCartId() {
		return cartId;
	}

	public void setCartId(int cartId) {
		this.cartId = cartId;
	}
	
	public String getConsumerId() {
		return consumerId;
	}
	
	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}
	
	public int getProductCount() {
		return productCount;
	}
	
	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}
	
	public List<Product> getProducts() {
		return products;
	}
	
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cartId;
		result = prime * result + ((consumerId == null) ? 0 : consumerId.hashCode());
		result = prime * result + productCount;
		result = prime * result + ((products == null) ? 0 : products.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		CartInfo other = (CartInfo) obj;
		
		if (consumerId == null) {
			if (other.consumerId != null)
				return false;
		} else if (!consumerId.equals(other.consumerId))
			return false;
		
		if (cartId != other.cartId)
			return false;
		
		if (productCount != other.productCount)
			return false;
		
		if (products == null) {
			if (other.products != null)
				return false;
		} else if (!products.equals(other.products))
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "CartInfo [cartId=" + cartId 
				+ ", consumerId=" + consumerId 
				+ ", productCount=" + productCount
				+ ", products=" + products 
				+ "]";
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return new CartInfo(this);
	}

}

