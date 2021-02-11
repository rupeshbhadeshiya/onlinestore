package com.learning.ddd.onlinestore.payment.domain;

public class TestAddressFactory {

	public static Address dummyAddress(AddressType addressType) {
		
		Address address = new Address(
			addressType, 
			"l1", "l2", "l3", "l4", 
			"pincode", "state", "country"
		);
		
		return address;
	}

}
