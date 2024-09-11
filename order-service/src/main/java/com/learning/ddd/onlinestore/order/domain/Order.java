package com.learning.ddd.onlinestore.order.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.learning.ddd.onlinestore.cart.domain.CartInfo;
import com.learning.ddd.onlinestore.payment.domain.PaymentMethod;
import com.learning.ddd.onlinestore.product.domain.Product;

@Entity
@Table(name = "OnlineStoreOrder", schema="orders") // MUST keep table name other than 'Order which conflicts with SQL standard word 'Order'
public class Order implements Serializable {

	private static final long serialVersionUID = -498472061562718714L;

	private String consumerId;	// Consumer currently associated with this Cart
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int orderId;
	
	// a value that Consumer can refer to deal with this Order
	@Column(unique = true)
	private String orderNumber;
	
	private int cartId;
	
	//private String name, email, mobileNumber;
	//private Date birthDate;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "billingAddressId", referencedColumnName = "addressId")
	private Address billingAddress;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "shippingAddressId", referencedColumnName = "addressId")
	private Address shippingAddress;
	
	// very important to understand that Item associated with Cart and with Order are different objects
	// an Item cannot point to a Cart and an Order at the same time, therefore we must have two different types of Items
	// say, CartItem and OrderItem
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, 
			fetch = FetchType.EAGER, orphanRemoval = true)
	private List<OrderItem> items;
	private int itemCount;
	private PaymentMethod paymentMethod;		//Payment method chosen by User for Payment
	private Double amount;
	private Date creationDate;
	
	private OrderStatus status;
	
	private boolean productsAvailableInInventory;
	
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL,
			fetch = FetchType.LAZY, orphanRemoval = true)
	private List<OrderTransaction> transactions;
	
	public Order() {
		super();
		allocateOrderNumber();
		this.creationDate = new Date();
		this.status = OrderStatus.PLACED;
	}
	
	public Order(CartInfo cartInfo) {
		this();
		this.setCartId(cartInfo.getCartId());
		this.setItems(convertCartProductsToOrderItems(cartInfo));
		this.setConsumerId(cartInfo.getConsumerId());
		this.setItemCount(cartInfo.getProductCount());
		this.setAmount(cartInfo.computeAmount());
		this.setPaymentMethod(paymentMethod);
		this.setBillingAddress(billingAddress);
		this.setShippingAddress(shippingAddress);
	}

	public Order(Order order) {
		this();
		this.consumerId = order.consumerId;
		this.orderId = order.orderId;
		this.orderNumber = order.orderNumber;
		this.cartId = order.cartId;
		this.billingAddress = order.billingAddress;
		this.shippingAddress = order.shippingAddress;
		this.items = order.items;
		this.itemCount = order.itemCount;
		this.paymentMethod = order.paymentMethod;
		this.amount = order.amount;
		this.creationDate = order.creationDate;
		this.transactions = order.transactions;
	}

	// allocate a new Order Number, a human-readable String
	// which a Consumer can use to refer to a Order later
	private void allocateOrderNumber() {
		this.orderNumber = UUID.randomUUID().toString();
	}
	
	
	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	
	public String getOrderNumber() {
		return orderNumber;
	}

	public Date getCreationDate() {
		return creationDate;
	}
	
	public int getCartId() {
		return cartId;
	}
	
	public void setCartId(int cartId) {
		this.cartId = cartId;
	}

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
	
	public Double getAmount() {
		// cart totalAmount + taxes if any + any other charge
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	public OrderStatus getStatus() {
		return status;
	}
	

	public void setProductsAvailableInInventory(boolean productsAvailableInInventory) {
		this.productsAvailableInInventory = productsAvailableInInventory;
	}
	
	public boolean isProductsAvailableInInventory() {
		return productsAvailableInInventory;
	}
	
	
	public List<Product> getProducts() {
		List<Product> products = new ArrayList<>();
		for (OrderItem orderItem : items) {
			products.add(orderItem.getProduct());
		}
		return products;
	}
	
	public OrderInfo getOrderInfo() {
		return new OrderInfo(orderId, cartId, consumerId, itemCount, getProducts());
	}
	
	
	// 1-* relationships ---------------- start
	
	public void setItems(List<OrderItem> items) {
		this.items = new ArrayList<>();
		for (OrderItem orderItem : items) {
			orderItem.setOrder(this);		// set bi-directional: OrderItem -> Order
			this.items.add(orderItem);		// set bi-directional: Order -> OrderItem
		}
	}
	
	public List<OrderItem> getItems() {
		return items;
	}
	
	public void addTransaction(OrderTransaction transaction) {
		if (this.transactions == null) {
			this.transactions = new ArrayList<>();
		}
		transaction.setOrder(this);			// set bi-directional: TransactionReceipt -> Order
		this.transactions.add(transaction);	// set bi-directional: Order -> TransactionReceipt
	}
	
	public void setTransactions(List<OrderTransaction> transactions) {
		this.transactions = new ArrayList<>();
		for (OrderTransaction transaction : transactions) {
			transaction.setOrder(this);					// set bi-directional: TransactionReceipt -> Order
			this.transactions.add(transaction);	// set bi-directional: Order -> TransactionReceipt
		}
	}
	
	public List<OrderTransaction> getTransactions() {
		return transactions;
	}
	
	// 1-* relationships ---------------- end
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		//result = prime * result + orderId; // use only necessary / important fields but except the "id" field
		result = prime * result + ((consumerId == null) ? 0 : consumerId.hashCode());
		result = prime * result + ((orderNumber == null) ? 0 : orderNumber.hashCode());
		result = prime * result + cartId;
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((paymentMethod == null) ? 0 : paymentMethod.hashCode());
		result = prime * result + ((billingAddress == null) ? 0 : billingAddress.hashCode());
		result = prime * result + ((shippingAddress == null) ? 0 : shippingAddress.hashCode());
		result = prime * result + ((items == null) ? 0 : items.hashCode());
		result = prime * result + itemCount;
		result = prime * result + ((transactions == null) ? 0 : transactions.hashCode());
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
		
		if (consumerId == null) {
			if (other.consumerId != null)
				return false;
		} else if (!consumerId.equals(other.consumerId))
			return false;
		
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		
		if (orderNumber == null) {
			if (other.orderNumber != null)
				return false;
		} else if (!orderNumber.equals(other.orderNumber))
			return false;
		
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		
		if (paymentMethod != other.paymentMethod)
			return false;
		
		if (shippingAddress == null) {
			if (other.shippingAddress != null)
				return false;
		} else if (!shippingAddress.equals(other.shippingAddress))
			return false;
		
		if (billingAddress == null) {
			if (other.billingAddress != null)
				return false;
		} else if (!billingAddress.equals(other.billingAddress))
			return false;

		if (itemCount != other.itemCount)
			return false;
		
		if (items == null) {
			if (other.items != null)
				return false;
		} else if (!items.equals(other.items))
			return false;
		//if (orderId != other.orderId)	// compare fields which truly represent
		//	return false;				// an Order, orderId is not that field
		
		if (transactions == null) {
			if (other.transactions != null)
				return false;
		} else if (!transactions.equals(other.transactions))
			return false;
		
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		
		return true;
	}


	@Override
	public String toString() {
		return "Order [consumerId=" + consumerId 
				+ ", orderId=" + orderId 
				+ ", orderNumber=" + orderNumber
				+ ", cartId=" + cartId
				+ ", creationDate=" + creationDate
				+ ", status=" + status
				+ ", itemCount=" + itemCount
				+ ", productsAvailableInInventory=" + productsAvailableInInventory
				+ ", amount=" + amount
				+ ", items=" + items 
				+ ", paymentMethod=" + paymentMethod
				+ ", billingAddress=" + billingAddress
				+ ", shippingAddress=" + shippingAddress 
				+ ", transactions=" + transactions
				+ "]";
	}
	
	@Override
	public Order clone() throws CloneNotSupportedException {
		return new Order(this);
	}

	// IMP: this is good step, you create new OrderItem from given CartItem
	// if you would be using OrderItem from request as is (say if it was in request)
	// then you would have faced an error 'detached entity passed to persist
	private List<OrderItem> convertCartProductsToOrderItems(
			CartInfo CartInfo) {
		
		List<OrderItem> orderItems = new ArrayList<OrderItem>();
		for (Product productInCart : CartInfo.getProducts()) {
			OrderItem orderItem = new OrderItem(productInCart);
			orderItem.setOrder(this);// set bi-direction (OrderItem -> Order)
			orderItems.add(orderItem);// set bi-direction (Order -> OrderItem)
		}
		
		return orderItems;
	}

	public void rejectOrder() {
		this.status = OrderStatus.REJECTED;
	}

	public void confirmOrder() {
		this.status = OrderStatus.CONFIRMED;		
	}
	
	public void cancelOrder() {
		this.status = OrderStatus.CANCELLED;		
	}

}
