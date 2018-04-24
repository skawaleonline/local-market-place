package com.lmp.app.entity;

public enum OrderStatus {

  RECIEVED("Recieved"), IN_PROGRESS("In Progress"), READY("Ready"), SHIPPED("Shipped"), DELIVERED(
      "Delivered"), COMPLETED("Completed");

  private OrderStatus(String name) {
    this.name = name;
  }

  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
