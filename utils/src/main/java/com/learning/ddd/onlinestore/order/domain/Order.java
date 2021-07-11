package com.learning.ddd.onlinestore.order.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.learning.ddd.onlinestore.payment.domain.PaymentMethod;

@Entity
@Table(name = "OnlineOrder") // MUST keep table name other than 'Order which conflicts with SQL standard word 'Order'
public class Order implements Serializable {

	private static final long serialVersionUID = -498472061562718714L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int orderId;
	
	private String consumerId;	// Consumer currently associated with this Cart
	
	//private String name, email, mobileNumber;
//	private Date birthDate;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "billingAddressId", referencedColumnName = "addressId")
	private Address billingAddress;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "shippingAddressId", referencedColumnName = "addressId")
	private Address shippingAddress;
	
	// very important to understand that Item associated with Cart and with Order are different objects
	// an Item cannot point to a Cart and an Order at the same time, therefore we must have two different types of Items
	// say, CartItem and OrderItem
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private List<OrderItem> items;
	
	private int itemCount;
	
	private PaymentMethod paymentMethod;		//Payment method chosen by User for Payment
	
	public Order() {
	}
	
//	public Order(int orderId, String name, String email, String mobileNumber, Date birthDate, Address billingAddress,
//			Address shippingAddress, List<OrderItem> items, PaymentMethod paymentMethod) {
//		super();
//		this.orderId = orderId;
//		this.name = name;
//		this.email = email;
//		this.mobileNumber = mobileNumber;
//		this.birthDate = birthDate;
//		this.billingAddress = billingAddress;
//		this.shippingAddress = shippingAddress;
//		this.items = items;
//		this.paymentMethod = paymentMethod;
//	}
	
	public Order(List<OrderItem> items, int itemCount, PaymentMethod paymentMethod, 
			Address billingAddress, Address shippingAddress) {
		super();
		this.items = items;
		this.itemCount = itemCount;
		this.paymentMethod = paymentMethod;
		this.billingAddress = billingAddress;
		this.shippingAddress = shippingAddress;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public String getEmail() {
//		return email;
//	}
//
//	public void setEmail(String email) {
//		this.email = email;
//	}
//
//	public String getMobileNumber() {
//		return mobileNumber;
//	}
//
//	public void setMobileNumber(String mobileNumber) {
//		this.mobileNumber = mobileNumber;
//	}
//
//	public Date getBirthDate() {
//		return birthDate;
//	}
//
//	public void setBirthDate(Date birthDate) {
//		this.birthDate = birthDate;
//	}

	public Address getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(Address address) {
		this.billingAddress = address;
	}
	
	public void setShippingAddress(Address address) {
		this.shippingAddress = address;
	}
	
	public Address getShippingAddress() {
		return shippingAddress;
	}
	
	public void setItems(List<OrderItem> items) {
		this.items = items;
	}
	
	public List<OrderItem> getItems() {
		return items;
	}
	
	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	
	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public int getItemCount() {
		return itemCount;
	}
	
	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}
	
	public String getConsumerId() {
		return consumerId;
	}
	
	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + orderId;
		result = prime * result + ((consumerId == null) ? 0 : consumerId.hashCode());
		result = prime * result + itemCount;
		result = prime * result + ((billingAddress == null) ? 0 : billingAddress.hashCode());
		result = prime * result + ((shippingAddress == null) ? 0 : shippingAddress.hashCode());
//		result = prime * result + ((birthDate == null) ? 0 : birthDate.hashCode());
//		result = prime * result + ((email == null) ? 0 : email.hashCode());
//		result = prime * result + ((mobileNumber == null) ? 0 : mobileNumber.hashCode());
//		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Order other = (Order) obj;
		if (orderId != other.orderId)
			return false;
		if (consumerId == null) {
			if (other.consumerId != null)
				return false;
		} else if (!consumerId.equals(other.consumerId))
			return false;
		if (itemCount != other.itemCount) 
			return false;
		if (billingAddress == null) {
			if (other.billingAddress != null)
				return false;
		} else if (!billingAddress.equals(other.billingAddress))
			return false;
//		if (birthDate == null) {
//			if (other.birthDate != null)
//				return false;
//		} else if (!birthDate.equals(other.birthDate))
//			return false;
//		if (email == null) {
//			if (other.email != null)
//				return false;
//		} else if (!email.equals(other.email))
//			return false;
//		if (mobileNumber == null) {
//			if (other.mobileNumber != null)
//				return false;
//		} else if (!mobileNumber.equals(other.mobileNumber))
//			return false;
//		if (name == null) {
//			if (other.name != null)
//				return false;
//		} else if (!name.equals(other.name))
//			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Order [orderId=" + orderId
//				+ ", name=" + name + ", email=" + email
//				+ ", mobileNumber=" + mobileNumber
//				+ ", birthDate=" + birthDate
				+ ", items="+items
				+ ", paymentMethod="+paymentMethod
				+ ", billingAddress(Id)="+billingAddress.getCountry()
				+ ", shippingAddress(id)="+shippingAddress.getCountry()
				+ "]";
	}
	
}
