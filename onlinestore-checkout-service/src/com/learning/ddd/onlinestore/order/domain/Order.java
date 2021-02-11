package com.learning.ddd.onlinestore.order.domain;

import java.util.Date;
import java.util.List;

import com.learning.ddd.onlinestore.inventory.domain.Item;
import com.learning.ddd.onlinestore.payment.domain.Address;
import com.learning.ddd.onlinestore.payment.domain.PaymentMethod;

// DDD: Entity (Aggregate Root)
//
// Order = Information about (ShoppingCart + Payment + PaymentMethod + Shipping/Billing-Address 
// 			+ Delivery + Tracking + Review/Cancellation/Complaint etc)
//
public class Order {

	private String orderId;						//unique Id of the Order
	private Date purchaseDate;					//Date time this Order was purchased
	private String merchantName;				//Name of the Merchant Store from where items were purchased
	private List<Item> items;					//Items purchased
	private int itemCount;						//Number of items purchased
	private Double amount;						//Money paid
	private PaymentMethod paymentMethod;		//Payment method chosen by User for Payment
	private Address billingAddress;				//Address where User wishes to receive Bill
	private Address shippingAddress;			//Address where User wishes to ship the Order
	private Date deliveryDate;					//ETA of the Order
	private String shipmentTrackingId;			//unique Id to track the Order

	
	public Order(String orderId, Date purchaseDate, String merchantName, List<Item> itemList, 
			int itemCount, Double amount, PaymentMethod paymentMethod, Address shippingAddress, 
			Address billingAddress, Date deliveryDate, String shipmentTrackingId) {
		super();
		this.orderId = orderId;
		this.purchaseDate = purchaseDate;
		this.merchantName = merchantName;
		this.items = itemList;
		this.itemCount = itemCount;
		this.amount = amount;
		this.paymentMethod = paymentMethod;
		this.shippingAddress = shippingAddress;
		this.billingAddress = billingAddress;
		this.deliveryDate = deliveryDate;
		this.shipmentTrackingId = shipmentTrackingId;
	}


	public String getOrderId() {
		return orderId;
	}


	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}


	public Date getPurchaseDate() {
		return purchaseDate;
	}


	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}


	public String getMerchantName() {
		return merchantName;
	}


	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}


	public List<Item> getItems() {
		return items;
	}


	public void setItems(List<Item> items) {
		this.items = items;
	}


	public int getItemCount() {
		return itemCount;
	}


	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}


	public Double getAmount() {
		return amount;
	}


	public void setAmount(Double amount) {
		this.amount = amount;
	}


	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}


	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}


	public Address getShippingAddress() {
		return shippingAddress;
	}


	public void setShippingAddress(Address shippingAddress) {
		this.shippingAddress = shippingAddress;
	}


	public Address getBillingAddress() {
		return billingAddress;
	}


	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
	}


	public Date getDeliveryDate() {
		return deliveryDate;
	}


	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}


	public String getShipmentTrackingId() {
		return shipmentTrackingId;
	}


	public void setShipmentTrackingId(String shipmentTrackingId) {
		this.shipmentTrackingId = shipmentTrackingId;
	}


	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", purchaseDate=" + purchaseDate + ", merchantName=" + merchantName
				+ ", items=" + items + ", itemCount=" + itemCount + ", amount=" + amount + ", paymentMethod="
				+ paymentMethod + ", billingAddress=" + billingAddress + ", shippingAddress=" + shippingAddress
				+ ", deliveryDate=" + deliveryDate + ", shipmentTrackingId=" + shipmentTrackingId + "]";
	}

}
