package com.learning.ddd.onlinestore.domain.event.pubsub.exception;

public class DomainEventPublishingFailedException extends RuntimeException {

	private static final long serialVersionUID = 8236224033895754155L;

	public DomainEventPublishingFailedException(Exception ex) {
		super(ex);
	}
	
}
