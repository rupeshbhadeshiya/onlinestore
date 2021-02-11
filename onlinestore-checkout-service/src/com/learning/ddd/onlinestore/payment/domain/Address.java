package com.learning.ddd.onlinestore.payment.domain;

public class Address {

	private AddressType type;
	private String line1;
	private String line2;
	private String line3;
	private String line4;
	private String pincode;
	private String state;
	private String country;

	public Address(
			AddressType type, 
			String line1, String line2, String line3, String line4,
			String pincode, String state, String country) {
				this.type = type;
				this.line1 = line1;
				this.line2 = line2;
				this.line3 = line3;
				this.line4 = line4;
				this.pincode = pincode;
				this.state = state;
				this.country = country;
	}

	public AddressType getType() {
		return type;
	}

	public void setType(AddressType type) {
		this.type = type;
	}

	public String getLine1() {
		return line1;
	}

	public void setLine1(String line1) {
		this.line1 = line1;
	}

	public String getLine2() {
		return line2;
	}

	public void setLine2(String line2) {
		this.line2 = line2;
	}

	public String getLine3() {
		return line3;
	}

	public void setLine3(String line3) {
		this.line3 = line3;
	}

	public String getLine4() {
		return line4;
	}

	public void setLine4(String line4) {
		this.line4 = line4;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return "Address [type=" + type + ", line1=" + line1 + ", line2=" + line2 + ", line3=" + line3 + ", line4="
				+ line4 + ", pincode=" + pincode + ", state=" + state + ", country=" + country + "]";
	}
	
}
