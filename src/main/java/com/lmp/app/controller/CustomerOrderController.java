package com.lmp.app.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;
import com.lmp.app.model.BaseResponse;
import com.lmp.app.model.CustomerOrderRequest;
import com.lmp.app.model.validator.CustomerOrderRequestValidator;
import com.lmp.app.service.CustomerOrderService;
import com.lmp.app.utils.ValidationErrorBuilder;

@RestController
public class CustomerOrderController extends BaseController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private CustomerOrderService service;

  private CustomerOrderRequestValidator customerOrderRequestValidator;

  @Autowired
  public CustomerOrderController(CustomerOrderRequestValidator customerOrderRequestValidator) {
      this.customerOrderRequestValidator = customerOrderRequestValidator;
  }

  @InitBinder("customerOrderRequest")
  public void setupBinder(WebDataBinder binder) {
      binder.addValidators(customerOrderRequestValidator);
  }

  @RequestMapping(value = "/customer-order", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<?> findByUserId(@Valid @RequestBody CustomerOrderRequest cRequest, Errors errors) {
    if (errors.hasErrors()) {
      return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
    }
    logger.info("customer order for the request " + cRequest.toString());
    return new ResponseEntity<BaseResponse>(service.getOrdersByUserId(cRequest), HttpStatus.OK);
  }

  @RequestMapping(value = "/store-order", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<?> findByStoreIdAndUserId(@Valid @RequestBody CustomerOrderRequest cRequest, Errors errors) {
    if(Strings.isNullOrEmpty(cRequest.getStoreId())) {
      errors.reject("storeId.required", "storeId is required");
    }
    if (errors.hasErrors()) {
      return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
    }
    logger.info("customer order for the request " + cRequest.toString());
    return new ResponseEntity<BaseResponse>(service.getOrdersByStoreId(cRequest), HttpStatus.OK);
  }
}
