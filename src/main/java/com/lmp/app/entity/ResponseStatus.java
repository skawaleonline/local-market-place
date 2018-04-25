package com.lmp.app.entity;

public enum ResponseStatus {

  ORDER_RECIEVED(2001),
  ORDER_PLACED(2002),
  ORDER_CANCELLED(2003),
  MOVED_TO_LIST(2004),
  CART_NOT_FOUND(4001),
  ITEM_OUT_OF_STOCK(4002),
  ORDER_NOT_FOUND(4003),
  INVALID_ORDER_STATUS(4004),
  ITEM_NOT_FOUND(4005);

  private int code;

  private ResponseStatus(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }
}
