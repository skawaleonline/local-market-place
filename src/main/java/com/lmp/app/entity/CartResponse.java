package com.lmp.app.entity;

import java.util.List;

public class CartResponse extends BaseResponse {

  private ShoppingCart cart;
  private List<String> itemIds;
  private CustomerOrder order;

  public CartResponse() {
  }

  public static CartResponse orderPlaced(CustomerOrder order) {
    CartResponse cResponse = new CartResponse();
    cResponse.setStatusCode(ResponseStatus.ORDER_PLACED.getCode());
    cResponse.setErrorMessage(ResponseStatus.ORDER_PLACED.toString());
    cResponse.setOrder(order);
    return cResponse;
  }

  public static CartResponse cartNotFound() {
    CartResponse cResponse = new CartResponse();
    cResponse.setStatusCode(ResponseStatus.CART_NOT_FOUND.getCode());
    cResponse.setErrorMessage(ResponseStatus.CART_NOT_FOUND.toString());
    return cResponse;
  }

  public static CartResponse productOutOfStock(List<String> itemIds) {
    CartResponse cResponse = new CartResponse();
    cResponse.setStatusCode(ResponseStatus.ITEM_OUT_OF_STOCK.getCode());
    cResponse.setErrorMessage(ResponseStatus.ITEM_OUT_OF_STOCK.toString());
    cResponse.setItemIds(itemIds);
    return cResponse;
  }

  public CustomerOrder getOrder() {
    return order;
  }

  public void setOrder(CustomerOrder order) {
    this.order = order;
  }

  public List<String> getItemIds() {
    return itemIds;
  }

  public void setItemIds(List<String> itemIds) {
    this.itemIds = itemIds;
  }

  public ShoppingCart getCart() {
    return cart;
  }

  public void setCart(ShoppingCart cart) {
    this.cart = cart;
  }
}
