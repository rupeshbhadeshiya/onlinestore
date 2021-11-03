package com.learning.ddd.onlinestore.cart.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.learning.ddd.onlinestore.cart.domain.exception.CartItemNotFoundException;

@Entity
public class Cart implements Serializable {

	private static final long serialVersionUID = 8342884428850145205L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cartId;
	
	private String consumerId;	// Consumer currently associated with this Cart
	
	private int itemCount;
	
	// Note: FetchType.LAZY is creating problem so don't use until find a solution
	@OneToMany(
			mappedBy = "cart", 			// Indicate inverse association with Child 
			cascade = CascadeType.ALL, 	// Any change to this Parent cascades to Child
			fetch = FetchType.EAGER,	// Load Children along with loading this Parent
			orphanRemoval = true)		// When remove a Child from items collection -
	private List<CartItem> items;		// - then remove it also from DB (its orphan now)
	
	public Cart() {
	}
	
	public Cart(String consumerId) {
		this.consumerId = consumerId;
	}
	
	public Double computeAmount() {
		double totalAmount = 0.0; 
		for (CartItem item : items) {
			totalAmount += item.getPrice() * item.getQuantity();
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
	
	public int getItemCount() {
		return itemCount;
	}
	
	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}

	//-------- specific methods : start -------------
	
	public List<CartItem> getItems() {
		return items;
	}
	
	// -- A Cart should only allow adding Items, not setting Items!
//	public void setItems(List<CartItem> cartItems) {
//		
//		this.items = cartItems;
//	}
	
	public void addItems(List<CartItem> cartItems) {
		for (CartItem cartItem : cartItems) {
			this.addItem(cartItem);
		}
	}
	
	// addChild(Child child) {
	// 	  children.add(child);
    //    child.setParent(this);
    // }
	public void addItem(CartItem cartItem) {
		
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
	public void removeItem(CartItem cartItemToRemove) throws CartItemNotFoundException {
		
		//System.out.println("removeItem(): cartId=" + cartId + ", cartItem=" + cartItemToRemove);
		
		// A -> B: one side of bi-directional relationship
		boolean wasItemPresent = this.items.remove(cartItemToRemove);
		//System.out.println("removeItem(): wasItemPresent=" + wasItemPresent);
		
		if (!wasItemPresent) {
			throw new CartItemNotFoundException(this, cartItemToRemove);
		}
		
		// B -> A: other side of bi-directional relationship
		cartItemToRemove.setCart(null);
		
		// update itemCount
		//System.out.println("removeItem(): Cart itemCount (before)=" + this.itemCount);
		this.itemCount -= cartItemToRemove.getQuantity();
		//System.out.println("removeItem(): Cart itemCount (after)=" + this.itemCount);
	}

	//-------- specific methods : end -------------

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		//result = prime * result + cartId;
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
		
		Cart other = (Cart) obj;
		
		if (consumerId == null) {
			if (other.consumerId != null)
				return false;
		} else if (!consumerId.equals(other.consumerId))
			return false;
		
		//if (cartId != other.cartId)
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
		return "Cart [cartId=" + cartId 
				+ ", consumerId=" + consumerId 
				+ ", itemCount=" + itemCount
				+ ", items=" + items 
				+ "]";
	}

}
