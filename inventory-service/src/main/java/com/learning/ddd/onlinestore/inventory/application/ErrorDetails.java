package com.learning.ddd.onlinestore.inventory.application;

import java.util.Date;
import java.util.List;

public class ErrorDetails {

	private Date timestamp;
	private int status;
	private List<String> errors;
	
	public ErrorDetails() {

	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	@Override
	public String toString() {
		return "ErrorDetails [timestamp=" + timestamp + ", status=" + status
				+ ", errors=" + errors + "]";
	}
	
}
