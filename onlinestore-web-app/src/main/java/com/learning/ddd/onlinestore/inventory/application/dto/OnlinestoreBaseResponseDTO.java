package com.learning.ddd.onlinestore.inventory.application.dto;

import java.io.Serializable;
import java.util.List;

public class OnlinestoreBaseResponseDTO implements Serializable {

	private static final long serialVersionUID = -4947538350145476194L;
	
	private List<String> errors;
	
	public OnlinestoreBaseResponseDTO() {
	}
	
	public OnlinestoreBaseResponseDTO(List<String> errors) {
		this.errors = errors;
	}
	
	public List<String> getErrors() {
		return errors;
	}
	
	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

}
