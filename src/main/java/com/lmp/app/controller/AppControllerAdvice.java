package com.lmp.app.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.lmp.app.entity.ResponseStatus;
import com.lmp.app.entity.validator.ValidationError;
import com.lmp.app.exceptions.CartNotFoundException;
import com.lmp.app.exceptions.InvalidOrderStatusException;
import com.lmp.app.exceptions.OrderNotFoundException;
import com.lmp.app.exceptions.ProductNotInStockException;
import com.lmp.app.model.BaseResponse;
import com.lmp.app.model.CartResponse;
import com.lmp.app.utils.ValidationErrorBuilder;

@ControllerAdvice
public class AppControllerAdvice extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    ValidationError error = ValidationErrorBuilder.fromBindingErrors(exception.getBindingResult());
    return super.handleExceptionInternal(exception, error, headers, status, request);
  }

  @ExceptionHandler({ CartNotFoundException.class })
  public ResponseEntity<BaseResponse> handleCartNotFoundException(Exception ex, WebRequest request) {
    return new ResponseEntity<BaseResponse>(BaseResponse.responseStatus(ResponseStatus.CART_NOT_FOUND), HttpStatus.OK);
  }

  @ExceptionHandler({ ProductNotInStockException.class })
  public ResponseEntity<CartResponse> handleProductNotInStockException(Exception ex, WebRequest request) {
    ProductNotInStockException e = (ProductNotInStockException) ex;
    return new ResponseEntity<CartResponse>(CartResponse.productOutOfStock(e.getOutOfStockItems()), HttpStatus.OK);
  }

  @ExceptionHandler({ OrderNotFoundException.class })
  public ResponseEntity<BaseResponse> handleOrderNotFoundException(Exception ex, WebRequest request) {
    OrderNotFoundException e = (OrderNotFoundException) ex;
    return new ResponseEntity<BaseResponse>(BaseResponse.responseStatus(ResponseStatus.ORDER_NOT_FOUND), HttpStatus.OK);
  }

  @ExceptionHandler({ InvalidOrderStatusException.class })
  public ResponseEntity<BaseResponse> InvalidOrderStatusException(Exception ex, WebRequest request) {
    return new ResponseEntity<BaseResponse>(BaseResponse.responseStatus(ResponseStatus.INVALID_ORDER_STATUS),
        HttpStatus.OK);
  }

}