package com.learning.ddd.onlinestore.order.application.dto;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.learning.ddd.onlinestore.inventory.domain.Product;

/* This is just a DTO object, to transfer important details from order-service to other services */

@Entity
//@Table(schema="orders")
public class OrderInfo implements Serializable {

	private static final long serialVersionUID = -2098795300002751589L;

	@Id
	private int orderId;			// unique id of the Order holding all Products
	
	private int cartId;				// unique id of the Cart holding the Products
	
	private String consumerId;		// Consumer currently associated with this Cart
	
	private int productCount;		// count of the Products in the Cart (for easy access)
	
	@Embedded
	private List<Product> products;	// Products in the Cart
	
	public OrderInfo() {
	}
	
	public OrderInfo(int orderId, int cartId, String consumerId, int itemCount, List<Product> products) {
		super();
		this.orderId = orderId;
		this.cartId = cartId;
		this.consumerId = consumerId;
		this.productCount = itemCount;
		this.products = products;
	}

	public OrderInfo(OrderInfo OrderInfo) {
		super();
		this.orderId = OrderInfo.orderId;
		this.cartId = OrderInfo.cartId;
		this.consumerId = OrderInfo.consumerId;
		this.productCount = OrderInfo.productCount;
		this.products = new ArrayList<>();
		for (Product product : OrderInfo.products) {
			this.products.add(new Product(product));
		}
	}

	public Double computeAmount() {
		double totalAmount = 0.0; 
		for (Product item : products) {
			totalAmount += item.getPrice() * item.getQuantity();
		}
		return totalAmount;
	}
	
	public int getOrderId() {
		return orderId;
	}
	
	public void setOrderId(int orderId) {
		this.orderId = orderId;
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
		result = prime * result + orderId;
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
		
		OrderInfo other = (OrderInfo) obj;
		
		if (consumerId == null) {
			if (other.consumerId != null)
				return false;
		} else if (!consumerId.equals(other.consumerId))
			return false;
		
		if (orderId != other.orderId)
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
		return "CartInfo ["
				+ " orderId=" + orderId
				+ ", cartId=" + cartId 
				+ ", consumerId=" + consumerId 
				+ ", productCount=" + productCount
				+ ", products=" + products 
				+ "]";
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return new OrderInfo(this);
	}

}

