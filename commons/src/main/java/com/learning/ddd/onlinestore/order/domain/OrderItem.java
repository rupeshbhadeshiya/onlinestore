package com.learning.ddd.onlinestore.order.domain;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.learning.ddd.onlinestore.inventory.domain.Product;

//DDD: ValueObject for Order domain
@Entity
@Table(schema="orders")
public class OrderItem implements Serializable {

	private static final long serialVersionUID = 4523534087165585966L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int itemId;				// ex. 1001, unique Id to identify an item uniquely
	
	@Embedded
	private Product product;
	
//	private int itemId;				// ex. 1001, unique Id to identify an item uniquely
//	private String category;		// ex. Grocery
//	private String subCategory;		// ex. Biscuits
//	private String name;			// ex. Parle-G
//	private int quantity;			// ex. 5
//	private Double price;			// ex. 30.0 INR (price of a single item)
	
	@JsonIgnore
	@ManyToOne(targetEntity = Order.class)
	@JoinColumn(name="orderId", nullable=false)
	private Order order;
	
	@JsonIgnore
	@ManyToOne(targetEntity = OrderTransaction.class)		
	@JoinColumn(name="transactionReceiptId", nullable=true)
	private OrderTransaction transactionReceipt; //it can be null till txn happen
	
	
	public OrderItem() {
		
		super();
		this.setProduct(new Product());
	}
	
//	public OrderItem(String category, String subCategory, String name,
//			double price, int quantity) {
//		this.category = category;
//		this.subCategory = subCategory;
//		this.name = name;
//		this.price = price;
//		this.quantity = quantity;
//	}
	
	public OrderItem(Product product) {

		super();
		this.setProduct(new Product(product));
	}
	
//	public OrderItem(int itemId, String category, String subCategory, String name,
//			double price, int quantity) {
//		this.itemId = itemId;
//		this.category = category;
//		this.subCategory = subCategory;
//		this.name = name;
//		this.price = price;
//		this.quantity = quantity;
//	}

	public OrderItem(OrderItem orderItem) {

		this(orderItem.getProduct());
	}
	
	
	public Product getProduct() {
		return product;
	}
	
	public void setProduct(Product product) {
		this.product = product;
	}
	
	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getName() {
		return this.getProduct().getName();
	}
	
	public void setName(String name) {
		this.getProduct().setName(name);
	}
	
	public String getCategory() {
		return this.getProduct().getCategory();
	}

	public void setCategory(String category) {
		this.getProduct().setCategory(category);
	}

	public String getSubCategory() {
		return this.getProduct().getSubCategory();
	}

	public void setSubCategory(String subCategory) {
		this.getProduct().setSubCategory(subCategory);
	}

	public int getQuantity() {
		return this.getProduct().getQuantity();
	}

	public void setQuantity(int quantity) {
		this.getProduct().setQuantity(quantity);
	}

	public Double getPrice() {
		return this.getProduct().getPrice();
	}

	public void setPrice(Double price) {
		this.getProduct().setPrice(price);
	}
	
	public void setOrder(Order order) {
		this.order = order;
	}
	
	public Order getOrder() {
		return order;
	}
	
	public void setTransactionReceipt(OrderTransaction transactionReceipt) {
		this.transactionReceipt = transactionReceipt;
	}
	
	public OrderTransaction getTransactionReceipt() {
		return transactionReceipt;
	}
	
	//-------- specific methods : start -------------
	
	public void increaseQuantity(int quantityToIncrease) {
		this.getProduct().increaseQuantity(quantityToIncrease);
		
	}
	public void decreaseQuantity(int quantityToDecrease) {
		this.getProduct().decreaseQuantity(quantityToDecrease);
	}
	
	public double computeAmount() {
		return this.getProduct().computeAmount();
	}
	
	//-------- specific methods : end -------------
	
	@Override
	public int hashCode() {
		return this.getProduct().hashCode();
	}

	@Override
	public boolean equals(Object other) {
		
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (getClass() != other.getClass())
			return false;
		
		OrderItem otherOrderItem = (OrderItem) other;
		
		return this.getProduct().equals(otherOrderItem.getProduct());
	}
	
	@Override
	public String toString() {
		return "OrderItem [itemId=" + itemId 
				+ this.getProduct() 
				+ "]";
	}
	
}
