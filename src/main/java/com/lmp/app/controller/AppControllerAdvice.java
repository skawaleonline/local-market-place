package com.lmp.app.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.lmp.app.entity.validator.ValidationError;
import com.lmp.app.utils.ValidationErrorBuilder;

@ControllerAdvice(assignableTypes = StoreInventoryController.class)
public class AppControllerAdvice extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    ValidationError error = ValidationErrorBuilder.fromBindingErrors(exception.getBindingResult());
    return super.handleExceptionInternal(exception, error, headers, status, request);
  }
}