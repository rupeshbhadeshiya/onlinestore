package com.learning.ddd.onlinestore.inventory.application;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.learning.ddd.onlinestore.inventory.domain.exception.ItemAlreadyExistsException;

@ControllerAdvice
public class InventoryExceptionHandler {

	
	@ExceptionHandler(Exception.class)
	public @ResponseBody ErrorDetails onException(Exception ex) throws Exception {
		
		System.err.println("============> Inside onException() of InventoryExceptionHandler: ex = " + ex);
		
		ErrorDetails errorDetails = new ErrorDetails();
		
		errorDetails.setTimestamp(new Date());
		
		Throwable cause = ex.getCause();
	    if (cause instanceof ConstraintViolationException) { 
	    	
	    	errorDetails.setStatus(HttpStatus.BAD_REQUEST.value());
			
			List<String> errors = new ArrayList<>();
			((ConstraintViolationException) ex).getConstraintViolations().forEach((constraintViolation) -> {
					errors.add(constraintViolation.getMessage());
				}
			);
			errorDetails.setErrors(errors);

			return errorDetails;
	    	
	    }
		
		throw ex;
		
//		ValidationErrorResponse error = new ValidationErrorResponse();
//		for (ConstraintViolation violation : e.getConstraintViolations()) {
//			error.getViolations().add(new Violation(violation.getPropertyPath().toString(), violation.getMessage()));
//		}
//		return error;
	}
	
//	@ExceptionHandler(ConstraintViolationException.class)
//	@ResponseStatus(HttpStatus.BAD_REQUEST)
//	public @ResponseBody ErrorDetails onConstraintValidationException(ConstraintViolationException ex) {
		
	@ExceptionHandler(TransactionSystemException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody ErrorDetails onTransactionSystemException(TransactionSystemException ex) {
		
		System.err.println("============> Inside onTransactionSystemException() of InventoryExceptionHandler: ex = " + ex);
		
		ErrorDetails errorDetails = new ErrorDetails();
		
		errorDetails.setTimestamp(new Date());
		errorDetails.setStatus(HttpStatus.BAD_REQUEST.value());
		
		List<String> errors = new ArrayList<>();
//	    ex.getBindingResult().getAllErrors().forEach((error) -> {
//	       // String fieldName = ((FieldError) error).getField();
//	        String errorMessage = error.getDefaultMessage();
//	        errors.add(errorMessage);
//	    });
		
		Throwable cause = ((TransactionSystemException) ex).getRootCause();
		
		System.err.println("============> Inside onTransactionSystemException() of InventoryExceptionHandler: cause = " + cause);
		
	    if (cause instanceof ConstraintViolationException) {    
			((ConstraintViolationException) cause).getConstraintViolations()
				.forEach((constraintViolation) -> {
						errors.add(constraintViolation.getMessage());
					}
				);
	    }
		errorDetails.setErrors(errors);

		return errorDetails;
		
//		ValidationErrorResponse error = new ValidationErrorResponse();
//		for (ConstraintViolation violation : e.getConstraintViolations()) {
//			error.getViolations().add(new Violation(violation.getPropertyPath().toString(), violation.getMessage()));
//		}
//		return error;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody ErrorDetails onMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		
		System.err.println("============> Inside onMethodArgumentNotValidException() of InventoryExceptionHandler: ex = " + ex);
		
		ErrorDetails errorDetails = new ErrorDetails();
		
		errorDetails.setTimestamp(new Date());
		errorDetails.setStatus(HttpStatus.BAD_REQUEST.value());
		
		List<String> errors = new ArrayList<>();
	    ex.getBindingResult().getAllErrors().forEach((error) -> {
	       // String fieldName = ((FieldError) error).getField();
	        String errorMessage = error.getDefaultMessage();
	        errors.add(errorMessage);
	    });
//		ex.getConstraintViolations().forEach((constraintViolation) -> {
//				errors.add(constraintViolation.getMessage());
//			}
//		);
		errorDetails.setErrors(errors);

		return errorDetails;
		
//		ValidationErrorResponse error = new ValidationErrorResponse();
//		for (ConstraintViolation violation : e.getConstraintViolations()) {
//			error.getViolations().add(new Violation(violation.getPropertyPath().toString(), violation.getMessage()));
//		}
//		return error;
	}

	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(ItemAlreadyExistsException.class)
	public @ResponseBody ErrorDetails handleItemAlreadyExistsException(ItemAlreadyExistsException ex) {
		
		ErrorDetails errorDetails = new ErrorDetails();
		
		errorDetails.setTimestamp(new Date());
		errorDetails.setStatus(HttpStatus.CONFLICT.value());
		List<String> errors = new ArrayList<>();
        errors.add("Item already exists");
		errorDetails.setErrors(errors);

		return errorDetails;
	}
	
}
