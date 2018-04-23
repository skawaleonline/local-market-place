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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lmp.app.entity.BaseResponse;
import com.lmp.app.entity.ShoppingCart;
import com.lmp.app.entity.ShoppingCartRequest;
import com.lmp.app.entity.validator.CartRequestValidator;
import com.lmp.app.service.ShoppingCartService;
import com.lmp.app.utils.ValidationErrorBuilder;

@RestController
@RequestMapping("/cart")
public class ShoppingCartController extends BaseController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private CartRequestValidator cartRequestValidator;

  @Autowired
  private ShoppingCartService service;

  @Autowired
  public ShoppingCartController(CartRequestValidator cartRequestValidator) {
      this.cartRequestValidator = cartRequestValidator;
  }

  @InitBinder("searchRequest")
  public void setupBinder(WebDataBinder binder) {
      binder.addValidators(cartRequestValidator);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<?> getCart(@Valid @PathVariable("id") String id) {
    logger.info("getting cart with id {} " + id);
    ShoppingCart cart = service.getCart(new ShoppingCartRequest(id));

    if (cart == null) {
      logger.info("no cart found or add failed {}", id);
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<ShoppingCart>(cart, HttpStatus.OK);
  }
  @RequestMapping(value = "/add", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<?> addToCart(@Valid @RequestBody ShoppingCartRequest cartRequest, Errors errors) {
    if (errors.hasErrors()) {
      return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
    }
    logger.info("shopping cart request " + cartRequest.toString());
    ShoppingCart cart = service.add(cartRequest);

    if (cart == null) {
      logger.info("no cart found or add failed {}", cartRequest.toString());
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<ShoppingCart>(cart, HttpStatus.OK);
  }

  @RequestMapping(value = "/remove", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<?> removeFromCart(@Valid @RequestBody ShoppingCartRequest cartRequest, Errors errors) {
    if (errors.hasErrors()) {
      return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
    }
    logger.info("shopping cart request " + cartRequest.toString());
    ShoppingCart cart = service.remove(cartRequest);

    if (cart == null) {
      logger.info("no cart found or add failed {}", cartRequest.toString());
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<ShoppingCart>(cart, HttpStatus.OK);
  }

  @RequestMapping(value = "/update", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<?> updateCart(@Valid @RequestBody ShoppingCartRequest cartRequest, Errors errors) {
    if (errors.hasErrors()) {
      return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
    }
    logger.info("shopping cart request " + cartRequest.toString());
    ShoppingCart cart = service.update(cartRequest);
    // logger.info("getting store details for store id {}", storeId);

    if (cart == null) {
      logger.info("no cart found or add failed {}", cartRequest.toString());
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<ShoppingCart>(cart, HttpStatus.OK);
  }
}
