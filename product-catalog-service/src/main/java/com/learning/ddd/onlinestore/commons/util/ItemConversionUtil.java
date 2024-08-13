package com.learning.ddd.onlinestore.commons.util;

import com.learning.ddd.onlinestore.inventory.domain.InventoryItem;
import com.learning.ddd.onlinestore.productcatalog.domain.Product;

public class ItemConversionUtil {
	
	public static Product fromInventoryItemToProduct(InventoryItem inventoryItem) {
		
		return new Product(
			inventoryItem.getCategory(), 
			inventoryItem.getSubCategory(), 
			inventoryItem.getName(), 
			inventoryItem.getPrice(),
			inventoryItem.getQuantity()
		);
	}

}
