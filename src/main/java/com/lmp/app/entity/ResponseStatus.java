package com.lmp.app.entity;

public enum ResponseStatus {

  CART_NOT_FOUND(4001), ITEM_OUT_OF_STOCK(4002), ORDER_PLACED(2001);

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
