package com.learning.ddd.onlinestore.cart.domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class CartItem implements Serializable {

	private static final long serialVersionUID = 5472900313173264495L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int itemId;				// ex. 1001, unique Id to identify an item uniquely
	private String category;		// ex. Grocery
	private String subCategory;		// ex. Biscuits
	private String name;			// ex. Parle-G
	private Double price;			// ex. 30.0 INR (price of a single item)
	private int quantity;			// ex. 5
	
	// Ignore 
	@JsonIgnore
	// all Cascades except 'REMOVE' that's why not mentioning ALL
	@ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	//@ManyToOne(fetch = FetchType.LAZY) // <- this created big problem!
	@JoinColumn(name = "cartId")		 // <- this created big problem!
	private Cart cart;

	
	public CartItem() {
		
	}
	
	public CartItem(String category, String subCategory, String name, 
			Double price, int quantity) {
		
		super();
		this.category = category;
		this.subCategory = subCategory;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
	}

	public CartItem(CartItem cartItem) {
		
		super();
		this.category = cartItem.category;
		this.subCategory = cartItem.subCategory;
		this.name = cartItem.name;
		this.price = cartItem.price;
		this.quantity = cartItem.quantity;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
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
		
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		
		if (quantity != other.quantity)
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "CartItem [id=" + itemId
				+ ", name=" + name
				+ ", category=" + category + ", subCategory=" + subCategory 
				 + ", price=" + price + ", quantity=" + quantity 
				//+ ", cart=" + cart 
				+ "]";
	}
	
	
	@Override
	public CartItem clone() throws CloneNotSupportedException {
		return new CartItem(this);
	}

}
