package com.learning.ddd.onlinestore.commons.onetomany.spike2.ab;

import java.io.Serializable;

public class CreateABDTO implements Serializable {

	private static final long serialVersionUID = -8643942763305596141L;
	
	private String aName;
	private String bName;
	
	public CreateABDTO() {
	}
	
	public String getaName() {
		return aName;
	}
	public void setaName(String aName) {
		this.aName = aName;
	}
	
	public String getbName() {
		return bName;
	}
	public void setbName(String bName) {
		this.bName = bName;
	}

	@Override
	public String toString() {
		return "CreateAandBkaDTO [aName=" + aName + ", bName=" + bName + "]";
	}
	
}
