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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.learning.ddd.onlinestore.payment.domain.PaymentMethod;

@Entity
@Table(name = "OrderTransaction", schema="order")
@TypeDef(name = "merchantInfoUserType", typeClass = MerchantInfoUserType.class)
public class OrderTransaction implements Serializable {

	// Merchant details (name, address, phone/email)
	// Receipt/Bill no., Receipt/Bill date,
	// Item details: each item brief, rate, total such items, total value for each
	// set of items
	// Total details: total items, total amount
	// payment method used
	// discount given etc

	private static final long serialVersionUID = -7980847301266401940L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private String transactionNumber;
	private Date purchaseDate;
	private TransactionStatus transactionStatus;
	private PaymentMethod paymentMethod;
	private int totalItems;
	private double totalAmount;
	
	@OneToMany(mappedBy = "transactionReceipt", fetch = FetchType.EAGER,
		cascade = CascadeType.DETACH, orphanRemoval = false)
	private List<OrderItem> items;	// even if a txn is deleted, orderItems shouldn't be deleted
	
	@Type(type = "merchantInfoUserType")
	@Columns(columns = { 
		@Column(name = "name"), @Column(name = "phone"), @Column(name = "email"),
		@Column(name = "addressLine1"), @Column(name = "addressLine2"), 
		@Column(name = "addressLine3")
	})
	private MerchantInfo merchantInfo;
	
	@JsonIgnore
	@ManyToOne(targetEntity = Order.class)
	@JoinColumn(name="orderId", nullable=false)
	private Order order;

	
	public OrderTransaction() {
		
		// allocate a unique Transaction Number which is human readable
		// which can be used by a Consumer to refer to a Transaction
		this.transactionNumber = UUID.randomUUID().toString();
	}


	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public TransactionStatus getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(TransactionStatus transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public int getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public MerchantInfo getMerchantInfo() {
		return merchantInfo;
	}

	public void setMerchantInfo(MerchantInfo merchantInfo) {
		this.merchantInfo = merchantInfo;
	}
	
	public void setOrder(Order order) {
		this.order = order;
	}
	
	public Order getOrder() {
		return order;
	}

	
	// 1-* relationships ---------------- start
	
	public List<OrderItem> getItems() {
		return items;
	}

	public void setItems(List<OrderItem> items) {
		this.items = new ArrayList<>();
		for (OrderItem orderItem : items) {
			orderItem.setTransactionReceipt(this);	// set bi-directional: OrderItem -> TransactionReceipt
			this.items.add(orderItem);		// set bi-directional: TransactionReceipt -> OrderItem
		}
	}
	
	// 1-* relationships ---------------- end
	


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		// all fields except id field
		//result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((items == null) ? 0 : items.hashCode());
		result = prime * result + ((merchantInfo == null) ? 0 : merchantInfo.hashCode());
		result = prime * result + ((paymentMethod == null) ? 0 : paymentMethod.hashCode());
		result = prime * result + ((purchaseDate == null) ? 0 : purchaseDate.hashCode());
		result = prime * result + ((transactionNumber == null) ? 0 : transactionNumber.hashCode());
		long temp;
		temp = Double.doubleToLongBits(totalAmount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + totalItems;
		result = prime * result + ((transactionStatus == null) ? 0 : transactionStatus.hashCode());
		//result = prime * result + ((order == null) ? 0 : order.hashCode());
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
		OrderTransaction other = (OrderTransaction) obj;
		//if (id == null) {				// compare fields which truly represent
		//	if (other.id != null)		// an OrderTransaction, id is not that field
		//		return false;
		//} else if (!id.equals(other.id))
		//	return false;
		if (transactionNumber == null) {
			if (other.transactionNumber != null)
				return false;
		} else if (!transactionNumber.equals(other.transactionNumber))
			return false;
		if (items == null) {
			if (other.items != null)
				return false;
		} else if (!items.equals(other.items))
			return false;
		if (merchantInfo == null) {
			if (other.merchantInfo != null)
				return false;
		} else if (!merchantInfo.equals(other.merchantInfo))
			return false;
		if (paymentMethod != other.paymentMethod)
			return false;
		if (purchaseDate == null) {
			if (other.purchaseDate != null)
				return false;
		} else if (!purchaseDate.equals(other.purchaseDate))
			return false;
		if (Double.doubleToLongBits(totalAmount) != Double.doubleToLongBits(other.totalAmount))
			return false;
		if (totalItems != other.totalItems)
			return false;
		if (transactionStatus != other.transactionStatus)
			return false;
//		if (order == null) {
//			if (other.order != null)
//				return false;
//		} else if (!order.equals(other.order))
//			return false;
		return true;
	}


	@Override
	public String toString() {
		return "OrderTransaction [id=" + id + ", transactionNumber=" + transactionNumber 
				+ ", purchaseDate=" + purchaseDate
				+ ", transactionStatus=" + transactionStatus 
				+ ", paymentMethod=" + paymentMethod 
				+ ", totalItems=" + totalItems 
				+ ", totalAmount=" + totalAmount 
				+ ", items=" + items 
				+ ", merchantInfo=" + merchantInfo
				//+ ", order=" + order 
				+ "]";
	}

}
