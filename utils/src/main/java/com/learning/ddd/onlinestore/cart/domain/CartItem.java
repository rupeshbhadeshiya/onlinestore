package com.learning.ddd.onlinestore.cart.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class CartItem {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;					// ex. 1001, unique Id to identify an item uniquely
	private String category;		// ex. Grocery
	private String subCategory;		// ex. Biscuits
	private String name;			// ex. Parle-G
	private int quantity;			// ex. 5
	private Double price;			// ex. 30.0 INR (price of a single item)
	
	// Ignore 
	@JsonIgnore
	// all Cascades except 'REMOVE' that's why not mentioning ALL
	@ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	//@ManyToOne(fetch = FetchType.LAZY) // <- this created big problem!
	@JoinColumn(name = "cartId")		 // <- this created big problem!
	private Cart cart;

	
	public CartItem() {
		
	}
	
	public CartItem(String category, String subCategory, 
			String name, int quantity, Double price) {
		
		super();
		this.category = category;
		this.subCategory = subCategory;
		this.name = name;
		this.quantity = quantity;
		this.price = price;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Cart getCart() {
		return cart;
	}
	
	public void setCart(Cart cart) {
		this.cart = cart;
	}
	
	//-------- specific methods : start -------------
	
	public void increaseQuantity(int quantityToIncrease) {
		this.quantity += quantityToIncrease;
		
	}
	public void decreaseQuantity(int quantityToDecrease) {
		this.quantity -= quantityToDecrease;
	}
	
	public double computeAmount() {
		return this.quantity * this.price;
	}
	
	//-------- specific methods : end -------------

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		//result = prime * result + id;
		//result = prime * result + ((cart == null) ? 0 : cart.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((subCategory == null) ? 0 : subCategory.hashCode());
		result = prime * result + quantity;
		result = prime * result + ((price == null) ? 0 : price.hashCode());
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
		
		CartItem other = (CartItem) obj;
		
		//if (id != other.id)
		//	return false;
		
		//if (cart == null) {
		//	if (other.cart != null)
		//		return false;
		//} else if (!cart.equals(other.cart))
		//	return false;
		
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		
		if (subCategory == null) {
			if (other.subCategory != null)
				return false;
		} else if (!subCategory.equals(other.subCategory))
			return false;
		
		if (quantity != other.quantity)
			return false;
		
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "CartItem [id=" + id
				+ ", name=" + name
				+ ", category=" + category + ", subCategory=" + subCategory 
				+ ", quantity=" + quantity + ", price=" + price 
				//+ ", cart=" + cart 
				+ "]";
	}

}
