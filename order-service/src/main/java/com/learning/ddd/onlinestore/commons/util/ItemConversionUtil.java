package com.learning.ddd.onlinestore.commons.util;

import com.learning.ddd.onlinestore.cart.domain.CartItem;
import com.learning.ddd.onlinestore.order.domain.OrderItem;

public class ItemConversionUtil {
	
	public static OrderItem fromCartItemToOrderItem(CartItem cartItem) {
		
		return new OrderItem(
			cartItem.getCategory(), 
			cartItem.getSubCategory(), 
			cartItem.getName(), 
			cartItem.getPrice(),
			cartItem.getQuantity()
		);
	}

}
