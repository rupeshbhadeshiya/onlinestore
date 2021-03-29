
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Address {
	
//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	private int addressId;
//	private String line1, line2, line3, postalCode, state, country;
//	private AddressType addressType;
//
//	@OneToOne(mappedBy = "address")
//	private Order order;
//	
//	public Address() {
//	}
//
//	public int getAddressId() {
//		return addressId;
//	}
//
//	public void setAddressId(int addressId) {
//		this.addressId = addressId;
//	}
//
//	public String getLine1() {
//		return line1;
//	}
//
//	public void setLine1(String line1) {
//		this.line1 = line1;
//	}
//
//	public String getLine2() {
//		return line2;
//	}
//
//	public void setLine2(String line2) {
//		this.line2 = line2;
//	}
//
//	public String getLine3() {
//		return line3;
//	}
//
//	public void setLine3(String line3) {
//		this.line3 = line3;
//	}
//
//	public String getPostalCode() {
//		return postalCode;
//	}
//
//	public void setPostalCode(String postalCode) {
//		this.postalCode = postalCode;
//	}
//
//	public String getState() {
//		return state;
//	}
//
//	public void setState(String state) {
//		this.state = state;
//	}
//
//	public String getCountry() {
//		return country;
//	}
//
//	public void setCountry(String country) {
//		this.country = country;
//	}
//
//	public Order getOrder() {
//		return order;
//	}
//
//	public void setOrder(Order order) {
//		this.order = order;
//	}
//
//	public AddressType getAddressType() {
//		return addressType;
//	}
//
//	public void setAddressType(AddressType addressType) {
//		this.addressType = addressType;
//	}
//
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + addressId;
//		result = prime * result + ((addressType == null) ? 0 : addressType.hashCode());
//		result = prime * result + ((country == null) ? 0 : country.hashCode());
//		result = prime * result + ((line1 == null) ? 0 : line1.hashCode());
//		result = prime * result + ((line2 == null) ? 0 : line2.hashCode());
//		result = prime * result + ((line3 == null) ? 0 : line3.hashCode());
//		result = prime * result + ((postalCode == null) ? 0 : postalCode.hashCode());
//		result = prime * result + ((state == null) ? 0 : state.hashCode());
//		result = prime * result + ((order == null) ? 0 : order.hashCode());
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		Address other = (Address) obj;
//		if (addressId != other.addressId)
//			return false;
//		if (addressType != other.addressType)
//			return false;
//		if (country == null) {
//			if (other.country != null)
//				return false;
//		} else if (!country.equals(other.country))
//			return false;
//		if (line1 == null) {
//			if (other.line1 != null)
//				return false;
//		} else if (!line1.equals(other.line1))
//			return false;
//		if (line2 == null) {
//			if (other.line2 != null)
//				return false;
//		} else if (!line2.equals(other.line2))
//			return false;
//		if (line3 == null) {
//			if (other.line3 != null)
//				return false;
//		} else if (!line3.equals(other.line3))
//			return false;
//		if (postalCode == null) {
//			if (other.postalCode != null)
//				return false;
//		} else if (!postalCode.equals(other.postalCode))
//			return false;
//		if (state == null) {
//			if (other.state != null)
//				return false;
//		} else if (!state.equals(other.state))
//			return false;
//		if (order == null) {
//			if (other.order != null)
//				return false;
//		} else if (order.getOrderId() != other.order.getOrderId())	// custom added, can't equal on Order
//			return false;											// because Order also equals on Address
////		} else if (!order.equals(other.order))						// so equals on Order here means infinite
////			return false;											// loop, to avoid it check with orderIds
//		return true;
//	}
//
//	@Override
//	public String toString() {
//		return "Address [addressId=" + addressId + ", line1=" + line1 + ", line2=" + line2
//				+ ", line3=" + line3 + ", postalCode=" + postalCode + ", state=" + state
//				+ ", country=" + country + ", addressType=" + addressType
//				+ "]";
//	}

	
}
