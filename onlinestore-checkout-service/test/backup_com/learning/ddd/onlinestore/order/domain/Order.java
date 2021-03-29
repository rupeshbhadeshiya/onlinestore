package backup_com.learning.ddd.onlinestore.order.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import backup_com.learning.ddd.onlinestore.payment.domain.Address;
import backup_com.learning.ddd.onlinestore.payment.domain.PaymentMethod;

// DDD: Entity (Aggregate Root)
//
// Order = Information about (ShoppingCart + Payment + PaymentMethod + Shipping/Billing-Address 
// 			+ Delivery + Tracking + Review/Cancellation/Complaint etc)
//
@Entity
@Table(name = "OnlineOrder")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int orderId;						//unique Id of the Order
	
	private Date purchaseDate;					//Date time this Order was purchased
	private String merchantName;				//Name of the Merchant Store from where items were purchased
	private List<Item> items;					//Items purchased
	private int itemCount;						//Number of items purchased
	private Double amount;						//Money paid
	private PaymentMethod paymentMethod;		//Payment method chosen by User for Payment
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "billingAddressId", referencedColumnName = "addressId")
	private Address billingAddress;				//Address where User wishes to receive Bill
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "shippingAddressId", referencedColumnName = "addressId")
	private Address shippingAddress;			//Address where User wishes to ship the Order
	
	private Date deliveryDate;					//ETA of the Order
	private String shipmentTrackingId;			//unique Id to track the Order

	public Order() {
	}
	
	public Order(int orderId, Date purchaseDate, String merchantName, 
			List<Item> itemList, int itemCount, Double amount, 
			PaymentMethod paymentMethod, Address billingAddress, 
			Address shippingAddress, Date deliveryDate, String shipmentTrackingId) {
		super();
		this.orderId = orderId;
		this.purchaseDate = purchaseDate;
		this.merchantName = merchantName;
		this.items = itemList;
		this.itemCount = itemCount;
		this.amount = amount;
		this.paymentMethod = paymentMethod;
		this.billingAddress = billingAddress;
		this.shippingAddress = shippingAddress;
		this.deliveryDate = deliveryDate;
		this.shipmentTrackingId = shipmentTrackingId;
	}


	public int getOrderId() {
		return orderId;
	}


	public void setOrderId(int orderId) {
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
		return "Order [orderId=" + orderId + ", purchaseDate=" + purchaseDate
				+ ", merchantName=" + merchantName + ", items=" + items + ", itemCount=" + itemCount
				+ ", amount=" + amount + ", paymentMethod=" + paymentMethod
				+ ", billingAddress=" + billingAddress
				+ ", shippingAddress=" + shippingAddress
				+ ", deliveryDate=" + deliveryDate + ", shipmentTrackingId=" + shipmentTrackingId + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((billingAddress == null) ? 0 : billingAddress.hashCode());
		result = prime * result + ((deliveryDate == null) ? 0 : deliveryDate.hashCode());
		result = prime * result + itemCount;
		result = prime * result + ((items == null) ? 0 : items.hashCode());
		result = prime * result + ((merchantName == null) ? 0 : merchantName.hashCode());
		result = prime * result + orderId;
		result = prime * result + ((paymentMethod == null) ? 0 : paymentMethod.hashCode());
		result = prime * result + ((purchaseDate == null) ? 0 : purchaseDate.hashCode());
		result = prime * result + ((shipmentTrackingId == null) ? 0 : shipmentTrackingId.hashCode());
		result = prime * result + ((shippingAddress == null) ? 0 : shippingAddress.hashCode());
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
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (billingAddress == null) {
			if (other.billingAddress != null)
				return false;
		} else if (!billingAddress.equals(other.billingAddress))
			return false;
		if (deliveryDate == null) {
			if (other.deliveryDate != null)
				return false;
		} else if (!deliveryDate.equals(other.deliveryDate))
			return false;
		if (itemCount != other.itemCount)
			return false;
		if (items == null) {
			if (other.items != null)
				return false;
		} else if (!items.equals(other.items))
			return false;
		if (merchantName == null) {
			if (other.merchantName != null)
				return false;
		} else if (!merchantName.equals(other.merchantName))
			return false;
		if (orderId != other.orderId)
			return false;
		if (paymentMethod != other.paymentMethod)
			return false;
		if (purchaseDate == null) {
			if (other.purchaseDate != null)
				return false;
		} else if (!purchaseDate.equals(other.purchaseDate))
			return false;
		if (shipmentTrackingId == null) {
			if (other.shipmentTrackingId != null)
				return false;
		} else if (!shipmentTrackingId.equals(other.shipmentTrackingId))
			return false;
		if (shippingAddress == null) {
			if (other.shippingAddress != null)
				return false;
		} else if (!shippingAddress.equals(other.shippingAddress))
			return false;
		return true;
	}
	
	

}
