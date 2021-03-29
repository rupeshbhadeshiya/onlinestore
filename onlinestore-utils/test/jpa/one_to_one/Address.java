package jpa.one_to_one;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int addressId;						//unique Id of the Address
	private AddressType addressType;
	private String line1;
	private String line2;
	private String line3;
	private String line4;
	private String pincode;
	private String state;
	private String country;
	
	@OneToOne(mappedBy = "billingAddress")
	private Order order;
	
	public Address() {
	}

	public Address(
			AddressType type, 
			String line1, String line2, String line3, String line4,
			String pincode, String state, String country) {
				this.addressType = type;
				this.line1 = line1;
				this.line2 = line2;
				this.line3 = line3;
				this.line4 = line4;
				this.pincode = pincode;
				this.state = state;
				this.country = country;
	}

	public int getAddressId() {
		return addressId;
	}
	
	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}
	
	public AddressType getAddressType() {
		return addressType;
	}

	public void setAddressType(AddressType addressType) {
		this.addressType = addressType;
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

	public void setOrder(Order order) {
		this.order = order;
	}
	
	public Order getOrder() {
		return order;
	}
	
	@Override
	public String toString() {
		return "Address [addressId=" + addressId + ", type=" + addressType
				+ ", line1=" + line1 + ", line2=" + line2 + ", line3=" + line3
				+ ", line4=" + line4 + ", pincode=" + pincode + ", state=" + state
				+ ", country=" + country
				//+ ", orderId=" + order.getOrderId()
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + addressId;
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((line1 == null) ? 0 : line1.hashCode());
		result = prime * result + ((line2 == null) ? 0 : line2.hashCode());
		result = prime * result + ((line3 == null) ? 0 : line3.hashCode());
		result = prime * result + ((line4 == null) ? 0 : line4.hashCode());
		result = prime * result + ((order == null) ? 0 : order.hashCode());
		result = prime * result + ((pincode == null) ? 0 : pincode.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((addressType == null) ? 0 : addressType.hashCode());
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
		Address other = (Address) obj;
		if (addressId != other.addressId)
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (line1 == null) {
			if (other.line1 != null)
				return false;
		} else if (!line1.equals(other.line1))
			return false;
		if (line2 == null) {
			if (other.line2 != null)
				return false;
		} else if (!line2.equals(other.line2))
			return false;
		if (line3 == null) {
			if (other.line3 != null)
				return false;
		} else if (!line3.equals(other.line3))
			return false;
		if (line4 == null) {
			if (other.line4 != null)
				return false;
		} else if (!line4.equals(other.line4))
			return false;
		if (order == null) {
			if (other.order != null)
				return false;
		} else if (!order.equals(other.order))
			return false;
		if (pincode == null) {
			if (other.pincode != null)
				return false;
		} else if (!pincode.equals(other.pincode))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (addressType != other.addressType)
			return false;
		return true;
	}

	public boolean equalsContents(Address obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
		Address other = (Address) obj;
//		if (addressId != other.addressId)
//			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (line1 == null) {
			if (other.line1 != null)
				return false;
		} else if (!line1.equals(other.line1))
			return false;
		if (line2 == null) {
			if (other.line2 != null)
				return false;
		} else if (!line2.equals(other.line2))
			return false;
		if (line3 == null) {
			if (other.line3 != null)
				return false;
		} else if (!line3.equals(other.line3))
			return false;
		if (line4 == null) {
			if (other.line4 != null)
				return false;
		} else if (!line4.equals(other.line4))
			return false;
		if (order == null) {
			if (other.order != null)
				return false;
		} else if (!order.equals(other.order))
			return false;
		if (pincode == null) {
			if (other.pincode != null)
				return false;
		} else if (!pincode.equals(other.pincode))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (addressType != other.addressType)
			return false;
		return true;
	}
	
}
