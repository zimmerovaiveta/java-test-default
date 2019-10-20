package com.etnetera.hr.controller;

import com.etnetera.hr.rest.Errors;
import com.etnetera.hr.rest.NotFoundError;
import com.etnetera.hr.rest.ValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Main REST controller.
 * 
 * @author Etnetera
 *
 */
@ControllerAdvice
public abstract class EtnRestController {

	@ExceptionHandler({MethodArgumentNotValidException.class})
	public ResponseEntity<Errors> handleValidationException(MethodArgumentNotValidException ex) {
		BindingResult result = ex.getBindingResult();
		Errors errors = new Errors();
		List<ValidationError> errorList = result.getFieldErrors().stream().map(e -> {
			return new ValidationError(e.getField(), e.getCode());
		}).collect(Collectors.toList());
		errors.setErrors(errorList);
		return ResponseEntity.badRequest().body(errors);
	}

	@ExceptionHandler(NotFoundError.class)
	public ResponseEntity<NotFoundError.NotFoundErrorData> handleNotFoundException(NotFoundError ex) {
		return new ResponseEntity<>(ex.getData(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Errors> handleNotFoundException(ConstraintViolationException ex) {
		Errors errors = new Errors();
		List<ValidationError> errorList =  ex.getConstraintViolations().stream().map(e -> {
			return new ValidationError(e.getPropertyPath().toString(), e.getMessage());
		}).collect(Collectors.toList());
		errors.setErrors(errorList);
		return ResponseEntity.badRequest().body(errors);
	}

}
