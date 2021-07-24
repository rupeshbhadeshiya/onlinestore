package com.learning.ddd.onlinestore.order;

import org.springframework.stereotype.Component;

import com.learning.ddd.onlinestore.order.domain.Address;
import com.learning.ddd.onlinestore.order.domain.AddressType;

@Component
public class DummyAddressFactory {

	public static Address dummyAddress(AddressType addressType) {
		
		Address address = new Address(
			addressType, 
			"l1", "l2", "l3", "l4", 
			"pincode", "state", "country"
		);
		
		return address;
	}

}
