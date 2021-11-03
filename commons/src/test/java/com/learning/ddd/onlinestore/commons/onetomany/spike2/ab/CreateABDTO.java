package com.learning.ddd.onlinestore.commons.onetomany.spike2.ab;

public class CreateABDTO {

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
