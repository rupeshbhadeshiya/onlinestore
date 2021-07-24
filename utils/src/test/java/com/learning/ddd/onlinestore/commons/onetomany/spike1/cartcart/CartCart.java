package com.learning.ddd.onlinestore.commons.onetomany.spike1.cartcart;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class CartCart {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private String consumerId;	// Consumer currently associated with this Cart
	
	private int itemCount;
	
	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<CartCartItem> items;
	
	public CartCart() {
	}
	
	public Double computeAmount() {
		double totalAmount = 0.0; 
		for (CartCartItem item : items) {
			totalAmount += item.getPrice() * item.getQuantity();
		}
		return totalAmount;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getConsumerId() {
		return consumerId;
	}
	
	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}
	
	public int getItemCount() {
		return itemCount;
	}
	
	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}

	//-------- specific methods : start -------------
	
	public List<CartCartItem> getItems() {
		return items;
	}
	
	// -- A Cart should only allow adding Items, not setting Items!
//	public void setItems(List<CartItem> cartItems) {
//		
//		this.items = cartItems;
//	}
	
	public void addItems(List<CartCartItem> cartItems) {
		for (CartCartItem cartItem : cartItems) {
			this.addItem(cartItem);
		}
	}
	
	// addChild(Child child) {
	// 	  children.add(child);
    //    child.setParent(this);
    // }
	public void addItem(CartCartItem cartItem) {
		
		// initialize if necessary
		if (this.items == null) {
			this.items = new ArrayList<>();
		}
		
		// A -> B: one side of bi-directional relationship
		if (this.items.contains(cartItem)) {
			this.items.set( this.items.indexOf(cartItem), cartItem );
		} else {
			this.items.add(cartItem);
		}
		
		// B -> A: other side of bi-directional relationship
		cartItem.setCart(this);
		
		// update itemCount
		this.itemCount += cartItem.getQuantity();
	}
	
	// removeChild(Child child) {
    //    children.remove(child);
    //    child.setParent(null);
    // }
	public void removeItem(CartCartItem cartItem) {
		
		// A -> B: one side of bi-directional relationship
		this.items.remove(cartItem);
		
		// B -> A: other side of bi-directional relationship
		cartItem.setCart(null);
		
		// update itemCount
		this.itemCount -= cartItem.getQuantity();
	}

	//-------- specific methods : end -------------

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		//result = prime * result + id;
		result = prime * result + ((consumerId == null) ? 0 : consumerId.hashCode());
		result = prime * result + itemCount;
		result = prime * result + ((items == null) ? 0 : items.hashCode());
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
		
		CartCart other = (CartCart) obj;
		
		if (consumerId == null) {
			if (other.consumerId != null)
				return false;
		} else if (!consumerId.equals(other.consumerId))
			return false;
		
		//if (id != other.id)
		//	return false;
		
		if (itemCount != other.itemCount)
			return false;
		
		if (items == null) {
			if (other.items != null)
				return false;
		} else if (!items.equals(other.items))
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "Cart [id=" + id 
				+ ", consumerId=" + consumerId 
				+ ", itemCount=" + itemCount
				+ ", items=" + items 
				+ "]";
	}

}
