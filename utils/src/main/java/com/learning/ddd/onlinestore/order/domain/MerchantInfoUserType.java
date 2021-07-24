package com.learning.ddd.onlinestore.order.domain;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

public class MerchantInfoUserType implements UserType {

	@Override
	public int[] sqlTypes() {
		// SQL Type for all columns/fields of this object MerchantInfo
		return new int[] { 	Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, 
							Types.VARCHAR, Types.VARCHAR, Types.VARCHAR };
	}

	@Override
	public Class<MerchantInfo> returnedClass() {
		return MerchantInfo.class;
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		return ((x == y) || (x != null && y != null && x.equals(y)));
	}

	@Override
	public int hashCode(Object x) throws HibernateException {
		return x != null ? x.hashCode() : 0;
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
			throws HibernateException, SQLException {

		if (rs.wasNull()) {
			return null;
		}
		
		String name = rs.getString(names[0]);
		String phone = rs.getString(names[1]);
		String email = rs.getString(names[2]);
		String addressLine1 = rs.getString(names[3]);
		String addressLine2 = rs.getString(names[4]);
		String addressLine3 = rs.getString(names[5]);
		
		return new MerchantInfo(name, phone, email, addressLine1, addressLine2, addressLine3);
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
			throws HibernateException, SQLException {
		
		if (value != null) {
			MerchantInfo t = (MerchantInfo) value;
			st.setString(index, t.getName());
			st.setString(index+1, t.getPhone());
			st.setString(index+2, t.getEmail());
			st.setString(index+3, t.getAddressLine1());
			st.setString(index+4, t.getAddressLine2());
			st.setString(index+5, t.getAddressLine3());
		} else {
			st.setString(index, null);
			st.setString(index + 1, null);
			st.setString(index + 2, null);
			st.setString(index + 3, null);
			st.setString(index + 4, null);
			st.setString(index + 5, null);
		}
		
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		return value == null ? null : value;
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException {
		Object deepCopy = deepCopy(value);
		if (!(deepCopy instanceof Serializable)) {
			return (Serializable) deepCopy;
		}
		return null;
	}

	@Override
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return deepCopy(cached);
	}

	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return deepCopy(original);
	}

}
