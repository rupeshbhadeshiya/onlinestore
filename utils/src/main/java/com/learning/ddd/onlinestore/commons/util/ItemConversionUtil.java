package com.learning.ddd.onlinestore.commons.util;

import com.learning.ddd.onlinestore.cart.domain.CartItem;
import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;
import com.learning.ddd.onlinestore.order.domain.OrderItem;

public class ItemConversionUtil {

	public static CartItem fromInventoryItemToCartItem(InventoryItem inventoryItem) {
		
		return new CartItem(
			inventoryItem.getCategory(), 
			inventoryItem.getSubCategory(), 
			inventoryItem.getName(), 
			inventoryItem.getQuantity(), 
			inventoryItem.getPrice()
		);
	}
	
	public static InventoryItem fromCartItemToInventoryItem(CartItem cartItem) {
		
		return new InventoryItem(
			cartItem.getCategory(), 
			cartItem.getSubCategory(), 
			cartItem.getName(), 
			cartItem.getQuantity(), 
			cartItem.getPrice()
		);
	}
	
	public static OrderItem fromCartItemToOrderItem(CartItem cartItem) {
		
		return new OrderItem(
			cartItem.getCategory(), 
			cartItem.getSubCategory(), 
			cartItem.getName(), 
			cartItem.getQuantity(), 
			cartItem.getPrice()
		);
	}

	public static InventoryItem fromOrderItemToInventoryItem(OrderItem orderItem) {
		
		return new InventoryItem(
				orderItem.getCategory(), 
				orderItem.getSubCategory(), 
				orderItem.getName(), 
				orderItem.getQuantity(), 
				orderItem.getPrice()
			);
	}

}
