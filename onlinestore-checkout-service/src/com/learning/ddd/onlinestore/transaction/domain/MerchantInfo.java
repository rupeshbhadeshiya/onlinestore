package com.learning.ddd.onlinestore.transaction.domain;

public class MerchantInfo {

	private String name;
	private String phone;
	private String email;
	private String addressLine1;
	private String addressLine2;
	private String addressLine3;
	
	public MerchantInfo() {
	}
	
	public MerchantInfo(String name, String phone, String email, String addressLine1, String addressLine2,
			String addressLine3) {
		super();
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.addressLine3 = addressLine3;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddressLine1() {
		return addressLine1;
	}
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	public String getAddressLine2() {
		return addressLine2;
	}
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}
	public String getAddressLine3() {
		return addressLine3;
	}
	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
	}

	@Override
	public String toString() {
		return "MerchantInfo [name=" + name + ", phone=" + phone + ", email=" + email + ", addressLine1=" + addressLine1
				+ ", addressLine2=" + addressLine2 + ", addressLine3=" + addressLine3 + "]";
	}
	
}
